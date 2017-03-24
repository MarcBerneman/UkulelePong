package game;

import java.util.ArrayList;

import fft.MicrophoneFFT;
import uart.ConnectionPanel;

public class Simon {
	private MicrophoneFFT mfft = new MicrophoneFFT(GameMain.SAMPLERATE, GameMain.MAXFREQ, GameMain.MINFREQ,
			GameMain.SOUNDTRESHHOLD, GameMain.MEASUREMENTRATE);
	private int nr_colors;
	private ArrayList<Integer> sequentie = new ArrayList<Integer>(10);
	private ConnectionPanel panel;
	private int NR_INDICATOR_LEDs = 10;

	public Simon(int N, ConnectionPanel panel) {
		this.nr_colors = N;
		this.panel = panel;
	}

	public void game() throws InterruptedException {
		boolean lost = false;
		while (sequentie.size() <= GameMain.NR_LEDs - NR_INDICATOR_LEDs) {
			int newHeight = randomHeight();
			System.out.println("Next color: " + newHeight);
			sequentie.add(newHeight);
			//playLinearSequence();
			playPositionSequence();
			for (int targetHeight : sequentie)
				if (!turn(targetHeight)) {
					lost = true;
					break;
				}
			if (lost) {
				lose();
				break;
			}

		}
	}

	private void playLinearSequence() throws InterruptedException {
		indicator("R");
		for (int i = 0; i < sequentie.size(); i++) {
			int lednr = i + NR_INDICATOR_LEDs;
			String lednumber = (lednr < 10 ? "0" + Integer.toString(lednr) : Integer.toString(lednr));
			switch (sequentie.get(i)) {
			case 1:
				panel.sendData("L" + lednumber + "R");
				break;
			case 2:
				panel.sendData("L" + lednumber + "B");
				break;
			case 3:
				panel.sendData("L" + lednumber + "G");
				break;
			case 4:
				panel.sendData("L" + lednumber + "Y");
				break;
			default:
				break;
			}
			Thread.sleep(750);
			panel.sendData("C0");
			Thread.sleep(250);
		}
		Thread.sleep(2000);
		indicator("R");
	}
	
	private void playPositionSequence() throws InterruptedException {
		int[] lightPositions = {35,40,46,55};
		indicator("R");
		for (int color : sequentie) {
			String c = "";
			switch(color) {
			case 1:
				c = "R";
				break;
			case 2:
				c = "B";
				break;
			case 3:
				c = "G";
				break;
			case 4:
				c = "Y";
				break;
			default:
				break;
			}
			panel.sendData("L" + lightPositions[color - 1] + c);
			Thread.sleep(1000);
		}
		Thread.sleep(2000);
		panel.sendData("C0");
		indicator("R");
	}

	private int randomHeight() {
		return (int) (Math.random() * nr_colors + 1);
	}

	private boolean turn(int target_tone) throws InterruptedException {
		int nr_measurements = 750;
		int i = 0;
		int tone = 0;
		int current_tone = 0;
		boolean testing_tone = false;
		indicator("G");
		while (i < nr_measurements) {
			if (mfft.newMeasurement) {
				tone = Simon.Frequencydetector(mfft.getDominantFrequency());
			}
			if (!testing_tone) {
				if (tone != 0) {
					testing_tone = true;
					current_tone = tone;
					panel.sendData("D" + (100 + current_tone * 20));
					System.out.println(current_tone);
				}
			} else {
				if (tone == current_tone) {
					i++;
				} else {
					i = 0;
					testing_tone = false;
				}
			}
			Thread.sleep(1);

		}
		indicator("R");
		System.out.println("Next tone");
		Thread.sleep(1000);
		return tone == target_tone;
	}

	private void lose() {
		if (sequentie.size() < GameMain.NR_LEDs - NR_INDICATOR_LEDs)
			System.out.println("You remembered " + (sequentie.size() - 1) + " colors.");
		else
			System.out.println("You won!");
		sequentie.clear();
		panel.sendData("C1");
	}

	public static int Frequencydetector(double frequency) {
		final int ERROR = 10;
		for (int i = 0; i < GameMain.Frequencies.length; i++)
			if (Math.abs(GameMain.Frequencies[i] - frequency) <= ERROR)
				return i + 1;
		return 0;
	}
	
	private void indicator(String color) throws InterruptedException {
		panel.sendData("I" + (NR_INDICATOR_LEDs - 1) + color);
		Thread.sleep(100);
	}

}
