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

import java.io.PrintStream;
import java.util.List;

/**
 * A dummy/test row fetch delegate that just prints the row out instead of doing
 * anything with it.
 * 
 * @author Jason W. Jones
 * 
 */
public class PrintStreamRowFetchDelegate implements RowFetchDelegate {

	private PrintStream printStream;
	private int reportInterval = 100;
	private int rowsProcessed = 0;
	private boolean printRows = true;

	public PrintStreamRowFetchDelegate() {
		this(System.out);
	}

	public PrintStreamRowFetchDelegate(PrintStream printStream) {
		this.printStream = printStream;
	}

	public void startProcessingRows() throws Exception {
		printStream.println("Starting row processing from data source");
	}

	public void processRow(List<String> row) throws Exception {
		if (printRows) {
			System.out.println(row);
		}
		if (++rowsProcessed % reportInterval == 0) {
			printStream.println("Processed " + rowsProcessed + " rows...");
		}
	}

	public void doneProcessingRows() throws Exception {
		printStream.println("Finished row processing -- " + rowsProcessed + " row(s) processed");
	}

}
