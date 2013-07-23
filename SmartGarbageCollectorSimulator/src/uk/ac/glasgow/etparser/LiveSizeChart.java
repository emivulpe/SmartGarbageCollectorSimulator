package uk.ac.glasgow.etparser;

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



public class LiveSizeChart extends ApplicationFrame {
	XYSeriesCollection dataset;
	XYSeries series1;
	JFreeChart chart;
	ChartPanel chartPanel;
	private int time;
	private int livesize;
	private XYSeries data;
	
	public LiveSizeChart() {
		
		super("Live size chart");
		
		data=new XYSeries("Data");
		data.add(0, 0);
        
        XYSeriesCollection dataset = new XYSeriesCollection(data);
        
        // based on the dataset we create the chart
        JFreeChart chart = ChartFactory.createXYLineChart("Livesize change over time", "Time",
                "Livesize", dataset, PlotOrientation.VERTICAL, false, false, false);
        
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        
        // add it to our application
        setContentPane(chartPanel);
        pack();
        setVisible(true);

//		dataset = (XYSeriesCollection) createDataset();
//		chart = createChart(dataset);
//		chartPanel = new ChartPanel(chart);
//		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//		setContentPane(chartPanel);

	}
	
	public void updateChart(double x,double y){
		    	try {
		    		Thread.sleep(1000);
		    	} catch (Exception e) {
		    		System.err.println("Exception whilst sleeping.");
		    		System.err.println(e);
		    		e.printStackTrace();
		    		System.exit(-1);
		    	}
		    	data.add(x,y);
		
	
  
    }


//	public void updateDataset(double x,double y){
//		series1.add(x, y);
//		dataset.addSeries(series1);
//		repaintChart();
//	}

	
//	private void repaintChart(){
////		   chartPanel.removeAll();
////		   chartPanel.revalidate(); // This removes the old chart 
//		    chart = createChart(dataset); 
//		    chart.removeLegend(); 
//		    ChartPanel chartPanel = new ChartPanel(chart); 
//		    chartPanel.setLayout(new BorderLayout()); 
//		    chartPanel.add(chartPanel); 
//		    chartPanel.repaint();
//		
//	}
//	/**
//	 * Creates a chart.
//	 * 
//	 * @param dataset
//	 *            the data for the chart.
//	 * 
//	 * @return a chart.
//	 */
//	private JFreeChart createChart(final XYDataset dataset) {
//
//		// create the chart...
//		final JFreeChart chart = ChartFactory.createXYLineChart(
//				"Line Chart Demo 6", // chart title
//				"X", // x axis label
//				"Y", // y axis label
//				dataset, // data
//				PlotOrientation.VERTICAL, true, // include legend
//				true, // tooltips
//				false // urls
//				);
//
//		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
//		chart.setBackgroundPaint(Color.white);
//
//		// final StandardLegend legend = (StandardLegend) chart.getLegend();
//		// legend.setDisplaySeriesShapes(true);
//
//		// get a reference to the plot for further customisation...
//		final XYPlot plot = chart.getXYPlot();
//		plot.setBackgroundPaint(Color.lightGray);
//		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,
//		// 5.0));
//		plot.setDomainGridlinePaint(Color.white);
//		plot.setRangeGridlinePaint(Color.white);
//
//		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//		renderer.setSeriesLinesVisible(0, false);
//		renderer.setSeriesShapesVisible(1, false);
//		plot.setRenderer(renderer);
//
//		// change the auto tick unit selection to integer units only...
//		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//		// OPTIONAL CUSTOMISATION COMPLETED.
//
//		return chart;
//
//	}


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