package com.my.ticketservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.my.ticketservice.dao.VenueRepository;
import com.my.ticketservice.domain.Seat;
import com.my.ticketservice.domain.SeatAvailabilityType;
import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.domain.Venue;
import com.my.ticketservice.domain.VenueFixture;
import com.my.ticketservice.exception.ExceptionConstants;
import com.my.ticketservice.exception.TicketServiceException;
import com.my.ticketservice.util.EmailValidator;

public class TicketServiceImplTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@InjectMocks
	private TicketServiceImpl ticketService;

	@Mock
	private VenueRepository venueRepository;
	
	@Mock
	private EmailValidator emailValidator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void numSeatsAvailableReturnsExceptionWhenLevelDoesNotExist() {
		int levelNumInt = 3;
		
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(String.format(ExceptionConstants.LEVEL_DOES_NOT_EXIST, levelNumInt));
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);

		
		Optional<Integer> levelNum = Optional.of(levelNumInt);

		ticketService.numSeatsAvailable(levelNum);

	}
	
	@Test
	public void numSeatsAvailableReturnsSeatsPerLevel() {
		int levelNumInt = 2;
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		venue.getLevel(levelNumInt).getSeats().get(0).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		venue.getLevel(levelNumInt).getSeats().get(2).setSeatAvailabilityType(SeatAvailabilityType.RESERVED);
		when(venueRepository.getVenue()).thenReturn(venue);
		
		Optional<Integer> levelNum = Optional.of(levelNumInt);
		
		int number = ticketService.numSeatsAvailable(levelNum);
		
		assertEquals(3, number);
	}
	
	@Test
	public void numSeatsAvailableReturnsSeatsPerAllLevel() {
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		venue.getLevel(1).getSeats().get(0).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		venue.getLevel(1).getSeats().get(2).setSeatAvailabilityType(SeatAvailabilityType.RESERVED);
		venue.getLevel(2).getSeats().get(3).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		when(venueRepository.getVenue()).thenReturn(venue);
		
		Optional<Integer> levelNum = Optional.ofNullable(null);
		
		int number = ticketService.numSeatsAvailable(levelNum);
		
		assertEquals(7, number);
	}
	
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenNumSeatsLessThenOne() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.NUMBER_OF_SEATS_MUST_BE_MORE_THEN_ZERO);
		
		ticketService.findAndHoldSeats(0, Optional.ofNullable(null), Optional.ofNullable(null), null);
	}
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenMinIsPresentMaxIsNot() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.MIN_AND_MAX_LEVEL_BOTH_SHOULD_HAVE_VALUE_OR_NULL);
		
		ticketService.findAndHoldSeats(1, Optional.of(1), Optional.ofNullable(null), null);
	}
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenMinIsNotPresentMaxIs() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.MIN_AND_MAX_LEVEL_BOTH_SHOULD_HAVE_VALUE_OR_NULL);
		
		ticketService.findAndHoldSeats(1, Optional.ofNullable(null), Optional.of(1), null);
	}
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenMinMoreThenMax() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.MIN_CANNOT_BE_MORE_THAN_MAX_LEVEL);
		
		ticketService.findAndHoldSeats(1, Optional.of(2), Optional.of(1), null);
	}
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenIncorrectEmail() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.INCORRECT_EMAIL);
		
		String email = "my!gmail.com";
		when(emailValidator.validate(email)).thenReturn(false);
		
		ticketService.findAndHoldSeats(1, Optional.ofNullable(null), Optional.ofNullable(null), email);
	}
	
	@Test
	public void findAndHoldSeatsThrowsExceptionWhenNotEnoughSeats() {
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(ExceptionConstants.NOT_ENOUGH_SEATS);
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);
		
		String email = "my@gmail.com";
		when(emailValidator.validate(email)).thenReturn(true);
		
		ticketService.findAndHoldSeats(11, Optional.ofNullable(null), Optional.ofNullable(null), email);
	}
	
	@Test
	public void findAndHoldSeatsReturnBestSeatsForAllLevel() {
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		venue.getLevel(1).getSeats().get(0).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		venue.getLevel(1).getSeats().get(2).setSeatAvailabilityType(SeatAvailabilityType.RESERVED);
		venue.getLevel(2).getSeats().get(3).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		when(venueRepository.getVenue()).thenReturn(venue);
		
		String email = "my@gmail.com";
		when(emailValidator.validate(email)).thenReturn(true);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(4, Optional.ofNullable(null), Optional.ofNullable(null), email);
		
		assertEquals(4, seatHold.getSeats().size());
		assertEquals(email, seatHold.getUserEmail());
		assertTrue(seatHold.getId() > 0);
		assertEquals(0, ticketService.numSeatsAvailable(Optional.of(1)));
		assertEquals(3, ticketService.numSeatsAvailable(Optional.of(2)));
		assertSeatAvalabilityType(seatHold.getSeats(), SeatAvailabilityType.HELD);
	}
	
	@Test
	public void findAndHoldSeatsReturnBestSeatsFor2Levels() {
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		venue.getLevel(1).getSeats().get(0).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		venue.getLevel(1).getSeats().get(2).setSeatAvailabilityType(SeatAvailabilityType.RESERVED);
		venue.getLevel(2).getSeats().get(3).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		when(venueRepository.getVenue()).thenReturn(venue);
		
		String email = "my@gmail.com";
		when(emailValidator.validate(email)).thenReturn(true);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(4, Optional.of(1), Optional.of(2), email);
		
		assertEquals(4, seatHold.getSeats().size());
		assertEquals(email, seatHold.getUserEmail());
		assertTrue(seatHold.getId() > 0);
		assertEquals(0, ticketService.numSeatsAvailable(Optional.of(1)));
		assertEquals(3, ticketService.numSeatsAvailable(Optional.of(2)));
		assertSeatAvalabilityType(seatHold.getSeats(), SeatAvailabilityType.HELD);
	}
	
	@Test
	public void findAndHoldSeatsReturnBestSeatsForSingleLevel() {
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		venue.getLevel(1).getSeats().get(0).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		venue.getLevel(1).getSeats().get(2).setSeatAvailabilityType(SeatAvailabilityType.RESERVED);
		venue.getLevel(2).getSeats().get(3).setSeatAvailabilityType(SeatAvailabilityType.HELD);
		when(venueRepository.getVenue()).thenReturn(venue);
		
		String email = "my@gmail.com";
		when(emailValidator.validate(email)).thenReturn(true);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(2, Optional.of(2), Optional.of(2), email);
		
		assertEquals(2, seatHold.getSeats().size());
		assertEquals(email, seatHold.getUserEmail());
		assertTrue(seatHold.getId() > 0);
		assertEquals(3, ticketService.numSeatsAvailable(Optional.of(1)));
		assertEquals(2, ticketService.numSeatsAvailable(Optional.of(2)));
		assertSeatAvalabilityType(seatHold.getSeats(), SeatAvailabilityType.HELD);
	}
	
	@Test
	public void reserveSeatsThrowsExceptionIfNoSeatHoldById() {
		int seatHoldId = 10;
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(String.format(ExceptionConstants.NO_SEATHOLD_WITH_ID, seatHoldId));
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);


		ticketService.reserveSeats(seatHoldId, null);
	}
	
	@Test
	public void reserveSeatsThrowsExceptionWhenEmailDoesNotMatchSeatholdCustomerEmail() {
		int seatHoldId = 10;
		String email = "my@gmail.com";
		String someoneOtherEmail = "someother@gmail.com";
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(String.format(ExceptionConstants.EMAIL_MISMATCH, someoneOtherEmail, seatHoldId));
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);
		SeatHold seatHold = new SeatHold();
		seatHold.setId(seatHoldId);
		seatHold.setUserEmail(email);
		venue.getSeatHolds().put(seatHoldId, seatHold);


		ticketService.reserveSeats(seatHoldId, someoneOtherEmail);
		
	}
	
	@Test
	public void reserveSeatsReturnReservationCodeIfSuccess() {
		int seatHoldId = 10;
		String email = "my@gmail.com";
		
		Venue venue = VenueFixture.createVenueWithLevelOneAndTwo();
		when(venueRepository.getVenue()).thenReturn(venue);
		
		int numberOfSeats = 7;
		List<Seat> seats = VenueFixture.createSeats(numberOfSeats);
		SeatHold seatHold = new SeatHold(seatHoldId, seats, email);
		venue.getSeatHolds().put(seatHoldId, seatHold);


		String code = ticketService.reserveSeats(seatHoldId, email);
		
		assertNull(venue.getSeatHold(seatHoldId));
		List<Seat> reservedSeats = venue.getReservation(code).getSeats();
		assertNotNull(reservedSeats);
		assertEquals(7, reservedSeats.size());
		assertSeatAvalabilityType(reservedSeats, SeatAvailabilityType.RESERVED);
		
	}

	private void assertSeatAvalabilityType(List<Seat> reservedSeats, SeatAvailabilityType seatAvailabilityType) {
		for (Seat seat : reservedSeats) {
			assertEquals(seatAvailabilityType, seat.getSeatAvailabilityType());
		}
	}
	

}
