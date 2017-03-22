package game;

import javax.swing.JFrame;

import fft.MicrophoneFFT;
import uart.ConnectionPanel;

public class GameMain {
	public final static int SAMPLERATE = 44100;
	public final static int MINFREQ = 50;
	public final static int MAXFREQ = 350;
	public final static double SOUNDTRESHHOLD = 500000;
	public final static int MEASUREMENTRATE = 10;

	public final static double[] Frequencies = { 82.4, 110, 146.8, 196, 246.9, 329.6 };
	
	public static int dutycyle = 120;

	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("UART");
		ConnectionPanel panel = new ConnectionPanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		MicrophoneFFT mfft = new MicrophoneFFT(SAMPLERATE, MAXFREQ, MINFREQ, SOUNDTRESHHOLD, MEASUREMENTRATE);
		
		while (true) {
			Thread.sleep(10);
			if (mfft.newMeasurement) {
				double freq = mfft.getDominantFrequency();
				int guitar_string = Simon.Frequencydetector(freq);
				System.out.println(freq + " -> " + guitar_string);
				panel.sendData("G" + Integer.toString(guitar_string));
				Thread.sleep(50);
				panel.sendData("D" + Integer.toString(dutycyle));
			}
		}
	}
}
