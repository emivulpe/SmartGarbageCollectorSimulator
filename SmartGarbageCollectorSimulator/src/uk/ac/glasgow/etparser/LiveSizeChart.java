package uk.ac.glasgow.etparser;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LiveSizeChart extends ApplicationFrame{
	
	private static final long serialVersionUID = 1L;
	private XYSeries data;

	public LiveSizeChart() {

		super("Live size chart");

		data = new XYSeries("Data");
		data.add(0, 0);

		XYSeriesCollection dataset = new XYSeriesCollection(data);

		// based on the dataset we create the chart
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Livesize change over time", "Time", "Livesize", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);

		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

		// add it to our application
		setContentPane(chartPanel);
		pack();

	}

	public void updateChart(double x, double y) {
		data.add(x, y);
	}
	

}
