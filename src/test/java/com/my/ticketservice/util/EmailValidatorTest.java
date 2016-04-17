package com.my.ticketservice.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EmailValidatorTest {
	
	private EmailValidator emailValidator;
	
	@Before
	public void setUp() {
		emailValidator = new EmailValidator();
	}
	
	
	@Test
	public void validateReturnTrueIfEmailCorrect() {
		assertTrue(emailValidator.validate("my@gmail.com"));
	}
	
	@Test
	public void validateReturnFalseIfEmailInCorrect() {
		assertFalse(emailValidator.validate("my#gmail.com"));
	}
	
	@Test
	public void validateReturnFalseIfEmailNull() {
		assertFalse(emailValidator.validate(null));
	}

}
