/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.math.matrix;

/**
 *
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public class Coll extends Vector
{
	public Coll(int size) throws Exception
	{
		if (size < 1)
			throw new Exception("Coll size cannot be less than one");
		this.setSize(size);
		this.items = new double[size];
	}

	public Coll(double ... items)
	{
		this.setSize(items.length);
		this.items = new double[items.length];
		for (int i = 0; i < items.length; i++)
			this.items[i] = items[i];
	}
	
	public Coll multiply(double multiplier) throws Exception
	{
		Coll coll = new Coll(getSize());
		for (int i = 0; i < getSize(); i++)
			coll.setItemValue(i, multiplier * getItemValue(i));
		return coll;
	}
	
}