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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Simple JDBC wrapper object that can issue a query to a given connection.
 * 
 * @author Jason W. Jones
 * 
 */
public class SimpleSqlFetcher {

	public void fetch(String query, Properties connection, RowFetchDelegate delegate) throws Exception {
		String url = connection.getProperty("url");

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
