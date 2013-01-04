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

import java.util.List;

/**
 * An object implementing this protocol is notified of events that result from a
 * SQL query.
 * 
 * @author Jason W. Jones
 * 
 */
public interface RowFetchDelegate {

	/**
	 * This method is called when rows start coming back from the SQL
	 * datasource.
	 * 
	 * @throws Exception wraps any sort of SQL or network error.
	 */
	void startProcessingRows() throws Exception;

	/**
	 * Called for every row of data is comes back from the SQL data source.
	 * 
	 * @param row a List of Strings based on the SQL query that was issued. Each
	 *            element of the list represents a column from the result set.
	 * @throws Exception wraps any possible SQL error.
	 */
	void processRow(List<String> row) throws Exception;

	/**
	 * Called after all rows have been processed. May be useful to clean up or
	 * close any files/connections.
	 * 
	 * @throws Exception wraps any sort of other exception such as from a SQL
	 *             connection or input stream.
	 */
	void doneProcessingRows() throws Exception;

}
