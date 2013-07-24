package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import uk.ac.glasgow.etparser.LiveSizeChart;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.events.Event;

import java.awt.BorderLayout;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LiveSize implements EventHandler {

	private long liveSize, allocatedMemorySize;
	private int numCreationsOrDeaths; // why do we need this???
	private HashMap<String, Integer> objectSizes;
	private StatisticsLogger logger; // doesn't work- how to output in a file???
	public static final int OUTPUT_INTERVAL = 1000; // output to logfile after this number of events
	private LiveSizeChart chart;


	private Queue<String> allocatedObjects;


	public LiveSize(boolean chartVisible) {
		objectSizes = new HashMap<String, Integer>();
		logger = new StatisticsLogger();
		liveSize = 0;
		allocatedMemorySize = 0;
		numCreationsOrDeaths = 0;
		allocatedObjects = new LinkedList<String>();
		System.out.println(chartVisible+"chaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaart in ls");
		chart=new LiveSizeChart(chartVisible);

	}
	
	public void setChartVisible(){
		chart.setVisible(true);
	}

	private boolean checkSizeLimitExcess() {
		return allocatedMemorySize >= 10000;
	}

	private void deallocateFirstObjects() {
		while (checkSizeLimitExcess() || allocatedMemorySize >= 8000) {
			String objectToDeallocate = allocatedObjects.remove();
			int sizeOfObject = objectSizes.remove(objectToDeallocate);
			allocatedMemorySize -= sizeOfObject;
			SimulatedHeap.getTheHeap().removeRecord(objectToDeallocate);
		}
	}

	public void deallocateLeastRecentlyUsedObjects(){
		
	}
	// getters

	public long getLiveSize() {
		return liveSize;
	}

	public long getAllocatedMemSize() {
		return allocatedMemorySize;
	}

	public int getNumCreationsOrDeaths() {
		return numCreationsOrDeaths;
	}

	// methods inherited by EventHandler

	@Override
	public void handle(Event e) {
		if (e.getStatus().equalsIgnoreCase("A")) {
			CreationEvent ce = (CreationEvent) e;
			objectSizes.put(e.getObjectID(), ce.getSize());
			allocatedObjects.add(e.getObjectID());
			liveSize += ce.getSize();
			allocatedMemorySize += ce.getSize();
			numCreationsOrDeaths++;
			if (checkSizeLimitExcess()) {
				deallocateFirstObjects();

			}


		}
		if (e.getStatus().equalsIgnoreCase("D")
				&& objectSizes.get(e.getObjectID()) != null) {
			numCreationsOrDeaths++;
			liveSize -= objectSizes.get(e.getObjectID());
			assert (liveSize >= 0);

		}
		updateChart();
		report();

	}
	
	public void updateChart(){
		int time=SimulatedHeap.getTheHeap().getTimeSequence();
		chart.updateChart(time, liveSize);
	}
	
	
	

	public void report() {
		if (numCreationsOrDeaths % OUTPUT_INTERVAL == 0) {
			// how to output to a logfile??????

			logger.getLogger().info(liveSize + ", " + allocatedMemorySize);
			System.out.println("Live size: " + liveSize + " Allocated memory: "
					+ allocatedMemorySize);

		}
	}

	public LiveSizeChart createChart() {

		LiveSizeChart chart = new LiveSizeChart(true);

		return chart;
	}

}
