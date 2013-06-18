package ad.math.matrix;

import ad.Config;

/**
 *
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public abstract class BaseMatrix
{
	
	public int getColsNumber()
	{
		return this.cols;
	}
	
	public int getRowsNumber()
	{
		return this.rows;
	}
	
	public void setItemValue(int row, int coll, double value)
	{
		this.items[row][coll] = value;
	}
	
	public double getItemValue(int row, int coll)
	{
		return this.items[row][coll];
	}
	
	public double[][] getItems()
	{
		return this.items;
	}
	
	public String toString()
	{
		String result = "";
		for (int i = 0; i < getRowsNumber(); i++)
		{
			for (int j = 0; j < getColsNumber(); j++)
			{
				result += String.format(Config.FLOAT_FORMAT, getItemValue(i, j));
			}
			result += "\n";
		}
		return result;
	}
	
	protected int rows = 0;
	protected int cols = 0;
	protected double[][] items;
}
