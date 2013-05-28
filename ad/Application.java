/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import ad.math.legandre.LegendrePolynomRecurrent;

/**
 *
 * @author shults
 */
public class Application
{

    private Application()
    {
    }

    public static Application getInstance() throws Exception
    {
        if (instance == null) {
            throw new Exception("Instance not initialed. Run application");
        }
        return instance;
    }

    public static void run() throws Exception
    {
        instance = new Application();
        LegendrePolynomRecurrent lp2 = new LegendrePolynomRecurrent((byte) 4);
        double x = -1;
        double step = 0.005;
        
        boolean wasOne = false;
        while (x <= 1 + step) {
            System.out.println("x=" + String.format("%.15f", x) + "\t\ty=" + String.format("%.15f", lp2.getSecondDerivative(x)));
            x += step;
        }
    }
    private static Application instance;
}
