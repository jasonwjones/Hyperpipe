package com.jasonwjones.hyperpipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleBasicRandomData {

	private String[] months = { "Jan", "Feb", "Mar" };
	private String[] measures = { "Sales" };
	private String[] products = { "100-10" };
	private String[] locations = { "Florida", "California", "Washington" };
	private String[] scenarios = { "Actual", "Budget" };
	private String[][] sources = { months, measures, products, locations, scenarios };

	private static Random random = new Random();

	public List<String> randomFact() {
		List<String> fact = new ArrayList<String>();
		for (String[] source : sources) {
			fact.add(randomString(source));
		}
		fact.add(randomAmount());
		return fact;
	}

	private static String randomAmount() {
		double amount = random.nextDouble() * 10000.0;
		return Double.toString(amount);
	}

	private static String randomString(String[] array) {
		return array[random.nextInt(array.length)];
	}

}
