/*
 * Copyright 2013 Jason W. Jones
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jasonwjones.hyperpipe;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BufferingEssbaseRowFetchDelegateTest {

	private static String apsServer = "http://prdashhy03:13080/aps/JAPI";
	private static String olapServer = "PRDASHHY03";
	private static String username = "hypuser";
	private static String password = "hyppass";
	private static String app = "Sample";
	private static String database = "Basic";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Ignore
	public void testProcessRows() throws Exception {
		BufferingEssbaseRowFetchDelegate delegate = new BufferingEssbaseRowFetchDelegate(apsServer, olapServer, username, password, app, database);

		List<String> column = Arrays.asList("Jan", "Sales", "'100-10'", "Florida", "Actual", "234.45");
		delegate.startProcessingRows();
		for (int i = 0; i < 10; i++) {
			delegate.processRow(column);
		}
		delegate.doneProcessingRows();
	}

}
