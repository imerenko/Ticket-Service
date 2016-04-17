package com.my.ticketservice.util;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class IdGeneratorTest {

	Set<Integer> ids = new HashSet<>();

	@Test
	public void getNextId() throws InterruptedException {

		Runnable task = () -> {
			ids.add(IdGenerator.getNextId());
		};

		int numberOfThreds = 100;
		for (int i = 0; i < numberOfThreds; i++) {
			Thread thread = new Thread(task);
			thread.start();
			thread.join();
		}

		assertEquals(numberOfThreds, ids.size());

	}
	
	@Test
	public void resetSetCounterToZero() {
		IdGenerator.getNextId();
		IdGenerator.getNextId();
		
		IdGenerator.reset();
		
		assertEquals(1, IdGenerator.getNextId());
		
	}

}
