package com.my.ticketservice.controller;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.ticketservice.domain.SeatHold;
import com.my.ticketservice.service.TicketService;
import com.my.ticketservice.util.IdGenerator;

/**
 * Rest controller.
 * Basically it is a wrapper around ticket service for creating rest points.
 *
 */
@RestController
@RequestMapping(value = "/rest/v1")
public class TicketController {

	@Resource
	private TicketService ticketService;

	@RequestMapping(method = RequestMethod.GET, value = "/seats", produces = MediaType.APPLICATION_JSON_VALUE)
	public String numSeatsAvailable(@RequestParam(value = "level", required = false) Integer venueLevelInt) {
		Optional<Integer> venueLevel = Optional.ofNullable(venueLevelInt);
		int num = ticketService.numSeatsAvailable(venueLevel);
		return "{ \"numSeatsAvailable\": \"" + num + "\" }";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/holds", produces = MediaType.APPLICATION_JSON_VALUE)
	public SeatHold findAndHoldSeats(@RequestBody FindAndHoldSeatsBody body) {
		return ticketService.findAndHoldSeats(body.numSeats, Optional.ofNullable(body.minLevel), Optional.ofNullable(body.maxLevel),
				body.customerEmail);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
	public String reserveSeats(@RequestBody ReserveSeatsBody body) {
		String code = ticketService.reserveSeats(body.seatHoldId, body.customerEmail);
		return "{ \"reservationCode\": \"" + code + "\" }";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/recreatedata")
	public void recreateData() {
		ticketService.recreateVenue();
		IdGenerator.reset();
	}


	static class FindAndHoldSeatsBody {
		public int numSeats;
		public Integer minLevel;
		public Integer maxLevel;
		public String customerEmail;
	}

	static class ReserveSeatsBody {
		public int seatHoldId;
		public String customerEmail;
	}

}
