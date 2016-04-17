package com.my.ticketservice.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class represents a SeatHold. Object that temporary holds seats, id: seat hold
 * id, seats: held seats, expirationDate: date and time when seat hold expires,
 * userEmail: email of the user that created the seat hold
 *
 */
public class SeatHold {
	private int id;

	private List<Seat> seats;

	private LocalDateTime expirationDate;

	private String userEmail;

	public SeatHold() {

	}

	public SeatHold(int id, List<Seat> seats, String userEmail) {
		super();
		this.id = id;
		this.seats = seats;
		this.userEmail = userEmail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
