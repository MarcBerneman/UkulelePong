import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class test {

	public static void main(String[] args) {
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open();

			info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open();

			final ByteArrayOutputStream out = new ByteArrayOutputStream();

			Thread sourceThread = new Thread() {
				@Override
				public void run() {
					sourceLine.start();
					while (true) {
						sourceLine.write(out.toByteArray(), 0, out.size());
					}
				}
			};

			Thread targetThread = new Thread() {
				@Override
				public void run() {
					targetLine.start();
					byte[] data = new byte[targetLine.getBufferSize() / 5];
					int readBytes;
					while(true) {
						readBytes = targetLine.read(data, 0, data.length);
						out.write(data, 0, readBytes);
					}
				}
			};
			targetThread.start();
			Thread.sleep(5000);
			targetLine.stop();
			targetLine.close();

			sourceThread.start();
			Thread.sleep(5000);
			sourceLine.stop();
			sourceLine.close();

		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
