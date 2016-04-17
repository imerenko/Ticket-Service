package com.my.ticketservice.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class represents a venue. It contains all levels(with seats and all necessary
 * information), seat holds and reservations.
 *
 */
public class Venue {
	private Map<Integer, Level> levels = new HashMap<>();

	private Map<Integer, SeatHold> seatHolds = new HashMap<>();

	private Map<String, Reservation> reservations = new HashMap<>();

	private Map<Integer, Level> getLevels() {
		return this.levels;
	}

	public Level getLevel(int levelId) {
		return levels.get(levelId);
	}

	public void putLevel(int levelId, Level level) {
		levels.put(levelId, level);
	}

	public void setLevels(Map<Integer, Level> levels) {
		this.levels = levels;
	}

	public Map<Integer, SeatHold> getSeatHolds() {
		return seatHolds;
	}

	public SeatHold getSeatHold(int seatHoldId) {
		return seatHolds.get(seatHoldId);
	}

	public void putSeatHold(Integer seatHoldId, SeatHold seatHold) {
		this.seatHolds.put(seatHoldId, seatHold);
	}

	public void removeSeatHold(Integer seatHoldId) {
		this.seatHolds.remove(seatHoldId);
	}

	public Reservation getReservation(String code) {
		return reservations.get(code);
	}

	public void putReservation(String code, Reservation reservation) {
		this.reservations.put(code, reservation);
	}

	/**
	 * @return sorted list of levels. The lower index the better level
	 */
	public List<Level> getLevelsByPriority() {
		List<Level> levels = new ArrayList<>();
		Map<Integer, Level> levelMap = getLevels();
		List<Integer> keys = new ArrayList<Integer>(levelMap.keySet());
		Collections.sort(keys);
		for (Integer pos : keys) {
			levels.add(levelMap.get(pos));
		}
		return levels;

	}

	/**
	 * @param minLevel
	 *            the minimum level
	 * @param maxLevel
	 *            the maximum level
	 * @return sorted list of levels limited by mimLevel and maxLevel. The lower
	 *         index the better level
	 */
	public List<Level> getLevelsByPriority(int minLevel, int maxLevel) {
		List<Level> levels = new ArrayList<>();
		for (int i = minLevel; i <= maxLevel; i++) {
			Level level = getLevel(i);
			levels.add(level);
		}
		return levels;
	}

}
