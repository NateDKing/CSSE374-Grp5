package umlparser;

//Generated with ChatGPT, for purposes for testing.

public class Logger {
    private static Logger instance;
    
    // Private constructor prevents external instantiation.
    private Logger() {}
    
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public static void log(String message) {
        System.out.println("LOG: " + message);
    }
}
