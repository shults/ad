package ad.math;

/**
 *
 * @author shults
 */
public class Matrix
{
	
	public Matrix(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		this.items = new double[rows][cols];
	}
	
	public int getCols()
	{
		return this.cols;
	}
	
	public int getRows()
	{
		return this.rows;
	}
	
	public void set(int row, int coll, double value)
	{
		this.items[row][coll] = value;
	}
	
	public double get(int row, int coll)
	{
		return this.items[row][coll];
	}
	
	public double[][] getItems()
	{
		return this.items;
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
		if (this.getCols() != matrix.getRows())
			throw new Exception("Number of rows and cols are mismatched");
		m = this.getRows();
		n = this.getCols();
		q = matrix.getCols();
		rm = new Matrix(m, q);
		for (i = 0; i < m; i++)
		{
			for (j = 0; j < q; j++)
			{
				summ = 0;
				for (r = 0; r < n; r++)
					summ += this.get(i, r) * matrix.get(r, j);
				rm.set(i, j, summ);
			}
		}
		return rm;
	}
	
	public Matrix multipluLeft(Matrix matrix)
	{
		return null;
	}
	
	private int rows = 0;
	private int cols = 0;
	private double[][] items;
}
