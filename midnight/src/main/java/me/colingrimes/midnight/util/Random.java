package me.colingrimes.midnight.util;

public final class Random {

	private final static java.util.Random random = new java.util.Random();

	/**
	 * Gets a random number from 0 to a number (exclusive).
	 * @param num any integer
	 * @return random number
	 */
	public static int number(int num) {
		return random.nextInt(num);
	}

	/**
	 * Gets a random number between two integers.
	 * @param lowNum low integer
	 * @param highNum high integer
	 * @return random number
	 */
	public static int number(int lowNum, int highNum) {
		return random.nextInt((highNum - lowNum) + 1) + lowNum;
	}

	/**
	 * Gets a random number between two doubles.
	 * @param lowNum low double
	 * @param highNum high double
	 * @return random number
	 */
	public static double decimal(double lowNum, double highNum) {
		return (random.nextDouble() * (highNum - lowNum)) + lowNum;
	}

	/**
	 * Returns true if the random number is higher than a random number 0-100.
	 * @param num any integer
	 * @return true if num is higher than a random number 0-100
	 */
	public static boolean chance(int num) {
		return num >= number(0, 100);
	}

	/**
	 * Returns true if the random number is higher than a random number 0-100.
	 * @param num any double
	 * @return true if num is higher than a random number 0-100
	 */
	public static boolean chance(double num) {
		return num >= decimal(0.0, 100.0);
	}

	private Random() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}