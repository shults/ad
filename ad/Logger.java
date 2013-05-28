package ad;

import java.util.ArrayList;

/**
 *
 * @author shults
 */
public class Logger
{

    public static void msg(String message)
    {
        messages.add(message);
    }

    public static void printMessages()
    {
        for (String msg : messages) {
            System.out.println(msg);
        }
    }

    /**
     *
     * @return array of added messages
     */
    public static ArrayList<String> getMessages()
    {
        return messages;
    }
    /**
     * List of messages
     */
    private static ArrayList<String> messages = new ArrayList<String>();
}
