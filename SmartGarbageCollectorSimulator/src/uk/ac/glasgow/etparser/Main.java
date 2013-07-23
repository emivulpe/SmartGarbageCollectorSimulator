package uk.ac.glasgow.etparser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public class Main {

	public static void main(final String[] args) {

//		ErrorLogger logger = new ErrorLogger();
		long startOfProcess = System.currentTimeMillis();
		try {
			InputStream fileStream = new FileInputStream(args[0]);
			//InputStream gzipStream = new GZIPInputStream(fileStream);
			ETParser parser = new ETParser(fileStream);
			parser.processFile();
			parser.printReport();
			fileStream.close();
			long endOfProcess = System.currentTimeMillis();
			long timeTakenInMillisecs = endOfProcess - startOfProcess;
			long timeTakenInSeconds = timeTakenInMillisecs / 1000;
			long linesPerSecond = parser.getLines() / timeTakenInSeconds;

			System.out.println("The program reads " + linesPerSecond
					+ " lines per second");
		}

		catch (IOException io) {
			System.out.println("IOException" + io);
			System.exit(0);
		}

	}

}