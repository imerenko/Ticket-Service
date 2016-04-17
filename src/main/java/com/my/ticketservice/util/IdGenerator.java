package com.my.ticketservice.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates unique integer id. AtomicInteger is used to solve a concurrency
 * problem. Constructor is private because no sense to create and instance of
 * this class.
 *
 */
public class IdGenerator {

	private IdGenerator() {

	}

	private static AtomicInteger counter = new AtomicInteger(0);

	public static int getNextId() {
		return counter.incrementAndGet();
	}

	public static void reset() {
		counter = new AtomicInteger(0);
	}

}
