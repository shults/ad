package ad.math.legandre;

import ad.Logger;

/**
 *
 * @author shults
 */
public class LegandrePolynom
{

	public static double getValue(double x, int degree)
	{
		return getValue(x, (byte) degree);
	}

	public static double getValue(double x, byte degree)
	{
		double result = 0;
		if (degree == 0) {
			result = 1;
		} else if (degree == 1) {
			result = x;
		} else if (degree > 1) {
			try {
				result = 0;
				double _degree = (double) degree;
				result += ((2 * (double) _degree - 1) / _degree) * x * LegandrePolynom.getValue(x, (byte) (degree - 1));
				result -= ((_degree - 1) / _degree) * LegandrePolynom.getValue(x, (byte) (degree - 2));
			} catch (Exception ex) {
				Logger.msg("Error in class " + LegandrePolynom.class.getName());
			}
		}
		return result;
	}

	public static double getFirstDerivative(double x, int degree)
	{
		double result = 0;
		if (degree < 1) {
			result = 0;
		} else if (degree == 1) {
			result = 1;
		} else if (degree == 2) {
			result = 3 * x;
		} else {
			try {
				result = 0;
				result += degree * getValue(x, degree - 1);
				result += x * getFirstDerivative(x, degree - 1);
			} catch (Exception ex) {
				Logger.msg("Error in class " + LegandrePolynom.class.getName() + " getFirstDerivative method");
			}
		}
		return result;
	}

	public static double getSecondDerivative(double x, int degree)
	{
		double result = 0;
		if (degree < 2) {
			result = 0;
		} else if (degree == 2) {
			result = 3;
		} else if (degree == 3) {
			result = 15 * x;
		} else {
			try {
				result = 0;
				result += (2 * degree - 1) * getFirstDerivative(x, degree - 1);
				result += getSecondDerivative(x, degree - 2);
			} catch (Exception ex) {
				Logger.msg("Error in class " + LegandrePolynom.class.getName());
			}
		}
		return result;
	}
}
