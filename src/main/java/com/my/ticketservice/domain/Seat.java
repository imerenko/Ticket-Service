package com.my.ticketservice.domain;

/**
 * Class represents a Seat. Besides other properties it has Level info object
 * that contains level data (without seat information).
 *
 */
public class Seat {
	private int id;

	private LevelInfo levelInfo;

	private int rowNumber;

	private int seatInARowNumber;

	private SeatAvailabilityType seatAvailabilityType;

	public Seat() {
		this.seatAvailabilityType = SeatAvailabilityType.FREE;
	}

	public Seat(int id, int rowNumber, int seatInARowNumber, LevelInfo levelInfo) {
		super();
		this.id = id;
		this.levelInfo = levelInfo;
		this.rowNumber = rowNumber;
		this.seatInARowNumber = seatInARowNumber;
		this.seatAvailabilityType = SeatAvailabilityType.FREE;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LevelInfo getLevelInfo() {
		return this.levelInfo;
	}

	public void setLevelInfo(LevelInfo levelInfo) {
		this.levelInfo = levelInfo;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getSeatInARowNumber() {
		return seatInARowNumber;
	}

	public void setSeatInARowNumber(int seatInARowNumber) {
		this.seatInARowNumber = seatInARowNumber;
	}

	public SeatAvailabilityType getSeatAvailabilityType() {
		return seatAvailabilityType;
	}

	public void setSeatAvailabilityType(SeatAvailabilityType seatAvailabilityType) {
		this.seatAvailabilityType = seatAvailabilityType;
	}

}
