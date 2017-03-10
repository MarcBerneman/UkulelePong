package game;

import fft.MicrophoneFFT;

public class GameMain {
	public final static int SAMPLERATE = 44100;
	public final static int MAXFREQ = 500;
	public final static double SOUNDTRESHHOLD = 30000;
	public final static int MEASUREMENTRATE = 10;

	public final static double[] Frequencies = { 82.4, 110, 146.8, 196, 246.9, 329.6 };

	public static void main(String[] args) throws InterruptedException {
		MicrophoneFFT mfft = new MicrophoneFFT(SAMPLERATE, MAXFREQ, SOUNDTRESHHOLD, MEASUREMENTRATE);
		while (true) {
			Thread.sleep(1);
			if (mfft.newMeasurement) {
				System.out.println(Frequencydetector(mfft.getDominantFrequency()));
			}

		}
	}
	
	public static int Frequencydetector(double frequency) {
		int ERROR = 10;
		for(int i = 0 ; i < Frequencies.length ; i++)
			if(Math.abs(Frequencies[i] - frequency) <= ERROR)
				return i+1;
		return 0;
	}

}
