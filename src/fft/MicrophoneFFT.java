package fft;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import charts.XYTable;

public class MicrophoneFFT {
	private  double dominantFreq = 0;
	public boolean newMeasurement = false;
	
	private boolean using_data = false;
	//private XYLineChart chart;

	public MicrophoneFFT(int sampleRate, int maxFreq, int minFreq, double soundTreshhold, int measurementRate) {
		//chart = new XYLineChart(maxFreq);

		ArrayList<Byte> microphoneInput = new ArrayList<Byte>(sampleRate / measurementRate);
		try {
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 2, 4, sampleRate,
					false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open();

			Thread targetThread = new Thread() {
				@Override
				public void run() {
					targetLine.start();
					while (true) {
						try {
							Thread.sleep(1);
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
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (microphoneInput.size() == sampleRate) {
							using_data = true;
							XYTable Spectrum = AppliedFFT.AmplitudeFrequenciesFFT(microphoneInput, sampleRate);
							//chart.setData(Spectrum);
							microphoneInput.clear();
							dominantFreq = Spectrum.dominantX(maxFreq,minFreq,soundTreshhold);
							newMeasurement = true;
						}
						try {
							Thread.sleep(1000/measurementRate);
						} catch (InterruptedException e) {
							e.printStackTrace();
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
	
	public double getDominantFrequency() {
		//Assume the user checked that newMeasurement == true
		newMeasurement = false;
		return dominantFreq;
	}
}
