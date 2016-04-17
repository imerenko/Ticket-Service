package com.my.ticketservice.service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.my.ticketservice.dao.VenueRepository;
import com.my.ticketservice.domain.Seat;
import com.my.ticketservice.domain.SeatAvailabilityType;
import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.domain.Venue;

/**
 * Scheduled task that runs every 60 seconds. It cleans all expired seat holds
 * in venue.
 *
 */
@Service
public class SeatHoldCleanService {

	@Resource
	private VenueRepository venueRepository;

	/**
	 * Removes expired seat holds in venue. Set the seatholds's seats from HELD
	 * to FREE state. During the cleaning the thread obtains lock of venue object.
	 */
	@Scheduled(fixedRate = 60000)
	public void cleanSeatHolds() {
		Venue venue = venueRepository.getVenue();
		synchronized (venue) {
			LocalDateTime localDateTime = LocalDateTime.now();
			Map<Integer, SeatHold> seatHolds = venue.getSeatHolds();
			for (Iterator<Map.Entry<Integer, SeatHold>> it = seatHolds.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, SeatHold> entry = it.next();
				SeatHold seatHold = entry.getValue();
				if (seatHold.getExpirationDate().isBefore(localDateTime)) {
					for (Seat seat : seatHold.getSeats()) {
						seat.setSeatAvailabilityType(SeatAvailabilityType.FREE);
					}

					it.remove();
				}
			}
		}

	}

}
