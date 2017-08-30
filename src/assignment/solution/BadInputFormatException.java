package assignment.solution;

public class BadInputFormatException extends Exception {

    private static final long serialVersionUID = 8486622037622568007L;

    public BadInputFormatException(String message) {
        super(message);
    }

    public BadInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadInputFormatException(Throwable cause) {
        super(cause);
    }

    public BadInputFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BadInputFormatException() {
    }
}
