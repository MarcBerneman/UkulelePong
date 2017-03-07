package fft;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYTable {
	public double[] x, y;
	private int N;

	public XYTable(int N) {
		this.N = N;
		x = new double[N];
		y = new double[N];
		initialize();
	}

	public XYTable(double[] x, double[] y) {
		// Assume same size
		N = x.length;
		this.x = x;
		this.y = y;
	}

	public XYTable(double[] y) {
		N = y.length;
		this.y = y;
		for (int i = 0; i < N; i++)
			x[i] = i;
	}

	public XYTable(byte[] y) {
		N = y.length;
		x = new double[N];
		this.y = new double[N];

		for (int i = 0; i < N; i++) {
			x[i] = i;
			this.y[i] = (double) y[i];
		}
	}

	public void Chart(double max) {
		ApplicationFrame frame = new ApplicationFrame("Chart");
		XYSeries series = new XYSeries("data");
		for (int i = 0; i < N; i++)
			if(x[i] <= max)
				series.add(x[i], y[i]);
		XYSeriesCollection collection = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Chart", "x", "y", collection);
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private void initialize() {
		for (int i = 0; i < N; i++) {
			x[i] = 0;
			y[i] = 0;
		}
	}
}
