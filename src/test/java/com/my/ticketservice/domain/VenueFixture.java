package com.my.ticketservice.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VenueFixture {

	public static Venue createVenueWithLevelOneAndTwo() {
		Venue venue = new Venue();
		Map<Integer, Level> levels = createsLevel(2, 5);
		venue.setLevels(levels);
		return venue;
	}

	public static Map<Integer, Level> createsLevel(int numberOflevels, int numberOfSeatsPerLevel) {
		Map<Integer, Level> levels = new HashMap<Integer, Level>();
		for (int i = 0; i < numberOflevels; i++) {
			Level level = new Level();
			level.setSeats(createSeats(numberOfSeatsPerLevel));
			levels.put(i + 1, level);
		}
		return levels;
	}

	public static List<Seat> createSeats(int number) {
		List<Seat> seats = new ArrayList<>();

		for (int i = 0; i < number; i++) {
			Seat seat = new Seat();
			seats.add(seat);
		}

		return seats;
	}

	public static List<Seat> createSeats(int number, SeatAvailabilityType seatAvailabilityType) {
		List<Seat> seats = new ArrayList<>();

		for (int i = 0; i < number; i++) {
			Seat seat = new Seat();
			seat.setSeatAvailabilityType(seatAvailabilityType);
			seats.add(seat);
		}

		return seats;
	}

}
