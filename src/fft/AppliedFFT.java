package fft;

public class AppliedFFT {
	public static double[] AmplitudeFFT(double[] signal) {
		int N = signal.length;

		Complex1D coefficients = new Complex1D();
		RealDoubleFFT fft = new RealDoubleFFT(N);
		fft.ft(signal, coefficients);
		double[] output = coefficients.absolute();
		for (int i = 0; i < output.length; i++)
			output[i] *= 2 / fft.norm_factor;
		return output;
	}

	public static double[] AmplitudeFFT(byte[] signal) {
		int N = signal.length;
		double[] signaldouble = new double[N];
		for (int i = 0; i < N; i++)
			signaldouble[i] = (double) signal[i];

		Complex1D coefficients = new Complex1D();
		RealDoubleFFT fft = new RealDoubleFFT(signaldouble.length);
		fft.ft(signaldouble, coefficients);
		double[] output = coefficients.absolute();
		for (int i = 0; i < output.length; i++)
			output[i] *= 2 / fft.norm_factor;
		return output;
	}
}