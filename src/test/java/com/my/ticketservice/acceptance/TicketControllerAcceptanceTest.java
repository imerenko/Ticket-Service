package com.my.ticketservice.acceptance;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;
import com.my.ticketservice.Application;
import com.my.ticketservice.dao.VenueRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TicketControllerAcceptanceTest {

	private int port = 8080;

	@Resource
	private VenueRepository venueRepository;

	@Before
	public void setUp() {
		get("rest/v1/recreatedata");
		RestAssured.port = port;
	}

	@Test
	public void testTicketService() {
		when().get("rest/v1/seats").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable", equalTo("6250"));
		when().get("rest/v1/seats?level=1").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("1250"));
		when().get("rest/v1/seats?level=2").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("2000"));

		given().contentType("application/json")
				.body("{\"numSeats\" : \"10\", \"minLevel\" : \"2\",\"maxLevel\" : \"2\",\"customerEmail\" : \"my@gmail.com\"}")
				.expect().statusCode(HttpStatus.SC_OK).body("id", equalTo(1)).when().post("rest/v1/holds");

		when().get("rest/v1/seats").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable", equalTo("6240"));
		when().get("rest/v1/seats?level=1").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("1250"));
		when().get("rest/v1/seats?level=2").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("1990"));

		given().contentType("application/json")
				.body("{\"seatHoldId\" : \"1\",\"customerEmail\" : \"my@gmail.com\"}")
				.expect().statusCode(HttpStatus.SC_OK).when().post("rest/v1/reservations");

		when().get("rest/v1/seats").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable", equalTo("6240"));
		when().get("rest/v1/seats?level=1").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("1250"));
		when().get("rest/v1/seats?level=2").then().statusCode(HttpStatus.SC_OK).body("numSeatsAvailable",
				equalTo("1990"));
		
		given().contentType("application/json")
		.body("{\"seatHoldId\" : \"1\",\"customerEmail\" : \"my@gmail.com\"}")
		.expect().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("rest/v1/reservations");
		
		when().get("rest/v1/seats?level=10").then().statusCode(HttpStatus.SC_BAD_REQUEST);

	}

	@After
	public void tearDown() {
		get("rest/v1/recreatedata");
	}

}
