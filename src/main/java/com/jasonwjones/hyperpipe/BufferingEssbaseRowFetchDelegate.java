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
	private StringBuilder builder = new StringBuilder();
	private int bufferSize = 200;

	/**
	 * TODO: disconnect from server in close() method
	 * 
	 * @param apsServer
	 * @param olapServer
	 * @param username
	 * @param password
	 * @param app
	 * @param db
	 * @throws Exception
	 */
	public BufferingEssbaseRowFetchDelegate(String apsServer, String olapServer, String username,
			String password, String app, String db) throws Exception {
		IEssbase ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
		IEssOlapServer olapSvr = ess.signOn(username, password, false, null, apsServer, olapServer);
		cube = olapSvr.getApplication(app).getCube(db);
	}

	public void processRow(List<String> row) throws Exception {
		String rowSpec = flattenColumn(row);
		addToBuffer(rowSpec);
	}

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
		System.out.println("Added to buffer, length now: " + builder.length());
	}

	private void flushBuffer() throws Exception {
		System.out.println("Send update data...");
		cube.sendString(builder.toString());
		builder.setLength(0);
	}

	private String flattenColumn(List<String> column) {
		String members = collectionToDelimitedString(column.subList(0, column.size() - 1), " ");
		Object fact = column.get(column.size() - 1);
		return members + " " + fact;
		// return collectionToDelimitedString(column, " ");
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

	public void doneProcessingRows() throws Exception {
		System.out.println("End update -- force flush");
		flushBuffer();
		cube.endUpdate();
	}

	public void startProcessingRows() throws Exception {
		System.out.println("Beginning update...");
		cube.beginUpdate(true, false);
	}

}
