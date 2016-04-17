package com.my.ticketservice.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.my.ticketservice.conf.VenueConfig;
import com.my.ticketservice.domain.Level;
import com.my.ticketservice.domain.Seat;
import com.my.ticketservice.domain.Venue;

@Repository
public class VenueRepositoryImpl implements VenueRepository {

	private Venue venue;

	/**
	 * create a new venue
	 */
	public VenueRepositoryImpl() {
		createVenue();

	}
	
	@Override
	public Venue getVenue() {
		return venue;
	}

	@Override
	public void recreateVenue() {
		createVenue();
	}

	/**
	 * Creates Venue object (including levels and seats) base on configuration
	 */
	private void createVenue() {
		venue = new Venue();

		Object[][] levelInformation = VenueConfig.getLevelInformation();

		int seatCount = 0;
		for (int i = 0; i < levelInformation.length; i++) {
			Level level = new Level((Integer) levelInformation[i][0], (String) levelInformation[i][1],
					(Integer) levelInformation[i][2], (Integer) levelInformation[i][3],
					(BigDecimal) levelInformation[i][4]);
			venue.putLevel(level.getLevelInfo().getId(), level);

			List<Seat> seats = new ArrayList<>(
					level.getLevelInfo().getNumverOfRows() * level.getLevelInfo().getNumberOfSeatsInARow());

			for (int j = 1; j <= level.getLevelInfo().getNumverOfRows(); j++) {
				for (int k = 1; k <= level.getLevelInfo().getNumberOfSeatsInARow(); k++) {
					seatCount++;
					Seat seat = new Seat(seatCount, j, k, level.getLevelInfo());
					seats.add(seat);
				}
			}

			level.setSeats(seats);

		}
	}

}
