/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import ad.math.legandre.LegandrePolynom;
import ad.math.Quadrature;
import ad.math.Redistribution;
import ad.math.matrix.Matrix;
import ad.math.matrix.Row;
import java.util.ArrayList;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author shults
 */
public class Application
{

	private Application() {}

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
		int size = 4;
		int i;
		Matrix c, A, B, G;
		Quadrature quadrature = new Quadrature(size, Math.cos(Math.asin(1 / 1.5)), 0.9);
		Redistribution redistribution = new Redistribution(quadrature);
		ArrayList<Quadrature.QP> qPoints = quadrature.getQuadraturePoints();
		c = new Matrix(size);
		
		for (i = 0; i < size; i++)
		{
			c.setItemValue(i, i, qPoints.get(i).getW());
		}
		
//		Matrix m = new Matrix(new Row(-1.0, 2.98, 3.5),
//							  new Row(3.1 * 6, 4.5554, 2.6),
//							  new Row(+2.0, 2.0, -6));
		System.out.println(c);
		System.out.println(c.getInvertibleMatrix());
		System.out.println(c.multiply(c.getInvertibleMatrix()));
		System.out.println(quadrature.getQuadraturePointsAngles());
		System.out.println(quadrature.getQuadraturePointsWeights());
		
	}
	private static Application instance;
}
