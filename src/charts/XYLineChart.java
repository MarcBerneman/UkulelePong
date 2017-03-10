package charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYLineChart {
	ApplicationFrame frame = new ApplicationFrame("Chart");
	JFreeChart chart;
	ChartPanel chartPanel;
	int x_max;
	
	XYSeriesCollection collection;
	
	public XYLineChart(int x_max) {
		this.x_max = x_max;
	}
	
	public void setData(XYTable table) {
		XYSeries series = new XYSeries("data");
		
		for (int i = 0; i < table.size(); i++)
			if(table.x[i] <= x_max)
				series.add(table.x[i], table.y[i]);
		collection = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Chart", "x", "y", collection);
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
