package com.my.ticketservice.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Class represents a Level
 *
 */
public class Level {

	private LevelInfo levelInfo;

	private List<Seat> seats;

	public Level() {

	}

	public Level(int id, String name, int numverOfRows, int numberOfSeatsInARow, BigDecimal price) {
		levelInfo = new LevelInfo(id, name, numverOfRows, numberOfSeatsInARow, price);
	}

	public LevelInfo getLevelInfo() {
		return levelInfo;
	}

	public void setLevelInfo(LevelInfo levelInfo) {
		this.levelInfo = levelInfo;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
