package input;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fft.AppliedFFT;
import fft.XYTable;

public class FFTtest {

	public static void main(String[] args) {
		test1();
	}
	
	public static void test1() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String path = "sounds/440.wav";
			File wav = new File(path);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(wav));
			int read;
			byte[] buffer = new byte[1411000/8];
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
			XYTable fft = AppliedFFT.AmplitudeFrequenciesFFT(audioBytes, 1411000/8);
			fft.Chart();
			XYTable signal = new XYTable(buffer);
			signal.Chart();
			System.out.println("Finished writing");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void test2() {
		double sampleFreq = 16; // = 2 * maxFreq
		double samplePeriod = 1/sampleFreq;
		int N = 50;
		int A1 = 5;
		int f1 = 1;
		int A2 = 3;
		double f2 = 4.5;
		double[] y = new double[N];
		for (int i = 0; i < N; i++) {
			y[i] = A1 * Math.sin(2 * Math.PI * f1 * i * samplePeriod);
			y[i] += A2 * Math.sin(2 * Math.PI * f2 * i * samplePeriod);
		}
		XYTable fft = AppliedFFT.AmplitudeFrequenciesFFT(y, sampleFreq);
		fft.Chart();
		
	}
}
