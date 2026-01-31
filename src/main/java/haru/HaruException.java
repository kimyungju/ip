package haru;

/**
 * Represents exceptions specific to the Haru application.
 */
public class HaruException extends Exception {
    /**
     * Constructs a new HaruException with the specified message.
     *
     * @param message The error message.
     */
    public HaruException(String message) {
        super(message);
    }
}
