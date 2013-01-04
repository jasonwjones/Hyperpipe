/**
 * Copyright 2013 Jason W. Jones
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jasonwjones.hyperpipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class for faking data from Sample Basic to help with testing.
 * 
 * @author Jason W. Jones
 * 
 */
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
