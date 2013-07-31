package uk.ac.glasgow.etparser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import uk.ac.glasgow.etparser.handlers.Heap;

public class Main {

	public static void main(final String[] args) {

		long startOfProcess = System.currentTimeMillis();
		try {
			InputStream fileStream = new FileInputStream(args[0]);
//			InputStream gzipStream = new GZIPInputStream(fileStream);
			ETParser etparser = new ETParser(fileStream,new Heap());
			etparser.processFile();
			etparser.printReport();
			fileStream.close();
			long endOfProcess = System.currentTimeMillis();
			long timeTakenInMillisecs = endOfProcess - startOfProcess;
			long timeTakenInSeconds = timeTakenInMillisecs / 1000;
			long linesPerSecond = etparser.getLines() / timeTakenInSeconds;

			System.out.println("The program reads " + linesPerSecond
					+ " lines per second");
		}

		catch (IOException io) {
			System.out.println("IOException" + io);
			System.exit(0);
		}

	}

}