package charts;

public class XYTable {
	public double[] x, y;
	int N;

	public XYTable(int N) {
		this.N = N;
		x = new double[N];
		y = new double[N];
		initialize();
	}

	public XYTable(double[] x, double[] y) {
		// Assume same size
		N = x.length;
		this.x = x;
		this.y = y;
	}

	public XYTable(double[] y) {
		N = y.length;
		this.y = y;
		for (int i = 0; i < N; i++)
			x[i] = i;
	}

	public XYTable(byte[] y) {
		N = y.length;
		x = new double[N];
		this.y = new double[N];

		for (int i = 0; i < N; i++) {
			x[i] = i;
			this.y[i] = (double) y[i];
		}
	}

	private void initialize() {
		for (int i = 0; i < N; i++) {
			x[i] = 0;
			y[i] = 0;
		}
	}
	
	public int size() {
		return N;
	}
	
	public double dominantX(double max_x, double min_x, double treshhold) {
		for(int i = 0 ; i < N ; i++)
			if(x[i] < max_x && x[i] > min_x && y[i] > treshhold)
				return x[i];
			else if(x[i] >= max_x)
				return 0;
		return 0;
	}
}
