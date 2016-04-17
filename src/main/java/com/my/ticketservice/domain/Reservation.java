package com.my.ticketservice.domain;

import java.util.List;

/**
 * Class represents Reservation. Contains code: reservation code, email:
 * customer email and seats: reserved seats by customer
 *
 */
public class Reservation {

	public String code;

	public String email;

	public List<Seat> seats;

	public Reservation(String code, List<Seat> seats, String email) {
		super();
		this.code = code;
		this.email = email;
		this.seats = seats;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
