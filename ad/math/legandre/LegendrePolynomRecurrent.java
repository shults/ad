package ad.math.legandre;

import ad.Logger;

/**
 *
 * @author shults
 */
public class LegendrePolynomRecurrent implements ILegandrePolynom
{

    public LegendrePolynomRecurrent(byte _degree) throws Exception
    {
        if (_degree < 0) {
            throw new Exception("Degree of polynom cannot by less zero");
        }
        this.degree = _degree;
    }

    @Override
    public double getValue(double x)
    {
        double result = 0;
        if (this.degree == 0) {
            result = 1;
        } else if (this.degree == 1) {
            result = x;
        } else if (this.degree > 1) {
            LegendrePolynomRecurrent lpm1;
            LegendrePolynomRecurrent lpm2;
            try {
                result = 0;
                lpm1 = new LegendrePolynomRecurrent((byte) (this.degree - 1));
                lpm2 = new LegendrePolynomRecurrent((byte) (this.degree - 2));
                double _degree = (double) this.degree;
                result += ((2 * (double) _degree - 1) / _degree) * x * lpm1.getValue(x);
                result -= ((_degree - 1) / _degree) * lpm2.getValue(x);
            } catch (Exception ex) {
                Logger.msg("Error in class " + LegendrePolynomRecurrent.class.getName());
            }
        }
        return result;
    }

    @Override
    public double getFirstDerivative(double x)
    {
        double result = 0;
        if (this.degree < 1) {
            result = 0;
        } else if (this.degree == 1) {
            result = 1;
        } else if (this.degree == 2) {
            result = 3 * x;
        } else {
            LegendrePolynomRecurrent lpm1;
            try {
                result = 0;
                lpm1 = new LegendrePolynomRecurrent((byte) (this.degree - 1));
                result += this.degree * lpm1.getValue(x);
                result += x * lpm1.getFirstDerivative(x);
            } catch (Exception ex) {
                Logger.msg("Error in class " + LegendrePolynomRecurrent.class.getName() + " getFirstDerivative method");
            }
        }
        return result;
    }

    @Override
    public double getSecondDerivative(double x)
    {
        double result = 0;
        if (this.degree < 2) {
            result = 0;
        } else if (this.degree == 2) {
            result = 3;
        } else if (this.degree == 3) {
            result = 15 * x;
        } else {
            try {
                result = 0;
                LegendrePolynomRecurrent lpm1 = new LegendrePolynomRecurrent((byte) (this.degree - 1));
                LegendrePolynomRecurrent lpm2 = new LegendrePolynomRecurrent((byte) (this.degree - 2));
                result += (2 * this.degree - 1) * lpm1.getFirstDerivative(x);
                result += lpm2.getSecondDerivative(x);
            } catch (Exception ex) {
                Logger.msg("Error in class " + LegendrePolynomRecurrent.class.getName());
            }
        }
        return result;
    }
    private byte degree;
}
