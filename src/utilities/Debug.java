/**
 * Created on Sep 19, 2012
 *
 * Project: demo06-ThreadedChatServerExample
 */

package utilities;

/**
 * @author kitty
 *
 */

public class Debug {
	
	//Constants
	//Attributes
	private static int level;

	//Constructors
	/**
	 * User defined constructor
	 * @param level - externally defined debug level
	 */
	public Debug(int level) {
		Debug.level = level;
	}

	//Getter and Setter Methods
	/**
	 * @return the current debug level
	 */
	public static int getLevel() {
		return level;
	}

	/**
	 * @param level - the current debug level
	 */
	public static void setLevel(int level) {
		Debug.level = level;
	}
	
	//Operational Methods
	/**
	 * @param message - the debug output message
	 */
	public static void output(String message) {
		if (level == 1)
		{
			System.out.println(message);
		}
		else // debug == 0
		{
			// do nothing for now
		}
	}
}
