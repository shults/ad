package ad;
import ad.math.Quadrature;
import ad.math.matrix.Row;
import ad.math.matrix.Matrix;

/**
 *
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public class StartLayer
{ 
	/**
	 * 
	 * @param M number of quadrature points
	 * @param a albedo
	 * @param g anisotropy factor
	 * @param t aptical thickness
	 * @param quadrature quadrature
	 * @param hpm redistribution matrix h+-
	 * @param hpp redistributeion matrix h++
	 * @throws Exception 
	 */
	public StartLayer(int M, double a, double g, double t, Quadrature quadrature, Matrix hpm, Matrix hpp) throws Exception
	{
		this.M = M;
		this.a = a;
		this.g = g;
		this.t = t;
		this.quadrature = quadrature;
		this.hpm = hpm;
		this.hpp = hpp;
		initDelataTStar();
		initTransmissionMatrix();
		initReflectionMatrix();
	}
	
	private void initDelataTStar() throws Exception
	{
		double TStar = getTStar();
		double deltaTStar = TStar;
		double minQuadratureAngle = quadrature.getQuadraturePointsAngles().getMinValue();
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
			c = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
			Row qPointsWeight = quadrature.getQuadraturePointsWeights();
			for (int i = 0; i < c.getRowsNumber(); i++)
				c.setItemValue(i, i, qPointsWeight.getItemValue(i));
		}
		return c;
	}
	
	public Matrix getMatrixB() throws Exception
	{
		if (matrixB == null)
		{
			matrixB = getInverselyVMatrix()
						.multiply(hpm)
						.multiply(getC())
						.multiply(0.25 * getAStar() * getDeltaTStar());
		}
		return matrixB;		
	}
	
	public Matrix getMatrixA() throws Exception
	{
		if (matrixA == null)
		{
			Matrix identityMatrix = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
			matrixA = getInverselyVMatrix()
						.multiply(identityMatrix.add(hpp.multiply(getC().multiply(-0.5 * getAStar()))													 ))
						.multiply(0.5 * getDeltaTStar());
		}
		return matrixA;
	}
	
	public Matrix getReflectionMatrix() throws Exception
	{
		if (reflectionMatrix == null)
		{
			initReflectionMatrix();
		}
		return reflectionMatrix;
	}
	
	private void initReflectionMatrix() throws Exception
	{
		Matrix IM = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
		reflectionMatrix = getMatrixG().multiply(2)
							.multiply(getMatrixB())
							.multiply(IM.add(getMatrixA()).getInvertibleMatrix())
							.multiply(getInversely2VWMatrix());
	}
	
	public Matrix getTransmissionMatrix() throws Exception
	{
		if (transmissionMatrix == null)
		{
			initTransmissionMatrix();
		}
		return transmissionMatrix;
	}
	
	public void initTransmissionMatrix() throws Exception
	{
		Matrix identityMatrix = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
		transmissionMatrix = getMatrixG().multiply(2).add(identityMatrix.multiply(-1)).multiply(getInversely2VWMatrix());
	}
	
	public Matrix getMatrixG() throws Exception
	{
		if (matrixG == null)
		{
		Matrix identityMatrix = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
		matrixG = (identityMatrix.add(getMatrixA())
					      .add(getMatrixB()
						       .multiply(identityMatrix.add(getMatrixA()).getInvertibleMatrix())
						       .multiply(getMatrixB()))).getInvertibleMatrix();
		}
		return matrixG;
	}
	
	public Matrix getInverselyVMatrix() throws Exception
	{
		if (InverselyVMatrix == null)
		{
			InverselyVMatrix = new Matrix(M);
			Row quadratureAngles = quadrature.getQuadraturePointsAngles();
			for (int i = 0; i < InverselyVMatrix.getRowsNumber(); i++)
				InverselyVMatrix.setItemValue(i, i, 1 / quadratureAngles.getItemValue(i));
		}
		return InverselyVMatrix;
	}
	
	//public Matrix getB
	private double getDeltaTStar()
	{
		return deltaTStar;
	}
	
	private double getTStar() throws Exception
	{
		return (1 - a * Math.pow(g, M)) * t;
	}
	
	public double getAStar() throws Exception
	{
		return a * (1 - Math.pow(g, M)) / (1 - a * Math.pow(g, M));
	}
	
	private Matrix getInversely2VWMatrix() throws Exception
	{
		if (Inversely2VW == null)
		{
			Inversely2VW = new Matrix(M);
			Row qPointsWeight = quadrature.getQuadraturePointsWeights();
			Row qPointsAngles = quadrature.getQuadraturePointsAngles();
			for (int i = 0; i < M; i++)
			{
				Inversely2VW.setItemValue(i, i, 1.0 / (2 * qPointsWeight.getItemValue(i) * qPointsAngles.getItemValue(i)));
			}
		}
		return Inversely2VW;
	}
	
	public void runDoubling() throws Exception
	{
		runDoubling(false);
	}
	
	public void runDoubling(boolean detail) throws Exception
	{
		Matrix newReflectionMatrix, newTransmissionMatrix;
		if (doublingOccurred)
			throw new Exception("Doubling had bean occurred. You cannot twice run doubling.");
		for (int i = 0; i < iterationsNumber; i++)
		{
			newReflectionMatrix = getTransmissionMatrix().multiply(
					getInversely2VWMatrix().add(
						getReflectionMatrix().multiply(getInversely2VWMatrix().getInvertibleMatrix()).multiply(getReflectionMatrix()).multiply(-1)
					).getInvertibleMatrix().multiply(getReflectionMatrix().multiply(getInversely2VWMatrix().getInvertibleMatrix()).multiply(getTransmissionMatrix()))
					).add(getReflectionMatrix());
			
			newTransmissionMatrix = getTransmissionMatrix()
									.multiply(
										getInversely2VWMatrix().add(
											getReflectionMatrix()
											.multiply(getInversely2VWMatrix().getInvertibleMatrix())
											.multiply(getReflectionMatrix())
										).getInvertibleMatrix()
									).multiply(getTransmissionMatrix());
			
			transmissionMatrix = newTransmissionMatrix;
			reflectionMatrix = newReflectionMatrix;
			
			if (detail) 
			{
				System.out.println("Reflection matrix after doubling #" + (i + 1));
				System.out.println(reflectionMatrix);
				
				System.out.println("Transmission matrix after doubling #" + (i + 1));
				System.out.println(transmissionMatrix);
			}
		}
		doublingOccurred = true;
	}
	
	public void addBoundaryCondition(BoundaryLayer boundaryLayer) throws Exception
	{
		
		Matrix R01, R10, T01, T10, T12, T21, R21,  R12, R20, T02, R30, T03, E, EI;
		
		
		R10 = R01 = boundaryLayer.getReflectionMatrix().multiply(getInversely2VWMatrix());
		T10 = T01 = boundaryLayer.getTransmissionMatrix().multiply(getInversely2VWMatrix());
		
		R21 = R12 = getReflectionMatrix().clone();
		T21 = T12 = getTransmissionMatrix().clone();
		
		E = new Matrix(M, Matrix.MATRIX_TYPE_IDENTITY);
		EI = getInversely2VWMatrix().getInvertibleMatrix();
		
		T02 = T12.multiply(E.add(R10.multiply(EI).multiply(R12).multiply(-1)).getInvertibleMatrix())
				.multiply(T01);
		
		R20 = T12.multiply(E.add(R10.multiply(EI).multiply(R12).multiply(-1)).getInvertibleMatrix())
				.multiply(R10)
				.multiply(EI)
				.multiply(T21)
				.add(R21);
		
		T03 = T10.multiply(E.add(R20.multiply(EI).multiply(R10).multiply(-1)).getInvertibleMatrix())
				.multiply(T02);
		
		R30 = T10.multiply(E.add(R20.multiply(EI).multiply(R10).multiply(-1)).getInvertibleMatrix())
				.multiply(R20)
				.multiply(EI)
				.multiply(T01)
				.add(R01);
		
		this.transmissionMatrix = T03;
		this.reflectionMatrix = R30.multiply(getInversely2VWMatrix());
		
	}
	
	public double getColimatedTransmission() throws Exception
	{
		if (!doublingOccurred)
			throw new Exception("Cannot calculate colimated transmision. Doubling had not heppend.");
		Matrix tm = getTransmissionMatrix();
		double tc = 0;
		Row qPw = quadrature.getQuadraturePointsWeights();
		Row qPv = quadrature.getQuadraturePointsAngles();
		for (int i = 0; i < M; i++)
			tc += 2 * qPv.getItemValue(i) * qPw.getItemValue(i) * tm.getItemValue(i, tm.getColsNumber() - 1);
		return tc;
	}
	
	public double getColimatedReflection() throws Exception
	{
		if (!doublingOccurred)
			throw new Exception("Cannot calculate colimated reflection. Doubling had not heppend.");
		Matrix rm = getReflectionMatrix();
		double tr = 0;
		Row qPw = quadrature.getQuadraturePointsWeights();
		Row qPv = quadrature.getQuadraturePointsAngles();
		for (int i = 0; i < M; i++)
			tr += 2 * qPv.getItemValue(i) * qPw.getItemValue(i) * rm.getItemValue(i, rm.getColsNumber() - 1);
		return tr;
	}
	
	public double getDiffuseTransmission() throws Exception
	{
		if (!doublingOccurred)
			throw new Exception("Cannot calculate colimated transmision. Doubling had not heppend.");
		Matrix tm = getTransmissionMatrix();
		double dt = 0;
		Row qPw = quadrature.getQuadraturePointsWeights();
		Row qPv = quadrature.getQuadraturePointsAngles();
		for (int i = 0; i < M; i++)
			for (int j = 0; j < M; j++)
				dt += qPv.getItemValue(j) * qPw.getItemValue(j) * tm.getItemValue(j, j);
		return dt;
	}
	
	public double getDiffuseReflection() throws Exception
	{
		if (!doublingOccurred)
			throw new Exception("Cannot calculate colimated transmision. Doubling had not heppend.");
		Matrix rm = getReflectionMatrix();
		double tr = 0;
		Row qPw = quadrature.getQuadraturePointsWeights();
		Row qPv = quadrature.getQuadraturePointsAngles();
		for (int i = 0; i < M; i++)
		{
			for (int j = 0; j < M; j++)
			{
				tr += 2 * qPv.getItemValue(j) * qPw.getItemValue(j) * rm.getItemValue(j, j);
			}
		}
		return tr;
	}
	
	private int M;
	private double a;
	private double g;
	private double t;
	private Quadrature quadrature;
	private Matrix hpm;
	private Matrix hpp;
	
	private Matrix c;
	private double deltaTStar;
	private int iterationsNumber;
	
	private Matrix InverselyVMatrix;
	private Matrix Inversely2VW;
	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix matrixG;
	
	private Matrix reflectionMatrix;
	private Matrix transmissionMatrix;
	private boolean doublingOccurred = false;
}
