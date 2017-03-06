package input;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import fft.AppliedFFT;

public class FFTtest {

	public static void main(String[] args) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String path = "sounds/440.wav";
			File wav = new File(path);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(wav));
			int read;
			byte[] buffer = new byte[50000];
//			while ((read = in.read(buffer)) > 0) {
//				out.write(buffer, 0, read);
//			}
			read = in.read(buffer);
			read = in.read(buffer);
			read = in.read(buffer);
			read = in.read(buffer);
			out.write(buffer,0,read);
			System.out.println("Finished reading");
			out.flush();
			in.close();
			byte[] audioBytes = out.toByteArray();
			double[] fftAmplitudes = AppliedFFT.AmplitudeFFT(buffer);
			MakeChart(frequencies(0,fftAmplitudes.length), fftAmplitudes);
			System.out.println("Finished writing");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void WriteToText(double[] data) throws IOException {
		File file = new File("data.txt");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for (double x : data)
			bw.write(x + "\n");
		bw.close();
	}
	
	public static void MakeChart(double[] x, double[] y) {
		ApplicationFrame frame = new ApplicationFrame("Chart");
		XYSeries series = new XYSeries("data");
		for(int i = 0 ; i < x.length ; i++)
			series.add(x[i],y[i]);
		XYSeriesCollection collection = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYBarChart("data", "x",false, "y", collection);
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static double[] frequencies(double SampleFrequency, int N) {
		double[] output = new double[N];
		for(int i = 0 ; i < N; i++)
			output[i] = i;
		return output;
	}

}
