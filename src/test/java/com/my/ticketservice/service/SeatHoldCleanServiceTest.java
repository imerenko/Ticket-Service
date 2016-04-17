package com.my.ticketservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.my.ticketservice.dao.VenueRepository;
import com.my.ticketservice.domain.Seat;
import com.my.ticketservice.domain.SeatAvailabilityType;
import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.domain.Venue;
import com.my.ticketservice.domain.VenueFixture;

public class SeatHoldCleanServiceTest {

	@InjectMocks
	private SeatHoldCleanService seatHoldCleanService;

	@Mock
	private VenueRepository venueRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void cleanSeatHolds() {
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		int expiredId = 10;
		LocalDateTime expiredDate = currentDateTime.minusDays(1);
		SeatHold expiredSeatHold = new SeatHold();
		expiredSeatHold.setId(expiredId);
		expiredSeatHold.setExpirationDate(expiredDate);
		List<Seat> expiredSeats = VenueFixture.createSeats(5, SeatAvailabilityType.HELD);
		expiredSeatHold.setSeats(expiredSeats);
		
		
		int liveId = 20;
		LocalDateTime liveDate = currentDateTime.plusDays(1);
		SeatHold liveSeatHold = new SeatHold();
		liveSeatHold.setId(liveId);
		liveSeatHold.setExpirationDate(liveDate);
		List<Seat> liveSeats = VenueFixture.createSeats(5, SeatAvailabilityType.HELD);
		liveSeatHold.setSeats(liveSeats);
		
		venue.putSeatHold(expiredId, expiredSeatHold);
		venue.putSeatHold(liveId, liveSeatHold);
		
		seatHoldCleanService.cleanSeatHolds();
		
		assertNull(venue.getSeatHold(expiredId));
		assertNotNull(venue.getSeatHold(liveId));
		assertSeatAvalabilityType(expiredSeats, SeatAvailabilityType.FREE);
		assertSeatAvalabilityType(liveSeats, SeatAvailabilityType.HELD);
		
		

	}
	
	private void assertSeatAvalabilityType(List<Seat> reservedSeats, SeatAvailabilityType seatAvailabilityType) {
		for (Seat seat : reservedSeats) {
			assertEquals(seatAvailabilityType, seat.getSeatAvailabilityType());
		}
	}

}
