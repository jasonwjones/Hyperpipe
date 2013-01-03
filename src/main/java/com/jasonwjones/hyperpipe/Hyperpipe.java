package com.jasonwjones.hyperpipe;

import java.util.Properties;

import com.beust.jcommander.JCommander;

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
		connection.put("driver", command.driverClass);

		try {
			fetcher.fetch(command.sqlQuery, connection, rowFetchDelegate);
		} catch (Exception e) {
			System.out.println("Exception processing: " + e.getMessage());
		}
	}

}
