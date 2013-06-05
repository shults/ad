package ad.math.matrix;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shults
 */
public class Matrix extends BaseMatrix
{
	
	/**
	 * Use this constructor only for 
	 * matrices with size NxN dimension 
	 * N - number of colls
	 * N - number of rows
	 * 
	 * @param size Size of square matrix
	 */
	public Matrix(int size)
	{
		this(size, size);
	}
	
	public Matrix(Row... rows)
	{
		int maxRowSize = 1;
		for (Row row : rows)
			maxRowSize = maxRowSize < row.getSize() ? row.getSize() : maxRowSize;
		initItems(rows.length, maxRowSize);
		for (int i = 0; i < rows.length; i++)
		{
			for (int j = 0; j < rows[i].getSize(); j++)
			{
				setItemValue(i, j, rows[i].getItemValue(j));
			}
		}
	}
	
	/**
	 * Use this constructor for creating matrices with size MxN
	 * M - number of rows
	 * N - number of cols
	 * 
	 * @param rows number of rows
	 * @param cols numBer of cals
	 */
	public Matrix(int rows, int cols)
	{
		initItems(rows, cols);
	}
	
	public Matrix(int size, String type)
	{
		this(size);
		switch (type)
		{
			case MATRIX_TYPE_IDENTITY:
				for (int i = 0; i < getRowsNumber(); i++)
					setItemValue(i, i, 1);
			break;
		}
	}
	
	private void initItems(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		this.items = new double[rows][cols];
	}
	
	public Matrix multiply(double constant) throws Exception
	{
		Matrix resultMatrix = new Matrix(getRowsNumber(), getColsNumber());
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				resultMatrix.setItemValue(i, j, constant * getItemValue(i, j));
			}
		}
		return resultMatrix;
	}
	
	public Matrix multiply(Matrix matrix) throws Exception
	{
		return multiplyRight(matrix);
	}
	
	public Matrix multiplyRight(Matrix matrix) throws Exception
	{
		int m,n,q,i,j,r;
		double summ = 0;
		Matrix rm;
		if (this.getColsNumber() != matrix.getRowsNumber())
			throw new Exception("Number of rows and cols are mismatched");
		m = getRowsNumber();
		n = getColsNumber();
		q = matrix.getColsNumber();
		rm = new Matrix(m, q);
		for (i = 0; i < m; i++)
		{
			for (j = 0; j < q; j++)
			{
				summ = 0;
				for (r = 0; r < n; r++)
					summ += getItemValue(i, r) * matrix.getItemValue(r, j);
				rm.setItemValue(i, j, summ);
			}
		}
		return rm;
	}
	
	public Matrix multiplyLeft(Matrix matrix)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Matrix add(Matrix matrix) throws Exception
	{
		if (getRowsNumber() != matrix.getRowsNumber() || getColsNumber() != matrix.getColsNumber())
			throw new Exception("Number of cols or rows mismatched");
		Matrix resultMatrix = new Matrix(getRowsNumber(), getColsNumber());
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				resultMatrix.setItemValue(i, j, getItemValue(i, j) + matrix.getItemValue(i, j));
			}
		}
		return resultMatrix;
	}
	
	public boolean equals(Matrix matrix)
	{
		return false;
	}
	
	/**
	 * This method return inverce matrix
	 * for example we have matrix [A]
	 * this method will return matrix [-1 x A]
	 * @return inverce matrix -A
	 */
	public Matrix getInverceMatrix()
	{
		Matrix inverceMatrix = new Matrix(getRowsNumber(), getColsNumber());
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				inverceMatrix.setItemValue(i, j, - getItemValue(i, j));
			}
		}
		return inverceMatrix;
	}
	
	/**
	 * This method returns invertible matrix
	 * If we have matrix A. This method will return matrix A^-1
	 * A * A^-1 = E
	 * A - current matrix
	 * A^-1 - invertible matrix
	 * E = identitty matrix
	 * 
	 * @return InvertibleMatrix
	 */
	public Matrix getInvertibleMatrix() throws Exception
	{
		if (!isSquare())
			throw new Exception("Invertible matrix cannot be calculated. Matrix is not square.");
		Matrix gaussMatrix = new Matrix(getRowsNumber(), 2 * getColsNumber());
		Matrix invertibleMatrix = new Matrix(getRowsNumber(), getColsNumber());
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				gaussMatrix.setItemValue(i, j, getItemValue(i, j));
				if (i == j)
					gaussMatrix.setItemValue(i, j + getColsNumber(), 1);
				else
					gaussMatrix.setItemValue(i, j + getColsNumber(), 0);
			}
		}
		
		for (int j = 0; j < gaussMatrix.getColsNumber() / 2; j ++)
		{
			gaussMatrix.multiplyRow(j, 1 / gaussMatrix.getItemValue(j, j));
			for (int i = j + 1; i < getRowsNumber(); i++)
				gaussMatrix.addToRow(i, gaussMatrix.getRow(j).multiply(-gaussMatrix.getItemValue(i, j))); // * gaussMatrix.getItemValue(j, j)
		}
		
		for (int j = gaussMatrix.getColsNumber() / 2 - 1; j > 0; j--)
		{
			for (int i = j - 1; i >= 0; i--)
				gaussMatrix.addToRow(i, gaussMatrix.getRow(j).multiply(-gaussMatrix.getRow(i).getItemValue(j))); // * gaussMatrix.getItemValue(j, j)
		}
		
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
				invertibleMatrix.setItemValue(i, j, gaussMatrix.getItemValue(i, j + getColsNumber()));
		}
		
		return invertibleMatrix;
	}
	
	public Row getRow(int i) throws Exception
	{
		Row returnRow = new Row(getColsNumber());
		for (int j = 0; j < getColsNumber(); j++)
			returnRow.setItemValue(j, getItemValue(i, j));
		return returnRow;
	}
	
	private void addToRow(int i, Row row)
	{
		for (int j = 0; j < getColsNumber(); j++)
			setItemValue(i, j, getItemValue(i, j) + row.getItemValue(j));
	}
	
	private void multiplyRow(int row, double multiplier) throws Exception
	{
		if (row > getRowsNumber() - 1)
			throw new Exception("Thera are no row with index " + row);
		for (int j = 0; j < getColsNumber(); j++)
			setItemValue(row, j, multiplier * getItemValue(row, j));
	}
	
	private void multiplyColl(int coll, double multiplier) throws Exception
	{
		if (coll > getRowsNumber() - 1)
			throw new Exception("Thera are no coll with index " + coll);
		for (int i = 0; i < getRowsNumber(); i++)
			setItemValue(i, coll, multiplier * getItemValue(i, coll));
	}
	
	public double det() throws Exception
	{
		double returnValue = 0;
		Matrix currentMatrix, nextMatrix; 
		if (!isSquare())
			throw new Exception("Determinant cannot be calculated. Matrix is not square.");
		if (getItemValue(0, 0) == 0.0 && getRowsNumber() != 1)
			throw new Exception("Determinant cannot be calculated");
		if (getRowsNumber() == 1)
		{
			returnValue = getItemValue(0, 0);
		}
		else 
		{
			currentMatrix = clone();
			double multiplier = currentMatrix.getItemValue(0, 0);
			for (int i = 0; i < currentMatrix.getRowsNumber(); i++)
			{
				if (i == 0)
				{
					currentMatrix.setItemValue(i, i, 1.0);
				}
				else 
				{
					currentMatrix.setItemValue(i, 0, currentMatrix.getItemValue(i, 0) / multiplier);
				}
			}
			
			for (int j = 1; j < currentMatrix.getColsNumber(); j++)
			{
				for (int i = 1; i < currentMatrix.getRowsNumber(); i++)
				{
					currentMatrix.setItemValue(i, j, currentMatrix.getItemValue(i, j) 
							- currentMatrix.getItemValue(0, j) * currentMatrix.getItemValue(i, 0));
				}
			}
			
			nextMatrix = new Matrix(currentMatrix.getRowsNumber() - 1, currentMatrix.getColsNumber() - 1);
			for (int i = 1; i < currentMatrix.getRowsNumber(); i++)
			{
				for (int j = 1; j < currentMatrix.getColsNumber(); j++)
				{
					nextMatrix.setItemValue(i - 1, j - 1, currentMatrix.getItemValue(i, j));
				}
			}
			returnValue = multiplier * nextMatrix.det();
		}
			
		return returnValue;
	}
	
	@Override
	public Matrix clone()
	{
		Matrix clonedMatrix = new Matrix(getRowsNumber(), getColsNumber());
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				clonedMatrix.setItemValue(i, j, getItemValue(i, j));
			}
		}
		return clonedMatrix;
	}
	
	public boolean isSquare()
	{
		return getColsNumber() == getRowsNumber();
	}
	
	public static final String MATRIX_TYPE_IDENTITY = "identyty";
}
