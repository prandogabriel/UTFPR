package log;

public class Logger extends java.util.logging.Logger{
	public final static Logger logger = new Logger("General Log", null);

	protected Logger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
	
	public static void log(String log){
		System.out.print(log);
	}
	
	public static void logln(String log){
		System.out.println(log);
	}

}
