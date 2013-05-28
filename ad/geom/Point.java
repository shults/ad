package ad.geom;

/**
 *
 * @author shults
 */
public class Point
{
    public Point(){}
    
    public void setX(double x)
    {
        _x = x;
    }
    
    public void setY(double y)
    {
        _y = y;
    }
    
    public double getX()
    {
        return _x;
    }
    
    public double getY()
    {
        return _y;
    }
    
    private double _x = 0;
    private double _y = 0;
}
