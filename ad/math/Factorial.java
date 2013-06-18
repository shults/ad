package ad.math;

/**
 * This class implemets determination of the factorial
 *
 * @version 0.1
 * @author Yaroslav Kotsur
 */
public class Factorial
{
	/**
	 * This method denies object construction of current class
	 */
	private Factorial(){}
	
	/**
	 * 
	 * @param x factorial attribute
	 * @return factorial value x!
	 */
    public static int get(byte x)
    {
        if (x <= 0) {
            return 1;
        } else {
            return x * Factorial.get((byte)(x - 1));
        }
    }
}
