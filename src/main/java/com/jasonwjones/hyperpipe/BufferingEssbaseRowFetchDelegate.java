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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.session.IEssbase;

/**
 * Receives rows as part of the RowFetchDelegate implementation, converts them
 * to raw Essbase send-data Strings, and when the threshold/buffer is hit, kicks
 * it out to the server. (Essbase docs suggest that the limit is 32K of data or
 * so).
 * 
 * @author Jason W. Jones
 * 
 */
public class BufferingEssbaseRowFetchDelegate implements RowFetchDelegate {

	private IEssCube cube;
	private IEssOlapServer olapSvr;

	/**
	 * Contains the data that is constantly being buffered before being cleared
	 * out and sent up.
	 */
	private StringBuilder builder = new StringBuilder();

	/**
	 * Per the Essbase Java API spec, the update strings are limited to 32K or
	 * so, so we'll cut it at 30 and send the update
	 */
	private int bufferSize = 30000;

	/**
	 * This constructor takes all of the connection parameters at once and
	 * builds the BufferingEssbaseRowDelegate.
	 * 
	 * @param apsServer URL to the Essbase APS server
	 * @param olapServer name of the Essbase OLAP server (frequently the same as
	 *            the apsServer)
	 * @param username username to connect to Essbase with
	 * @param password password for the user
	 * @param app Essbase application to use
	 * @param db Essbase database (within application) to use
	 * @throws Exception wraps an EssException, if anything happens
	 */
	public BufferingEssbaseRowFetchDelegate(String apsServer, String olapServer, String username,
			String password, String app, String db) throws Exception {

		IEssbase ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
		olapSvr = ess.signOn(username, password, false, null, apsServer, olapServer);
		cube = olapSvr.getApplication(app).getCube(db);
	}

	/**
	 * Called for each row that comes in from SQL
	 */
	public void processRow(List<String> row) throws Exception {
		String rowSpec = flattenColumn(row);
		addToBuffer(rowSpec);
	}

	/**
	 * Adds an update string to the current buffer and will automatically flush
	 * if necessary (i.e., the buffer is greater than the max buffer size)
	 * 
	 * @param text text to add to the buffer
	 */
	private void addToBuffer(String text) {

		if (builder.length() + text.length() > bufferSize) {
			try {
				flushBuffer();
			} catch (Exception e) {
				System.out.println("Error flushing buffer: " + e.getMessage());
			}
		}

		builder.append(text);
		builder.append("\n");
	}

	private void flushBuffer() throws Exception {
		System.out.println("Send update data...");
		cube.sendString(builder.toString());
		builder.setLength(0);
	}

	/**
	 * Given a SQL column that is represented as list of string values, wrap all
	 * of them in quotes except the last value which is presumably the fact
	 * value and therefore cannot and should not have quotes around it.
	 * 
	 * @param column all of the columns in a SQL ro
	 * @return a text representation of the column suitable for an Essbase
	 *         update string
	 */
	private String flattenColumn(List<String> column) {
		String members = collectionToDelimitedString(column.subList(0, column.size() - 1), " ");
		Object fact = column.get(column.size() - 1);
		return members + " " + fact;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	private static String collectionToDelimitedString(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "\"", "\"");
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @param prefix the String to start each element with
	 * @param suffix the String to end each element with
	 * @return the delimited String
	 */
	private static String collectionToDelimitedString(Collection<?> coll, String delim,
			String prefix, String suffix) {
		if (coll.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Called before any rows are processed so perform any necessary steps
	 * before streaming in updates.
	 */
	public void startProcessingRows() throws Exception {
		System.out.println("Beginning update...");
		cube.beginUpdate(true, false);
	}

	/**
	 * Called when processing has completed, so perform cleanup tasks if needed.
	 */
	public void doneProcessingRows() throws Exception {
		System.out.println("End update -- force flush");
		flushBuffer();
		cube.endUpdate();
		olapSvr.disconnect();
	}

}
