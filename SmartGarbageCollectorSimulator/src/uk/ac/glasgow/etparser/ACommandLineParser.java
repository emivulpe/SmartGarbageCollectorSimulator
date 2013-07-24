package uk.ac.glasgow.etparser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class ACommandLineParser {

	private static boolean interactive = false;
	private static boolean chart = false;
	private static boolean help = false;
	private static LiveSizeChart lchart;
	
	//default would be to ignore these errors. if unborn or dead in args specify...
	private static WayToDeal preaccess=WayToDeal.IGNORE;
	private static WayToDeal postaccess=WayToDeal.IGNORE;
	
	private static String inputFile;
	
	public enum WayToDeal {IGNORE,MOVE	};
	
	
	public ACommandLineParser(){
		
	}
	
	
	public static void main(String args[]) {
		
		Options options = new Options();
		
		// First parameter is the option name
		// Second parameter is whether this is a switch or argument
		// Third is a description
		options.addOption("i", false, "run the program interactively");
		options.addOption("ch", false, "display a livechart");
		options.addOption("r", true, "number of times to repeat the run");
		options.addOption("f",true,"input file name");
		options.addOption("unborn",true,"how to deal with unborn accesses: ignore or move");
		options.addOption("dead",true,"how to deal with dead accesses");
		options.addOption("h",false,"ask for help");
		
		// Parse the arguments
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse( options, args);
		} catch (ParseException e) {
			System.err.println("Error parsing commandline options.");
			System.err.println(e);
			e.printStackTrace();
			System.exit(-1);
		}
		
		int reps = 1;
		if (cmd.hasOption("r")) {
			reps = Integer.parseInt(cmd.getOptionValue("r"));
		}
		
		if(cmd.hasOption("unborn")){
			preaccess=enumConverter(cmd.getOptionValue("unborn"));	
		}
		
		if(cmd.hasOption("dead")){
			postaccess=enumConverter(cmd.getOptionValue("dead"));
		}
		
		if(cmd.hasOption("f"))
			inputFile=cmd.getOptionValue("f");
		
		// Now we can interrogate them
		interactive = cmd.hasOption("i");
		chart = cmd.hasOption("ch");
		help=cmd.hasOption("h");
//		System.out.println(chart+"chaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaart");
		// Want at least time or date
		if (help) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "CommandlineParser", options );
			System.exit(0);
			
		}
		if (!cmd.hasOption("f")){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "CommandlineParser", options );
			System.out.println("Please enter a filename to process");
			
		}
		
		
		
		for (int i=0; i<reps;i++) {
			if (interactive){
			//	System.out.println(tf.format(new Date()));
				displayChoices();
				askForPreaccess();
				askForPostaccess();
				}
//				
//			if (chart)
//				lchart=new LiveSizeChart();
//				if(lchart!=null) lchart.setVisible(true);
		}
		
		

//		ErrorLogger logger = new ErrorLogger();
		long startOfProcess = System.currentTimeMillis();
		try {
			InputStream fileStream = new FileInputStream(inputFile);
			//InputStream gzipStream = new GZIPInputStream(fileStream);
			System.out.println(chart+"chaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaart");
			ETParser etparser = new ETParser(fileStream,preaccess,postaccess,chart);
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
			
			//	System.out.println(df.format(new Date()));
		
		
	

	private static void displayChoices() {

		System.out
				.println("Hello, dear user! Before you start the smart garbage collector simulator"
						+ " you must choose how to deal with pre-access and post-access errors.");
		System.out.println();
	}

	private static void askForPreaccess() {
		System.out.println("First choose dealing with pre-access errors:");
		System.out.println();
		System.out.println("Enter 'IGNORE' to ignore them.");
		System.out
				.println("Enter 'MOVE' to allocate them at their first access.");
		System.out.println();
		Scanner scanner = new Scanner(System.in);
		String preAccess = scanner.nextLine();
		while (!preAccess.equalsIgnoreCase("IGNORE")
				&& !preAccess.equalsIgnoreCase("MOVE")) {
			System.out.println("Please enter a valid option");
			preAccess = scanner.next();
		}

		System.out.println();
		preaccess = enumConverter(preAccess);
	}

	private static void askForPostaccess() {

		System.out.println("Now choose dealing with post-access errors");
		System.out.println();
		System.out.println("Enter 'Ignore' to ignore them.");
		System.out
				.println("Enter 'Move' to kill objects at the end of the program.");
		System.out.println("Enter 'Don't count' not to count these errors");
		Scanner scanner = new Scanner(System.in);
		String postAccess = scanner.nextLine();
		while (!postAccess.equalsIgnoreCase("IGNORE")
				&& !postAccess.equalsIgnoreCase("MOVE")) {
			System.out.println("Please enter a valid option");
			postAccess = scanner.next();
		}
		postaccess = enumConverter(postAccess);
	}
	
	public static WayToDeal enumConverter(String s){
		if(s.equalsIgnoreCase("IGNORE")||s.equalsIgnoreCase("i")){
			return WayToDeal.IGNORE;
		}
		else if (s.equalsIgnoreCase("MOVE")||s.equalsIgnoreCase("m")){
			return WayToDeal.MOVE;
		}
		return null;
	}

	
}
