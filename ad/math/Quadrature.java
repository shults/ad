package ad.math;

import java.util.ArrayList;
import ad.math.legandre.LegandrePolynom;

/**
 * This
 *
 * @author shults
 */
public class Quadrature
{
	
	/**
	 * @param degree Degree of quadrure
	 * @param v_c Cosine of critical angle
	 */
	public Quadrature(int degree, double v_c, double g)
	{
		this.v_c = v_c;
		this.degree = degree / 2;
		this.g = g;
	}

	private class Range
	{
		public Range(double a, double b)
		{
			this.a = a;
			this.b = b;
		}

		public double getA()
		{
			return a;
		}

		public double getB()
		{
			return b;
		}

		public String toString()
		{
			return "a=" + a + "\t\tb=" + b;
		}
		
		private double a;
		private double b;
	}
	
	/**
	 * Quadrature Point
	 * @param v Cos of angle of quadrature point
	 * @param w Weight of quadrature point
	 */
	public class QP
	{
		/**
		 * @param v Cos of quadrature angle
		 * @param w Weigth
		 * @param type Type of quadrature point 
		 */
		public QP(double v, double w, byte type) throws Exception
		{
			if (type != TYPE_GAUSS && type != TYPE_RADAU)
				throw new Exception("Unallowed type");
			this.type = type;
			this.v = v;
			this.w = w;
		}
		
		/**
		 * @return Cos of quadrature point
		 */
		public double getV()
		{
			return this.v;
		}
		
		/**
		 * @return The weight of quadrature point
		 */
		public double getW()
		{
			return this.w;
		}
		
		public String toString()
		{
			return "v=" + String.format("%10f", getV()) + "\t\tw=" + String.format("%10f", getW());
		}
		
		public static final byte TYPE_RADAU = 1;
		public static final byte TYPE_GAUSS = 2;
		private byte type;
		private double v;
		private double w;
	}
	
	public ArrayList<QP> getQuadraturePoints(byte order) throws Exception
	{
		ArrayList<QP> _quadraturePoints;
		if (order != ORDER_QP_ASC && order != ORDER_QP_DESC)
			throw new Exception("Order type do not supported");
		if (order == ORDER_QP_ASC)
			_quadraturePoints = getQuadraturePoints();
		else 
		{
			_quadraturePoints = new ArrayList<>();
			for (int i = getQuadraturePoints().size() - 1; i >= 0; i--)
			{
				_quadraturePoints.add(getQuadraturePoints().get(i));
			}
		}
		return _quadraturePoints;
	}
	
	public ArrayList<QP> getQuadraturePoints() throws Exception
	{
		double v;
		double w;
		if (quadraturePoints == null || quadraturePoints.isEmpty())
		{
			// finding Radau quadrature roots
			for (double x : getRadauRoots())
			{
				v = (0.5 * (1 + v_c))
					- (0.5 * (1 - v_c) * x);
				w = (1 - v_c) 
					/ (2 * (1 - x) * Math.pow(LegandrePolynom.getFirstDerivative(x, degree - 1), 2));
				quadraturePoints.add(new QP(v, w, QP.TYPE_RADAU));
			}
			
			// finding Gauss quadrature roots
			for (double x : getGaussRoots())
			{
				v = 0.5 * v_c * (1 - x);
				w = v_c / ((1 - x * x) * Math.pow(LegandrePolynom.getFirstDerivative(x, degree), 2));
				quadraturePoints.add(new QP(v, w, QP.TYPE_GAUSS));
			}
			this.orderQP();
		}
		return quadraturePoints;
	}
	
	/**
	 * This method order quadrature points ascending by cosine of angle theta
	 */
	private void orderQP()
	{
		QP qpCurrent, qpNext;
		for (int i = 0; i < quadraturePoints.size() - 1; i++)
		{
			for (int j = 0; j < quadraturePoints.size() - i - 1; j++)
			{
				qpCurrent = quadraturePoints.get(j);
				qpNext = quadraturePoints.get(j + 1);
				if (qpCurrent.getV() > qpNext.getV())
				{
					quadraturePoints.set(j, qpNext);
					quadraturePoints.set(j + 1, qpCurrent);
				}
			}
		}
	}

	private ArrayList<Double> getGaussRoots()
	{
		if (gaussRoots == null || gaussRoots.isEmpty())
		{
			double x;
			double x_prev;
			int i;
			ArrayList<Range> ranges = this.getGaussRanges();
			for (Range range : ranges) 
			{
				x = x_prev = range.getB();
				i = 0;
				do 
				{
					x_prev = x;
					x = x_prev - LegandrePolynom.getValue(x_prev, degree) / LegandrePolynom.getFirstDerivative(x_prev, degree);
				} while (Math.abs(x - x_prev) > ABSOLUTE_ERROR);
				gaussRoots.add(x);
			}
		}
		return gaussRoots;
	}
	
	private ArrayList<Range> getGaussRanges()
	{
		ArrayList<Quadrature.Range> ranges = new ArrayList<>();
		double x = -1;
		double step = 1 / ((double) STEP_DEGREE_MULPLIER * this.degree);
		while (x < 1) 
		{
			if (LegandrePolynom.getValue(x, degree) * LegandrePolynom.getValue(x + step, degree) < 0
				&& LegandrePolynom.getFirstDerivative(x, degree) * LegandrePolynom.getFirstDerivative(x + step, degree) > 0) 
			{
				ranges.add(new Range(x, x + step));
			}
			x += step;
		}
		return ranges;
	}
	

	private ArrayList<Double> getRadauRoots()
	{
		if (radauRoots == null || radauRoots.isEmpty())
		{
			double x;
			double x_prev;
			int i;
			ArrayList<Range> ranges = getRadauRanges();
			radauRoots.add(-1.0);
			for (Range range : ranges) 
			{
				x = x_prev = range.getB();
				i = 0;
				do 
				{
					x_prev = x;
					x = x_prev - getRadauValue(x_prev) / getRadauDerivative(x_prev);
				} while (Math.abs(x - x_prev) > ABSOLUTE_ERROR);
				radauRoots.add(x);
			}
		}
		return radauRoots;
	}

	private ArrayList<Quadrature.Range> getRadauRanges()
	{
		ArrayList<Quadrature.Range> ranges = new ArrayList<>();
		double x = -1;
		double step = 1 / ((double) STEP_DEGREE_MULPLIER * this.degree);
		while (x < 1) 
		{
			if (getRadauValue(x) * getRadauValue(x + step) < 0
				&& getRadauDerivative(x) * getRadauDerivative(x + step) > 0) 
			{
				ranges.add(new Range(x, x + step));
			}
			x += step;
		}
		return ranges;
	}

	private double getRadauValue(double x)
	{
		return LegandrePolynom.getValue(x, this.degree - 1)
				+ ((x - 1) / this.degree) * LegandrePolynom.getFirstDerivative(x, this.degree - 1);
	}

	private double getRadauDerivative(double x)
	{
		return ((this.degree + 1.0) / this.degree) * LegandrePolynom.getFirstDerivative(x, this.degree - 1)
				+ ((x - 1.0) / this.degree) * LegandrePolynom.getSecondDerivative(x, this.degree - 1);
	}
	
	/**
	 * @return Degree of quadrature
	 */
	public int getM()
	{
		return 2 * this.degree;
	}
	
	/**
	 * @return anisotropy of a slab
	 */
	public double getG()
	{
		return this.g;
	}
	
	public static final byte ORDER_QP_ASC = 1;
	public static final byte ORDER_QP_DESC = 2;
	private static final int STEP_DEGREE_MULPLIER = 50;
	private static final double ABSOLUTE_ERROR = 1E-6;
	private int degree;
	private double v_c;
	private double g;
	private ArrayList<Double> radauRoots = new ArrayList<>();
	private ArrayList<Double> gaussRoots = new ArrayList<>();
	private ArrayList<QP> quadraturePoints = new ArrayList<>();
}
