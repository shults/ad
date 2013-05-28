package ad;

/**
 *
 * @author shults
 */
public class Ad
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try {
            Application.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Logger.printMessages();
    }
}
