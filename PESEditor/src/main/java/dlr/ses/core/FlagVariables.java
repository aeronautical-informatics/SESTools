package dlr.ses.core;

/**
 * <h1>FlagVariables</h1>
 * <p>
 * This class contains those variables which are used for counting. For storing
 * these variables value as an object this FlagVariable class is used. So when
 * the saved project will be opened then the count will start from the
 * previously saved number.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */

public class FlagVariables implements java.io.Serializable {

	public int nodeNumber;
	public int uniformityNodeNumber;

}
