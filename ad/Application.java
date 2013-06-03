/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import ad.math.legandre.LegandrePolynom;
import ad.math.Quadrature;
import ad.math.Redistribution;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
		Quadrature quadrature = new Quadrature(4, Math.cos(Math.asin(1 / 1.5)), 0.9);
		Redistribution redistribution = new Redistribution(quadrature);
		for (Quadrature.QP qp : quadrature.getQuadraturePoints())
		{
			System.out.println(qp);
		}
	}
	private static Application instance;
}
