package common.toolkit.java.util;

public class ThreadUtil {

	public static void sleep( long millis ){
		try {
			Thread.sleep( millis );
		} catch ( Throwable e ) {
			e.printStackTrace();
		}
	}
}
