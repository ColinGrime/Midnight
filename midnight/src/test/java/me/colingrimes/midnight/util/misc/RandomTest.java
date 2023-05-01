package me.colingrimes.midnight.util.misc;

import me.colingrimes.midnight.util.misc.Random;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomTest {

	@Test
	void testNumberWithSingleParameter() {
		int num = 10;
		Set<Integer> generatedNumbers = new HashSet<>();

		for (int i=0; i<1000; i++) {
			int randomNumber = Random.number(num);
			assertTrue(randomNumber >= 0 && randomNumber < num, "Random number should be between 0 (inclusive) and num (exclusive)");
			generatedNumbers.add(randomNumber);
		}

		assertTrue(generatedNumbers.size() > 1, "Generated numbers should have more than one unique value");
	}

	@Test
	void testNumberWithTwoParameters() {
		int lowNum = 5;
		int highNum = 20;
		Set<Integer> generatedNumbers = new HashSet<>();

		for (int i=0; i<1000; i++) {
			int randomNumber = Random.number(lowNum, highNum);
			assertTrue(randomNumber >= lowNum && randomNumber <= highNum, "Random number should be between lowNum (inclusive) and highNum (inclusive)");
			generatedNumbers.add(randomNumber);
		}

		assertTrue(generatedNumbers.size() > 1, "Generated numbers should have more than one unique value");
	}

	@Test
	void testDecimal() {
		double lowNum = 1.0;
		double highNum = 10.0;
		Set<Double> generatedNumbers = new HashSet<>();

		for (int i=0; i<1000; i++) {
			double randomNumber = Random.decimal(lowNum, highNum);
			assertTrue(randomNumber >= lowNum && randomNumber < highNum, "Random number should be between lowNum (inclusive) and highNum (exclusive)");
			generatedNumbers.add(randomNumber);
		}

		assertTrue(generatedNumbers.size() > 1, "Generated numbers should have more than one unique value");
	}

	@Test
	void testChanceInt() {
		int num = 50;

		int trueCount = 0;
		int falseCount = 0;

		for (int i=0; i<1000; i++) {
			if (Random.chance(num)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}

		assertTrue(trueCount > 0, "True count should be greater than 0");
		assertTrue(falseCount > 0, "False count should be greater than 0");
	}

	@Test
	void testChanceDouble() {
		double num = 50.0;

		int trueCount = 0;
		int falseCount = 0;

		for (int i=0; i<1000; i++) {
			if (Random.chance(num)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}

		assertTrue(trueCount > 0, "True count should be greater than 0");
		assertTrue(falseCount > 0, "False count should be greater than 0");
	}
}
