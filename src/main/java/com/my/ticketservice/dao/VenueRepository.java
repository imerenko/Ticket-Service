package com.my.ticketservice.dao;

import com.my.ticketservice.domain.Venue;

/**
 * Data Access Object that contains a reference to Venue Object
 *
 */
public interface VenueRepository {

	/**
	 * @return Venue object
	 */
	Venue getVenue();

	/**
	 * recreate Venue from Config
	 */
	void recreateVenue();

}
