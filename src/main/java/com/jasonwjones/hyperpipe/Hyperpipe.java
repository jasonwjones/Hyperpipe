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

import java.util.Properties;

import com.beust.jcommander.JCommander;

/**
 * Main runnable when running Hyperpipe on the command-line. Parses arguments
 * and kicks things off.
 * 
 * @author Jason W. Jones
 * 
 */
public class Hyperpipe {

	public static void main(String[] args) {

		HyperPipeCommand command = new HyperPipeCommand();
		JCommander commander = new JCommander(command);

		try {
			commander.parse(args);
			System.out.println("Query will be: " + command.sqlQuery);
		} catch (Exception e) {
			commander.usage();
			System.exit(1);
		}

		try {
			Class.forName(command.driverClass);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.err.println("Could not load driver class " + command.driverClass);
			System.err.println("Is driver jar file on the classpath?");
			System.exit(1);
		}

		RowFetchDelegate rowFetchDelegate = null;
		if (command.simulateEssbaseLoad) {
			rowFetchDelegate = new PrintStreamRowFetchDelegate();
		} else {
			try {
				rowFetchDelegate = new BufferingEssbaseRowFetchDelegate(command.apsServer, command.olapServer, command.essUser, command.essPass, command.application, command.database);
			} catch (Exception e) {
				System.out.println("Could not connect to Essbase data source: " + e.getMessage());
				System.exit(1);
			}
		}

		SimpleSqlFetcher fetcher = new SimpleSqlFetcher();

		Properties connection = new Properties();
		connection.put("user", command.sqlUser);
		connection.put("url", command.sqlUrl);

		try {
			fetcher.fetch(command.sqlQuery, connection, rowFetchDelegate);
		} catch (Exception e) {
			System.out.println("Error while processing data: " + e.getMessage());
		}
	}

}
