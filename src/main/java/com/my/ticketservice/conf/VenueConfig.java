package com.my.ticketservice.conf;

import java.math.BigDecimal;

/**
 * Configuration Class that has application configuration properties I use class
 * instead of .property file for simplicity
 *
 */
public class VenueConfig {

	public static Object[][] getLevelInformation() {
		Object[][] levelInformation = { { 1, "Orchestra", 25, 50, new BigDecimal(100) },
				{ 2, "Main", 20, 100, new BigDecimal(75) }, { 3, "Balcony 1", 15, 100, new BigDecimal(50) },
				{ 4, "Balcony 2", 15, 100, new BigDecimal(40) } };
		return levelInformation;
	}

	public static int numOfSecondsExpirationTime() {
		return 60;
	}

}
