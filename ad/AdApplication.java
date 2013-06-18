/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import ad.math.Quadrature;
import ad.math.Redistribution;
import ad.math.matrix.Matrix;
import ad.math.matrix.Row;
import java.util.ArrayList;

/**
 *
 * @author shults
 */
public class AdApplication
{

	private AdApplication(double a, double g, double t, double nslab, int M, boolean detail)
	{
		this.a = a;
		this.g = g;
		this.t = t;
		this.nslab = nslab;
		this.M = M;
		initV_c();
		Logger.msg(String.format("%s=\t%8.3f", "a", a));
		Logger.msg(String.format("%s=\t%8.3f", "g", g));
		Logger.msg(String.format("%s=\t%8.3f", "t", t));
		Logger.msg(String.format("%s=\t%8.3f", "nslab", nslab));
		Logger.msg(String.format("%s=\t%8d", "M", M));
		Logger.msg(String.format("%s=\t%8.3f", "v_c", getV_c()));
	}

	public static AdApplication getInstance() throws Exception
	{
		if (instance == null) {
			throw new Exception("Instance not initialed. Run application");
		}
		return instance;
	}

	/**
	 *
	 * @param a albedo
	 * @param g anisotropy
	 * @param t optical thickness
	 * @param nslab refraction index of slab
	 * @param M number of quadrature points
	 * @throws Exception
	 */
	public static void run(double a, double g, double t, double nslab, int M, boolean detail) throws Exception
	{
		instance = new AdApplication(a, g, t, nslab, M, detail);

		StartLayer sl = new StartLayer(M, a, g, t, 
				getInstance().getQuadrature(), 
				getInstance().getRedistribution().getHpm(),
				getInstance().getRedistribution().getHpp());
		
		BoundaryLayer bl = new BoundaryLayer(nslab, getInstance().getQuadrature());
		
		if (detail) {
			System.out.println("Interim results of calculation:\n");
			System.out.println("Cosine of critical angle:");
			System.out.println(String.format(Config.FLOAT_FORMAT, getInstance().getV_c()) + "\n");
			System.out.println("Quadrature points (angles):");
			System.out.println(getInstance().getQuadrature().getQuadraturePointsAngles() + "\n");
			System.out.println("Quadrature points (weight):");
			System.out.println(getInstance().getQuadrature().getQuadraturePointsWeights() + "\n");

			System.out.println("\nInit start layer\n\nC matrix:\n");
			System.out.println(sl.getC());
			System.out.println("\nInversely V[i] matrix:\n");
			System.out.println(sl.getInverselyVMatrix());
			System.out.println("\nhpm matrix:");
			System.out.println(getInstance().getRedistribution().getHpm());
			System.out.println("\nhpp matrix:");
			System.out.println(getInstance().getRedistribution().getHpp());
			
			System.out.println("\nB matrix:");
			System.out.println(sl.getMatrixB());
			System.out.println("\nA matrix:");
			System.out.println(sl.getMatrixA());
			System.out.println("\nG matrix:");
			System.out.println(sl.getMatrixG());
			
			System.out.println("Boundary layer reflection matrix:");
			System.out.println(bl.getReflectionMatrix());
			
			System.out.println("Boundary layer transmission matrix:");
			System.out.println(bl.getTransmissionMatrix());
			
			System.out.println("\nStart reflection matrix:");
			System.out.println(sl.getReflectionMatrix());
			
			System.out.println("\nStart transmission matrix:");
			System.out.println(sl.getTransmissionMatrix());
		}
		
		sl.runDoubling(detail);
		
		sl.addBoundaryCondition(bl);
		
		if (detail)
		{
			System.out.println("\nResult reflection matrix:");
			System.out.println(sl.getReflectionMatrix());
			
			System.out.println("\nResult transmission matrix:");
			System.out.println(sl.getTransmissionMatrix());
		}
		
		System.out.println("TC\t" + String.format(Config.FLOAT_FORMAT, sl.getColimatedTransmission()));
		System.out.println("RC\t" + String.format(Config.FLOAT_FORMAT, sl.getColimatedReflection()));
		
//		System.out.println("RD\t" + String.format(Config.FLOAT_FORMAT, sl.getDiffuseReflection()));
//		System.out.println("TD\t" + String.format(Config.FLOAT_FORMAT, sl.getDiffuseTransmission()));

	}

	public double getV_c()
	{
		return v_c;
	}

	private void initV_c()
	{
		if (nslab == 0) {
			v_c = 0;
		} else {
			v_c = Math.cos(Math.asin(1 / nslab));
		}
	}

	public Quadrature getQuadrature() throws Exception
	{
		if (getInstance().quadrature == null) {
			getInstance().quadrature = new Quadrature(M, getInstance().getV_c(), g);
		}
		return getInstance().quadrature;
	}

	public Redistribution getRedistribution() throws Exception
	{
		if (getInstance().redistribution == null) {
			getInstance().redistribution = new Redistribution(getInstance().getQuadrature());
		}
		return getInstance().redistribution;
	}

	public int getM()
	{
		return this.M;
	}

	public double getG()
	{
		return this.g;
	}

	public double getA()
	{
		return this.a;
	}

	public double getT()
	{
		return this.t;
	}

	public double getNslab()
	{
		return this.nslab;
	}
	
	private static AdApplication instance;
	private Quadrature quadrature;
	private Redistribution redistribution;
	private int M = 32;
	private double nslab = 1;
	private double g = 1;
	private double a = 1;
	private double t = 1;
	private double v_c;
}
