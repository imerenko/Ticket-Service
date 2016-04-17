package com.my.ticketservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.my.ticketservice.conf.VenueConfig;
import com.my.ticketservice.dao.VenueRepository;
import com.my.ticketservice.domain.Level;
import com.my.ticketservice.domain.Reservation;
import com.my.ticketservice.domain.Seat;
import com.my.ticketservice.domain.SeatAvailabilityType;
import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.domain.Venue;
import com.my.ticketservice.exception.ExceptionConstants;
import com.my.ticketservice.exception.TicketServiceException;
import com.my.ticketservice.util.EmailValidator;
import com.my.ticketservice.util.IdGenerator;

/**
 * 
 * Ticket Service interface implementation. All write operations
 * (findAndHoldSeats, reserveSeats) use pessimistic locks (synchronized venue),
 * So before modifying any data (level, seat, reservations, seat holds) in venue
 * please obtain a lock. read operation numSeatsAvailable is not blocked.
 *
 */
@Service
public class TicketServiceImpl implements TicketService {

	@Resource
	private EmailValidator emailValidator;

	@Resource
	private VenueRepository venueRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.my.ticketservice.service.TicketService#numSeatsAvailable(java.util.
	 * Optional)
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		Venue venue = venueRepository.getVenue();
		if (venueLevel.isPresent()) {
			Level level = venue.getLevel(venueLevel.get());
			if (level == null) {
				throw new TicketServiceException(
						String.format(ExceptionConstants.LEVEL_DOES_NOT_EXIST, venueLevel.get()));
			}
			return numSeatsAvailablePerLevel(level);
		}

		return numSeatsAvailableForLevels(venue.getLevelsByPriority());

	}

	/**
	 * @param levels
	 *            to search in.
	 * @return number of available(FREE) seats within levels.
	 */
	private int numSeatsAvailableForLevels(List<Level> levels) {
		int number = 0;

		for (Level level : levels) {
			number += numSeatsAvailablePerLevel(level);
		}

		return number;
	}

	/**
	 * @param level
	 *            to search in.
	 * @return number of available(FREE) seats within level.
	 */
	private int numSeatsAvailablePerLevel(Level level) {
		int number = 0;
		List<Seat> seats = level.getSeats();

		for (Seat seat : seats) {
			if (seat.getSeatAvailabilityType().equals(SeatAvailabilityType.FREE)) {
				number++;
			}
		}

		return number;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.my.ticketservice.service.TicketService#findAndHoldSeats(int,
	 * java.util.Optional, java.util.Optional, java.lang.String)
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {

		validateHoldSeatsInput(numSeats, minLevel, maxLevel, customerEmail);

		Venue venue = venueRepository.getVenue();

		/*
		 * synchronized venue when search and create a seat hold. So other
		 * threads will not be able to modify venue data.
		 */
		synchronized (venue) {
			List<Level> levels;
			if (minLevel.isPresent() && maxLevel.isPresent()) {
				levels = venue.getLevelsByPriority(minLevel.get(), maxLevel.get());

			} else {
				levels = venue.getLevelsByPriority();
			}

			List<Seat> seats = getBestSeats(numSeats, levels);
			changeSeatsAvailabilityType(seats, SeatAvailabilityType.HELD);
			SeatHold seatHold = createSeatHold(customerEmail, seats);
			venue.putSeatHold(seatHold.getId(), seatHold);
			return seatHold;

		}

	}

	/**
	 * Validate parameters for for creating a seat hold
	 * 
	 * @param numSeats
	 *            number of seats to find and hold
	 * @param minLevel
	 *            the minimum venue level
	 * @param maxLevel
	 *            the macimum venue level
	 * @param customerEmail
	 *            customer email
	 * @throws TicketServiceException
	 *             if validation fails
	 */
	private void validateHoldSeatsInput(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {

		if (numSeats < 1) {
			throw new TicketServiceException(ExceptionConstants.NUMBER_OF_SEATS_MUST_BE_MORE_THEN_ZERO);
		}

		if ((minLevel.isPresent() && !maxLevel.isPresent()) || (!minLevel.isPresent() && maxLevel.isPresent())) {
			throw new TicketServiceException(ExceptionConstants.MIN_AND_MAX_LEVEL_BOTH_SHOULD_HAVE_VALUE_OR_NULL);
		}

		if (minLevel.isPresent() && maxLevel.isPresent() && minLevel.get() > maxLevel.get()) {
			throw new TicketServiceException(ExceptionConstants.MIN_CANNOT_BE_MORE_THAN_MAX_LEVEL);
		}

		if (!emailValidator.validate(customerEmail)) {
			throw new TicketServiceException(ExceptionConstants.INCORRECT_EMAIL);

		}

	}

	/**
	 * levels must be prioritised (sorted). The lower number(index) the better
	 * level
	 * 
	 * @param numSeats
	 *            number of seats to hold
	 * @param levels
	 *            levels where seats will be held
	 * @return list of best available seats
	 * @throws TicketServiceException
	 *             if no enough seats
	 */
	private List<Seat> getBestSeats(int numSeats, List<Level> levels) {
		List<Seat> seats = new ArrayList<>();

		for (Level level : levels) {
			List<Seat> levelSeats = level.getSeats();
			for (Seat levelSeat : levelSeats) {
				if (levelSeat.getSeatAvailabilityType().equals(SeatAvailabilityType.FREE)) {
					seats.add(levelSeat);
					if (numSeats == seats.size()) {
						return seats;
					}
				}
			}
		}

		throw new TicketServiceException(ExceptionConstants.NOT_ENOUGH_SEATS);

	}

	/**
	 * @param customerEmail
	 *            customer email
	 * @param seats
	 *            seats for the seathold
	 * @return SeatHold seat hold with expirationDate
	 */
	private SeatHold createSeatHold(String customerEmail, List<Seat> seats) {
		SeatHold seatHold = new SeatHold(IdGenerator.getNextId(), seats, customerEmail);
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime expirationDate = currentDateTime.plusSeconds(VenueConfig.numOfSecondsExpirationTime());
		seatHold.setExpirationDate(expirationDate);
		return seatHold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.my.ticketservice.service.TicketService#reserveSeats(int,
	 * java.lang.String)
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		Venue venue = venueRepository.getVenue();

		/*
		 * synchronized venue when reserve seats. So other threads will not be
		 * able to modify venue data.
		 */
		synchronized (venue) {

			validateReserveInput(seatHoldId, customerEmail, venue);

			SeatHold seatHold = venue.getSeatHold(seatHoldId);
			List<Seat> seats = seatHold.getSeats();
			changeSeatsAvailabilityType(seats, SeatAvailabilityType.RESERVED);

			String code = (new Date()).toString() + customerEmail;
			Reservation reservation = new Reservation(code, seats, customerEmail);
			venue.putReservation(code, reservation);
			venue.removeSeatHold(seatHoldId);
			return code;
		}

	}

	/**
	 * @param seatHoldId
	 *            set hold id which seats to reserve
	 * @param customerEmail
	 *            customer email
	 * @param venue
	 *            venue
	 * @throws TicketServiceException
	 *             if validation fails
	 */
	private void validateReserveInput(int seatHoldId, String customerEmail, Venue venue) {
		SeatHold seatHold = venue.getSeatHold(seatHoldId);
		if (seatHold == null) {
			throw new TicketServiceException(String.format(ExceptionConstants.NO_SEATHOLD_WITH_ID, seatHoldId));
		}
		if (!seatHold.getUserEmail().equals(customerEmail)) {
			throw new TicketServiceException(
					String.format(ExceptionConstants.EMAIL_MISMATCH, customerEmail, seatHoldId));
		}
	}

	/**
	 * Change seat seatAvailabilityType
	 * 
	 * @param seats
	 *            seats where change seatAvailabilityType
	 * @param seatAvailabilityType
	 *            seatAvailabilityType set to seats
	 */
	private void changeSeatsAvailabilityType(List<Seat> seats, SeatAvailabilityType seatAvailabilityType) {
		for (Seat seat : seats) {
			seat.setSeatAvailabilityType(seatAvailabilityType);
		}
	}

}
