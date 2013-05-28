package ad.math;

/**
 * This method works with factorial
 *
 * @author shults
 */
public class Factorial
{

    public static int get(byte x)
    {
        if (x <= 0) {
            return 1;
        } else {
            return x * Factorial.get((byte)(x - 1));
        }
    }
}
