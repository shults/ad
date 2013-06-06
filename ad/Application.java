/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import ad.math.Quadrature;
import ad.math.Redistribution;
import ad.math.matrix.Matrix;
import java.util.ArrayList;

/**
 *
 * @author shults
 */
public class Application
{

	private Application(double a, double g, double t, double nslab, int M)
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

	public static Application getInstance() throws Exception
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
	public static void run(double a, double g, double t, double nslab, int M) throws Exception
	{
		instance = new Application(a, g, t, nslab, M);
		System.out.println("Cosine of critical angle:");
		System.out.println("v_c=" + getInstance().getV_c());
		System.out.println("Quadrature points (angles):");
		System.out.println(getInstance().getQuadrature().getQuadraturePointsAngles());
		System.out.println("Quadrature points (weight):");
		System.out.println(getInstance().getQuadrature().getQuadraturePointsWeights());
		
		StartLayer sl = new StartLayer();
		System.out.println("\nInit start layer\nC matrix:");
		System.out.println(sl.getC());
		System.out.println("\ngetInverselyVMatrix matrix:");
		System.out.println(sl.getInverselyVMatrix());
		System.out.println("\nhpm matrix:");
		System.out.println(Application.getInstance().getRedistribution().getHpm());
		System.out.println("\nhpp matrix:");
		System.out.println(Application.getInstance().getRedistribution().getHpp());
		System.out.println("getAStar=" + sl.getAStar());
		System.out.println("\nB matrix:");
		System.out.println(sl.getMatrixB());
		System.out.println("\nA matrix:");
		System.out.println(sl.getMatrixA());
		System.out.println("\nG matrix:");
		System.out.println(sl.getMatrixG());
//		
		System.out.println("\nTransmission matrix:");
		System.out.println(sl.getTransmissionMatrix());
		
	}
	
	public double getV_c()
	{
		return v_c;
	}
	
	private void initV_c()
	{
		v_c = Math.cos(Math.asin(1 / nslab));
	}
	
	public Quadrature getQuadrature() throws Exception
	{
		if (getInstance().quadrature == null)
		{
			getInstance().quadrature = new Quadrature(M, getInstance().getV_c(), g);
		}
		return getInstance().quadrature;
	}
	
	public Redistribution getRedistribution() throws Exception
	{
		if (getInstance().redistribution == null)
		{
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
	
	private static Application instance;
	private Quadrature quadrature;
	private Redistribution redistribution;
	private int M = 32;
	private double nslab = 1;
	private double g = 1;
	private double a = 1;
	private double t = 1;
	private double v_c;
}
