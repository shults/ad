package ad;

import ad.math.Quadrature;
import ad.math.matrix.Matrix;
import ad.math.matrix.Row;

/**
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public class BoundaryLayer
{

	/**
	 * 
	 * @param nslab
	 * @param quadrature Quadrature
	 * @throws Exception 
	 */
	public BoundaryLayer(double nslab, Quadrature quadrature) throws Exception
	{
		this.nslab = nslab;
		this.criticalAngle = Math.asin(1.0 / nslab);
		this.qPointsAngles = quadrature.getQuadraturePointsAngles();
		this.qPointsWeight = quadrature.getQuadraturePointsWeights();
		initMatrices();
	}
	
	private void initMatrices()
	{
		double angle;
		int matrixSize = qPointsAngles.getSize();
		reflectionMatrix = new Matrix(matrixSize);
		transmissionMatrix = new Matrix(matrixSize);
		for (int i = 0; i < qPointsAngles.getSize(); i++)
		{
			angle = Math.acos(qPointsAngles.getItemValue(i));
			reflectionMatrix.setItemValue(i, i, 2 * qPointsAngles.getItemValue(i) * qPointsWeight.getItemValue(i) * getReflection(angle));
			transmissionMatrix.setItemValue(i, i, 2 * qPointsAngles.getItemValue(i) * qPointsWeight.getItemValue(i) * (1 - getReflection(angle)));
		}
	}
	
	private double getReflection(double angleBeta)
	{
		double returnValue = 0;
		if (angleBeta == 0)
		{
			returnValue = Math.pow((1.0 - nslab) / (1.0 + nslab), 2);
		}
		else if (angleBeta > criticalAngle)
		{
			returnValue = 1;
		} 
		else
		{
			double angleAlpha = Math.asin(nslab * Math.sin(angleBeta));
			returnValue = 0.5 * 
					((Math.pow(Math.sin(angleAlpha - angleBeta), 2) / Math.pow(Math.signum(angleAlpha + angleBeta), 2)) 
					+ (Math.pow(Math.tan(angleAlpha - angleBeta), 2) / Math.pow(Math.tan(angleAlpha + angleBeta), 2)));
		}
		return returnValue;
	}
	
	public Matrix getReflectionMatrix()
	{
		return reflectionMatrix;
	}
	
	public Matrix getTransmissionMatrix()
	{
		return transmissionMatrix;
	}
	
	public Matrix getInvercedTransmissionMatrix()
	{
		Matrix tm = getTransmissionMatrix();
		Matrix im = new Matrix(tm.getColsNumber());
		for (int i = 0; i < im.getRowsNumber(); i++)
		{
			for (int j = 0; j < im.getRowsNumber(); j++)
			{
				im.setItemValue(i, j, tm.getItemValue(i, j) * Math.pow((1.0 / nslab), 4));
			}
		}
		return im;
	}
	
	private double criticalAngle;
	private double nslab;
	private Row qPointsWeight;
	private Row qPointsAngles;
	private Matrix reflectionMatrix;
	private Matrix transmissionMatrix;
}
