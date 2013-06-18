package ad.math;

import ad.math.legandre.LegandrePolynom;
import ad.math.matrix.Matrix;

/**
 *
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public class Redistribution
{
	
	public Redistribution(Quadrature quadrature)
	{
		this.quadrature = quadrature;
	}
	
	public Matrix getHmm() throws Exception
	{
		if (hmm == null)
		{
			hmm = new Matrix(quadrature.getM());
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hmm.setItemValue(i, j, getRedistributionValue(- qp_i.getV(), - qp_j.getV()));
					j++;
				}
				i++;
			}
		}
		return hmm;
	}
	
	public Matrix getHmp() throws Exception
	{
		if (hmp == null)
		{
			hmp = new Matrix(quadrature.getM());
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hmp.setItemValue(i, j, getRedistributionValue(- qp_i.getV(), qp_j.getV()));
					j++;
				}
				i++;
			}
		}
		return hmp;
	}
	
	public Matrix getHpm() throws Exception
	{
		if (hpm == null)
		{
			hpm = new Matrix(quadrature.getM());
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hpm.setItemValue(i, j, getRedistributionValue(qp_i.getV(), - qp_j.getV()));
					j++;
				}
				i++;
			}
		}
		return hpm;
	}
	
	public Matrix getHpp() throws Exception
	{
		if (hpp == null)
		{
			hpp = new Matrix(quadrature.getM());
			int i = 0, j;
			for (Quadrature.QP qp_i : quadrature.getQuadraturePoints())
			{
				j = 0;
				for (Quadrature.QP qp_j : quadrature.getQuadraturePoints())
				{
					hpp.setItemValue(i, j, getRedistributionValue(qp_i.getV(), qp_j.getV()));
					j++;
				}
				i++;
			}
		}
		return hpp;
	}
	
	private double getRedistributionValue(double v_i, double v_j)
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
	private Matrix hmm, hmp, hpm, hpp;
}
