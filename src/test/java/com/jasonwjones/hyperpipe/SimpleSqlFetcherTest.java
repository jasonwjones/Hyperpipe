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

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleSqlFetcherTest {

	private static String connectionUrl = "jdbc:h2:mem:test_mem";
	private int fetchedRows = 0;

	@Before
	@Ignore
	public void setUp() throws Exception {
		executeSql("CREATE TABLE TEST(ID INT PRIMARY KEY, PERIOD INT, ACCOUNT VARCHAR(255) DEFAULT '');");
		executeSql("INSERT INTO TEST VALUES (1, 1, 'Sales');");
	}

	@Test
	@Ignore
	public void testFetch() throws Exception {
		SimpleSqlFetcher fetcher = new SimpleSqlFetcher();

		fetchedRows = 0;

		fetcher.fetch("select * from TEST", null, new RowFetchDelegate() {

			public void processRow(List<String> rows) {
				System.out.println("Row " + ++fetchedRows + ": " + rows);
			}

			public void doneProcessingRows() throws Exception {
			}

			public void startProcessingRows() throws Exception {
			}
		});

		assertEquals(1, fetchedRows);
	}

	private static void executeSql(String query) throws Exception {
		Connection conn = DriverManager.getConnection(connectionUrl);
		Statement statement = conn.createStatement();
		statement.execute(query);
	}

}
