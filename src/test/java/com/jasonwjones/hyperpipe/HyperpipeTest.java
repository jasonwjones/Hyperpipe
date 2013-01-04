/* Copyright 2013 Jason W. Jones
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
 * the License. */

package com.jasonwjones.hyperpipe;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HyperpipeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Ignore
	public void testMain() {
		String[] args = { "-sqlDriver", "org.h2.Driver", "-sqlUrl", "jdbc:h2:mem:test_mem",
				"-simulateEssbase", "-sqlQuery", "\"SELECT 1\"" };
		Hyperpipe.main(args);
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testEssbase() {
		String[] args = { "-sqlDriver", "org.h2.Driver", "-sqlUrl",
				"jdbc:h2:tcp://localhost/~/s4me", "-sqlUser", "sa", "-sqlQuery",
				"\"SELECT * FROM TESTESS\"", "-apsServer", "http://prdashhy03:13080/aps/JAPI",
				"-olapServer", "prdashhy03", "-application", "Sample", "-database", "Basic",
				"-essUser", "hypuser", "-essPass", "hyppass" };
		Hyperpipe.main(args);
	}
}
