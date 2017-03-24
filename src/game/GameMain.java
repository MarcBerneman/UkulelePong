package game;

import javax.swing.JFrame;

import uart.ConnectionPanel;

public class GameMain {
	public final static int SAMPLERATE = 44100;
	public final static int MINFREQ = 50;
	public final static int MAXFREQ = 350;
	public final static double SOUNDTRESHHOLD = 500000;
	public final static int MEASUREMENTRATE = 10;
	public final static int NR_LEDs = 60;

	public final static double[] Frequencies = { 329.6, 246.9, 196, 146.8, 110, 82.4 };

	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("UART");
		ConnectionPanel panel = new ConnectionPanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setSize(frame.getWidth() + 20, frame.getHeight());
	}
}
