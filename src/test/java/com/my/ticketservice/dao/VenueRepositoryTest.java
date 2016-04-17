package com.my.ticketservice.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.my.ticketservice.domain.Venue;

public class VenueRepositoryTest {
	private VenueRepository venueRepository;
	
	@Before
	public void setUp() {
		venueRepository = new VenueRepositoryImpl();
	}
	
	@Test
	public void getVenue() {
		Venue venue = venueRepository.getVenue();
		
		assertEquals(4, venue.getLevelsByPriority().size());
		assertEquals(1250, venue.getLevel(1).getSeats().size());
		assertEquals(2000, venue.getLevel(2).getSeats().size());
		assertEquals(1500, venue.getLevel(3).getSeats().size());
		assertEquals(1500, venue.getLevel(4).getSeats().size());
		assertEquals(15, venue.getLevel(4).getSeats().get(1499).getRowNumber());
		assertEquals(100, venue.getLevel(4).getSeats().get(1499).getSeatInARowNumber());
		
	}

}
