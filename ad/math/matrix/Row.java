/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.math.matrix;

/**
 *
 * @author shults
 */
public class Row extends Vector
{

	public Row(int size) throws Exception
	{
		if (size < 1)
			throw new Exception("Row size cannot be less than one");
		this.setSize(size);
		this.items = new double[size];
	}

	public Row(double... items)
	{
		this.setSize(items.length);
		this.items = new double[items.length];
		for (int i = 0; i < items.length; i++)
			this.items[i] = items[i];
	}

	public Row multiply(double multiplier) throws Exception
	{
		Row row = new Row(getSize());
		for (int j = 0; j < getSize(); j++)
			row.setItemValue(j, multiplier * getItemValue(j));
		return (Row) row;
	}
}
