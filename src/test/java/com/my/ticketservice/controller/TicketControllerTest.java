package com.my.ticketservice.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.service.TicketService;
import com.my.ticketservice.util.IdGenerator;

public class TicketControllerTest {

	@InjectMocks
	private TicketController ticketController;

	@Mock
	private TicketService ticketService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void numSeatsAvailableCallWithoutLevelCallService() {
		Optional<Integer> noLevel = Optional.ofNullable(null);
		int num = 200;
		when(ticketService.numSeatsAvailable(noLevel)).thenReturn(num);

		String actualNumMessage = ticketController.numSeatsAvailable(null);

		String expectedNumMessage = "{ \"numSeatsAvailable\": \"" + num + "\" }";

		assertEquals(expectedNumMessage, actualNumMessage);
	}

	@Test
	public void numSeatsAvailableCallWithLevelCallService() {
		int levelInt = 5;
		int num = 200;
		Optional<Integer> level = Optional.of(levelInt);

		when(ticketService.numSeatsAvailable(level)).thenReturn(num);

		String actualNumMessage = ticketController.numSeatsAvailable(levelInt);

		String expectedNumMessage = "{ \"numSeatsAvailable\": \"" + num + "\" }";

		assertEquals(expectedNumMessage, actualNumMessage);
	}

	
	@Test
	public void findAndHoldSeatsWithoutLevelsCallService() {
		TicketController.FindAndHoldSeatsBody body = new TicketController.FindAndHoldSeatsBody();
		body.numSeats = 3;
		body.minLevel = null;
		body.maxLevel = null;
		body.customerEmail = "customer@gmail.com";
		Optional<Integer> noLevel = Optional.ofNullable(null);
		
		SeatHold expectedSeatHold = new SeatHold();
		when(ticketService.findAndHoldSeats(body.numSeats, noLevel, noLevel, body.customerEmail)).thenReturn(expectedSeatHold);
		
		SeatHold actualSeatHold = ticketController.findAndHoldSeats(body);
		
		assertEquals(expectedSeatHold, actualSeatHold);
		
	}
	
	@Test
	public void findAndHoldSeatsWithLevelsCallService() {
		TicketController.FindAndHoldSeatsBody body = new TicketController.FindAndHoldSeatsBody();
		body.numSeats = 3;
		body.minLevel = 1;
		body.maxLevel = 2;
		body.customerEmail = "customer@gmail.com";
		Optional<Integer> minLevel = Optional.of(body.minLevel);
		Optional<Integer> maxLevel = Optional.of(body.maxLevel);
		
		SeatHold expectedSeatHold = new SeatHold();
		when(ticketService.findAndHoldSeats(body.numSeats, minLevel, maxLevel, body.customerEmail)).thenReturn(expectedSeatHold);
		
		SeatHold actualSeatHold = ticketController.findAndHoldSeats(body);
		
		assertEquals(expectedSeatHold, actualSeatHold);
	}
	
	@Test
	public void reserveSeatsCallService() {
		TicketController.ReserveSeatsBody body = new TicketController.ReserveSeatsBody();
		body.seatHoldId = 10;
		body.customerEmail = "customer@gmail.com";
		String code = "reservationCode";
		when(ticketService.reserveSeats(body.seatHoldId, body.customerEmail)).thenReturn(code);
		
		String expectedMessage = "{ \"reservationCode\": \"" + code + "\" }";
		
		String actualMessage = ticketController.reserveSeats(body);
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	public void recreateData() {
		IdGenerator.getNextId();
		
		ticketController.recreateData();
		
		verify(ticketService).recreateVenue();
		assertEquals(1, IdGenerator.getNextId());
	}
	
}
