package com.my.ticketservice.exception;

/**
 * Class contains exception messages. It can contain exceptions codes that will
 * be translatable. Constructor is private because no sense to create and instance of
 * this class.
 *
 */
public class ExceptionConstants {

	private ExceptionConstants() {

	}

	public static final String LEVEL_DOES_NOT_EXIST = "Level %s does not exist.";
	public static final String NUMBER_OF_SEATS_MUST_BE_MORE_THEN_ZERO = "umber of seats must be more then zero.";
	public static final String MIN_AND_MAX_LEVEL_BOTH_SHOULD_HAVE_VALUE_OR_NULL = "Both min and max levels should have value or both be empty.";
	public static final String MIN_CANNOT_BE_MORE_THAN_MAX_LEVEL = "Min level number cannot be more then max level number.";
	public static final String INCORRECT_EMAIL = "Incorrect E-mail.";
	public static final String NOT_ENOUGH_SEATS = "Sorry. Not enough seats.";
	public static final String NO_SEATHOLD_WITH_ID = "Sorry. There is no seathold with id = %s";
	public static final String EMAIL_MISMATCH = "Sorry. Email %s does not correspond to the seathold's email with id: %s";

}
