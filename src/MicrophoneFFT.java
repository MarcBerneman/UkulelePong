import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import charts.XYLineChart;
import charts.XYTable;
import fft.AppliedFFT;



public class MicrophoneFFT {
	static int sampleRate = 44100;
	static boolean using_data = false;
	static int maxFreq = 500;
	static double soundTreshhold = 100000;
	static XYLineChart chart = new XYLineChart(maxFreq);
	
	public static void main(String[] args) {
		
		ArrayList<Byte> microphoneInput = new ArrayList<Byte>(sampleRate/10);
		
		try {
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 2, 4, sampleRate, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open();

			Thread targetThread = new Thread() {
				@Override
				public void run() {
					targetLine.start();
					while (true) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (!using_data) {
							byte[] data = new byte[2 * sampleRate];
							int readBytes = targetLine.read(data, 0, data.length);
							for (int i = 0; i < readBytes / 2; i++)
								microphoneInput.add(data[2 * i + 1]);
						}
					}

				}
			};

			Thread fftThread = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(microphoneInput.size() == sampleRate) {
							using_data = true;
							XYTable Spectrum = AppliedFFT.AmplitudeFrequenciesFFT(microphoneInput, sampleRate);
							chart.setData(Spectrum);
							microphoneInput.clear();
							double dominantFreq = Spectrum.dominantX(maxFreq, soundTreshhold);
							System.out.println(dominantFreq);
						}
						using_data = false;
					}
				}
			};

			targetThread.start();
			fftThread.start();

		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		}

	}
}
