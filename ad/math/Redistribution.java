package ad.math;

import ad.math.legandre.LegandrePolynom;
import java.util.ArrayList;

/**
 *
 * @author shults
 */
public class Redistribution
{
	
	public Redistribution(Quadrature quadrature)
	{
		this.quadrature = quadrature;
		this.hmm = new double[quadrature.getM()][quadrature.getM()];
		this.hmp = new double[quadrature.getM()][quadrature.getM()];
		this.hpm = new double[quadrature.getM()][quadrature.getM()];
		this.hpp = new double[quadrature.getM()][quadrature.getM()];
	}
	
	public double[][][][] gerTedistributionMatrix()
	{
		if (redistributionMatrix == null)
		{
			redistributionMatrix = new double[2][2][quadrature.getM()][quadrature.getM()];
		}
		return redistributionMatrix;
	}
	
	private double[][] getHmm() throws Exception
	{
		if (hmm == null)
		{
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hmm[i][j] = getRedistribution(qp_i.getV(), qp_j.getV());
					j++;
				}
				i++;
			}
		}
		return hmm;
	}
	
	private double[][] getHmp() throws Exception
	{
		if (hmp == null)
		{
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hmp[i][j] = getRedistribution(qp_i.getV(), - qp_j.getV());
					j++;
				}
				i++;
			}
		}
		return hmp;
	}
	
	private double[][] getHpm() throws Exception
	{
		if (hpm == null)
		{
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hpm[i][j] = getRedistribution( - qp_i.getV(), qp_j.getV());
					j++;
				}
				i++;
			}
		}
		return hpm;
	}
	
	private double[][] getHpp() throws Exception
	{
		if (hpp == null)
		{
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hpp[i][j] = getRedistribution( - qp_i.getV(), qp_j.getV());
					j++;
				}
				i++;
			}
		}
		return hpp;
	}
	
	private double getRedistribution(double v_i, double v_j)
	{
		double summ = 0;
		for (int k = 0; k <= quadrature.getM() - 1; k++)
		{
			summ += (2 * k + 1) * getHi(k) * LegandrePolynom.getValue(v_i, k) * LegandrePolynom.getValue(v_j, k);
		}
		return summ;
	}
	
	private double getHi(int k)
	{
		return (Math.pow(quadrature.getG(), k) - Math.pow(quadrature.getG(), quadrature.getM())) 
				/ (1 - Math.pow(quadrature.getG(), quadrature.getM()));
	}
	
	private Quadrature quadrature;
	private double[][] hmm, hmp, hpm, hpp;
	private double[][][][] redistributionMatrix;
}
