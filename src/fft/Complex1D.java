package fft;
/**
  * Construct a 1-D complex data sequence.
*/
public class Complex1D
{
/**
  * <em>x</em>[<em>i</em>] is the real part of <em>i</em>-th complex data.
*/
    public double x[];
/**
  * <em>y</em>[<em>i</em>] is the imaginary part of <em>i</em>-th complex data.
*/
    public double y[];
    
	public double[] absoluteSquared() {
		double[] absolutes = new double[x.length];
		for (int i = 0; i < x.length; i++)
			absolutes[i] = (x[i] * x[i] + y[i] * y[i]);
		return absolutes;
	}
	
	public double[] absolute() {
		double[] absolutes = new double[x.length];
		for (int i = 0; i < x.length; i++)
			absolutes[i] = Math.sqrt(x[i] * x[i] + y[i] * y[i]);
		return absolutes;
	}
}
