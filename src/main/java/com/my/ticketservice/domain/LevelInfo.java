package com.my.ticketservice.domain;

import java.math.BigDecimal;

/**
 * Class contains Level information like name, price etc
 *
 */
public class LevelInfo {
	private int id;

	private String name;

	private int numverOfRows;

	private int numberOfSeatsInARow;

	private BigDecimal price;

	public LevelInfo(int id, String name, int numverOfRows, int numberOfSeatsInARow, BigDecimal price) {
		super();
		this.id = id;
		this.name = name;
		this.numverOfRows = numverOfRows;
		this.numberOfSeatsInARow = numberOfSeatsInARow;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumverOfRows() {
		return numverOfRows;
	}

	public void setNumverOfRows(int numverOfRows) {
		this.numverOfRows = numverOfRows;
	}

	public int getNumberOfSeatsInARow() {
		return numberOfSeatsInARow;
	}

	public void setNumberOfSeatsInARow(int numberOfSeatsInARow) {
		this.numberOfSeatsInARow = numberOfSeatsInARow;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
