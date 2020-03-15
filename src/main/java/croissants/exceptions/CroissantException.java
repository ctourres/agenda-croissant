package croissants.exceptions;

public class CroissantException extends Exception {

    /**
     * Create a HTTP 500 exception.
     * @param message the String that is the entity of the 500 response.
     */
    public CroissantException(String message) {
        super(message);
    }
}
