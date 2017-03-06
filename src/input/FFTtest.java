package input;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import fft.AppliedFFT;

public class FFTtest {

	public static void main(String[] args) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String path = "sounds/guitar.wav";
			File wav = new File(path);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(wav));
			int read;
			byte[] buffer = new byte[10000];
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
			WriteToText(fftAmplitudes);
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

}
