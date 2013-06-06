/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.math.matrix;

/**
 *
 * @author shults
 */
public abstract class Vector
{

	public double getItemValue(int i)
	{
		return this.items[i];
	}

	public void setItemValue(int i, double value) throws Exception
	{
		if (i > this.items.length - 1) {
			throw new Exception("Row/Coll size is less than" + (i + 1));
		}
		this.items[i] = value;
	}

	public double[] getItems()
	{
		return this.items;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	protected void setSize(int size)
	{
		this.size = size;
	}
	
	@Override
	public String toString()
	{
		String returnValue = new String();
		for (int i = 0; i < getSize(); i++)
			returnValue += String.format("%,12.9f", getItemValue(i));
		return returnValue;
	}
	
	public double getMinValue()
	{
		double minValue = items[0];
		for (int i = 0; i < items.length; i++)
			minValue = minValue > items[i] ? items[i] : minValue;
		return minValue;
	}
	
	public double getMaxValue()
	{
		double maxValue = items[0];
		for (int i = 0; i < items.length; i++)
			maxValue = maxValue < items[i] ? items[i] : maxValue;
		return maxValue;
	}
	
	public abstract Vector multiply(double multiplier) throws Exception;
	
	protected double[] items;
	private int size;
}
