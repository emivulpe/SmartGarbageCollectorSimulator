package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
	public static final int OUTPUT_INTERVAL = 1000; // output to logfile after
													// this number of events
	private Queue<String> allocatedObjects;

	public LiveSizeChart chart;

	public LiveSize() {
		objectSizes = new HashMap<String, Integer>();
		logger = new StatisticsLogger();
		liveSize = 0;
		allocatedMemorySize = 0;
		numCreationsOrDeaths = 0;
		allocatedObjects = new LinkedList<String>();
		chart = new LiveSizeChart();
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
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
			chart.updateDataset(10, 12);

		}
		if (e.getStatus().equalsIgnoreCase("D")
				&& objectSizes.get(e.getObjectID()) != null) {
			numCreationsOrDeaths++;
			liveSize -= objectSizes.get(e.getObjectID());
			assert (liveSize >= 0);

		}

		report();

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

		LiveSizeChart chart = new LiveSizeChart();

		return chart;
	}

	public class LiveSizeChart extends ApplicationFrame {
		XYSeriesCollection dataset;
		XYSeries series1;
		JFreeChart chart;
		ChartPanel chartPanel;
		public LiveSizeChart() {
			
			super("Live size chart");

			dataset = (XYSeriesCollection) createDataset();
			chart = createChart(dataset);
			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			setContentPane(chartPanel);

		}

		/**
		 * Creates a sample dataset.
		 * 
		 * @return a sample dataset.
		 */
		private XYDataset createDataset() {

			series1 = new XYSeries("First");
			series1.add(0.0, 0.0);

			dataset = new XYSeriesCollection();
			dataset.addSeries(series1);

			return dataset;

		}
		
		private void updateDataset(double x,double y){
			series1.add(x, y);
			dataset.addSeries(series1);
			repaintChart();
		}

		
		private void repaintChart(){
//			   chartPanel.removeAll();
//			   chartPanel.revalidate(); // This removes the old chart 
			    chart = createChart(dataset); 
			    chart.removeLegend(); 
			    ChartPanel chartPanel = new ChartPanel(chart); 
			    chartPanel.setLayout(new BorderLayout()); 
			    chartPanel.add(chartPanel); 
			    chartPanel.repaint();
			
		}
		/**
		 * Creates a chart.
		 * 
		 * @param dataset
		 *            the data for the chart.
		 * 
		 * @return a chart.
		 */
		private JFreeChart createChart(final XYDataset dataset) {

			// create the chart...
			final JFreeChart chart = ChartFactory.createXYLineChart(
					"Line Chart Demo 6", // chart title
					"X", // x axis label
					"Y", // y axis label
					dataset, // data
					PlotOrientation.VERTICAL, true, // include legend
					true, // tooltips
					false // urls
					);

			// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
			chart.setBackgroundPaint(Color.white);

			// final StandardLegend legend = (StandardLegend) chart.getLegend();
			// legend.setDisplaySeriesShapes(true);

			// get a reference to the plot for further customisation...
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(Color.lightGray);
			// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,
			// 5.0));
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);

			final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setSeriesLinesVisible(0, false);
			renderer.setSeriesShapesVisible(1, false);
			plot.setRenderer(renderer);

			// change the auto tick unit selection to integer units only...
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			// OPTIONAL CUSTOMISATION COMPLETED.

			return chart;

		}


		// ****************************************************************************
		// * JFREECHART DEVELOPER GUIDE *
		// * The JFreeChart Developer Guide, written by David Gilbert, is
		// available *
		// * to purchase from Object Refinery Limited: *
		// * *
		// * http://www.object-refinery.com/jfreechart/guide.html *
		// * *
		// * Sales are used to provide funding for the JFreeChart project -
		// please *
		// * support us so that we can continue developing free software. *
		// ****************************************************************************

		/**
		 * Starting point for the demonstration application.
		 * 
		 * @param args
		 *            ignored.
		 */
		public void main(final String[] args) {

			final LiveSizeChart demo = new LiveSizeChart();
			demo.pack();
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);

		}

	}

}
