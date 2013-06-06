/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;
import ad.math.matrix.Row;
import ad.math.matrix.Matrix;

/**
 *
 * @author shults
 */
public class StartLayer
{ 
	
	public StartLayer() throws Exception
	{
		initDelataTStar();
	}
	
	private void initDelataTStar() throws Exception
	{
		double TStar = getTStar();
		double deltaTStar = TStar;
		double minQuadratureAngle = Application.getInstance().getQuadrature().getQuadraturePointsAngles().getMinValue();
		int i = 0;
		do {
			deltaTStar /= 2;
			i++;
		} while (deltaTStar > minQuadratureAngle);
		this.iterationsNumber = i;
		this.deltaTStar = deltaTStar;
	}
	
	public Matrix getC() throws Exception
	{
		if (c == null)
		{
			c = new Matrix(Application.getInstance().getM(), Matrix.MATRIX_TYPE_IDENTITY);
			Row qPointsWeight = Application.getInstance().getQuadrature().getQuadraturePointsWeights();
			for (int i = 0; i < c.getRowsNumber(); i++)
				c.setItemValue(i, i, qPointsWeight.getItemValue(i));
		}
		return c;
	}
	
	public Matrix getMatrixB() throws Exception
	{
		Matrix bMatrix = getInverselyVMatrix()
						.multiply(Application.getInstance().getRedistribution().getHpm())
						.multiply(getC())
						.multiply(0.25 * getAStar() * getDeltaTStar());
		return bMatrix;		
	}
	
	public Matrix getMatrixA() throws Exception
	{
		Matrix identityMatrix = new Matrix(Application.getInstance().getM(), Matrix.MATRIX_TYPE_IDENTITY);
		Matrix hpp = Application.getInstance().getRedistribution().getHpp();
		Matrix aMatrix = getInverselyVMatrix()
						.multiply(identityMatrix.add(hpp.multiply(getC().multiply(-0.5 * getAStar()))													 ))
						.multiply(0.5 * getDeltaTStar());
		return aMatrix;
	}
	
	public Matrix getReflectionMatrix()
	{
		return null;
	}
	
	public Matrix getTransmissionMatrix() throws Exception
	{
		Matrix identityMatrix = new Matrix(Application.getInstance().getM(), Matrix.MATRIX_TYPE_IDENTITY);
		return getMatrixG().multiply(2).add(identityMatrix.multiply(-1));
	}
	
	public Matrix getMatrixG() throws Exception
	{
		Matrix identityMatrix = new Matrix(Application.getInstance().getM(), Matrix.MATRIX_TYPE_IDENTITY);
		Matrix gMatrix = (identityMatrix.add(getMatrixA())
					      .add(getMatrixB()
						       .multiply(identityMatrix.add(getMatrixA()).getInvertibleMatrix())
						       .multiply(getMatrixB()))).getInvertibleMatrix();
		return gMatrix;
	}
	
	public Matrix getInverselyVMatrix() throws Exception
	{
		Matrix matrix = new Matrix(Application.getInstance().getM());
		Row quadratureAngles = Application.getInstance().getQuadrature().getQuadraturePointsAngles();
		for (int i = 0; i < matrix.getRowsNumber(); i++)
			matrix.setItemValue(i, i, 1 / quadratureAngles.getItemValue(i));
		return matrix;
	}
	
	//public Matrix getB
	private double getDeltaTStar()
	{
		return deltaTStar;
	}
	
	private double getTStar() throws Exception
	{
		int M = Application.getInstance().getM();
		double a = Application.getInstance().getA();
		double g = Application.getInstance().getG();
		double t = Application.getInstance().getT();
		return (1 - a * Math.pow(g, M)) * t;
	}
	
	public double getAStar() throws Exception
	{
		int M = Application.getInstance().getM();
		double a = Application.getInstance().getA();
		double g = Application.getInstance().getG();
		return a * (1 - Math.pow(g, M)) / (1 - a * Math.pow(g, M));
	}
	
	private Matrix c;
	private double deltaTStar;
	private int iterationsNumber;
}
