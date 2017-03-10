package sound_input;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import fft.AppliedFFT;
import fft.XYTable;

public class Main {
	static int sampleRate = 44100;

	public static void main(String[] args) {
		ArrayList<Byte> microphoneInput = new ArrayList<Byte>(sampleRate);
		XYTable Spectrum;
		boolean using_data = false;
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
						if (!using_data) {
							byte[] data = new byte[2*sampleRate];
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
					Spectrum = AppliedFFT.AmplitudeFrequenciesFFT(microphoneInput, sampleRate);
					// Spectrum = new XYTable(microphoneInput.toByteArray());
					Spectrum.Chart(1000);
				}
			};

			targetThread.start();
			Thread.sleep(1000);
			fftThread.start();

		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

	}
}
