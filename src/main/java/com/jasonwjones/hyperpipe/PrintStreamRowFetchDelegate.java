package com.jasonwjones.hyperpipe;

import java.io.PrintStream;
import java.util.List;

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
