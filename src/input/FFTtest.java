package input;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import fft.AppliedFFT;
import fft.XYTable;

public class FFTtest {

	public static void main(String[] args) {
		test1();
	}
	
	public static void test1() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String path = "sounds/1.wav";
			File wav = new File(path);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(wav));
//			int read;
			byte[] buffer = new byte[44100*2];
			in.read(buffer);
			in.read(buffer);
			
			byte[] sound = new byte[buffer.length/2];
			for(int i = 0 ; i < sound.length ; i++)
				sound[i] = buffer[2*i+1];
//			in.read(buffer);
//			in.read(buffer);
			XYTable signal = new XYTable(sound);
			signal.Chart(100000);
//			while ((read = in.read(buffer)) > 0) {
//				out.write(buffer, 0, read);
//			}
			System.out.println("Finished reading");
//			out.flush();
			in.close();
//			byte[] audioBytes = out.toByteArray();
			XYTable fft = AppliedFFT.AmplitudeFrequenciesFFT(sound, 44100*2);
			fft.Chart(500);
			System.out.println("Finished writing");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void test2() {
		double sampleFreq = 16;
		double samplePeriod = 1/sampleFreq;
		int N = 500;
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
		fft.Chart(20);
		
	}
}
