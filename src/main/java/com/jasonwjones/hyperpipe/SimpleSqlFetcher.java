package com.jasonwjones.hyperpipe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleSqlFetcher {

	/**
	 * 
	 * @param query
	 * @param connection
	 * @param delegate
	 * @throws Exception
	 */
	public void fetch(String query, Properties connection, RowFetchDelegate delegate) throws Exception {
		String url = connection.getProperty("url");
		String driver = connection.getProperty("driver");

		Class.forName(driver);

		Connection conn = DriverManager.getConnection(url, connection);
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		delegate.startProcessingRows();

		while (resultSet.next()) {
			List<String> stringColumn = resultSetRowToStringList(resultSet);
			delegate.processRow(stringColumn);
		}

		delegate.doneProcessingRows();
	}

	private static List<String> resultSetRowToStringList(ResultSet resultSet) throws SQLException {
		List<String> columns = new ArrayList<String>();

		ResultSetMetaData md = resultSet.getMetaData();
		for (int columnIndex = 1; columnIndex <= md.getColumnCount(); columnIndex++) {
			String columnStringValue = resultSet.getString(columnIndex);
			columns.add(columnStringValue);
		}

		return columns;
	}

}
