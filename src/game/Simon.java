package game;

import java.util.ArrayList;

import fft.MicrophoneFFT;

public class Simon {
	private MicrophoneFFT mfft = new MicrophoneFFT(GameMain.SAMPLERATE, GameMain.MAXFREQ, GameMain.MINFREQ,
			GameMain.SOUNDTRESHHOLD, GameMain.MEASUREMENTRATE);
	private int N;
	ArrayList<Integer> sequentie = new ArrayList<Integer>(10);

	public Simon(int N) {
		this.N = N;
	}

	int randomHeight() {
		return (int) (Math.random() * N + 1);
	}

	void turn() {
		int newHeight = randomHeight();
		sequentie.add(newHeight);
		for (int height : sequentie)
			if (readValue() != height)
				lose();

	}

	private void lose() {
		sequentie.clear();
	}

	private int readValue() {
		int string = Frequencydetector(mfft.getDominantFrequency());
		return string;
	}

	public static int Frequencydetector(double frequency) {
		final int ERROR = 10;
		for (int i = 0; i < GameMain.Frequencies.length; i++)
			if (Math.abs(GameMain.Frequencies[i] - frequency) <= ERROR)
				return i + 1;
		return 0;
	}

}
