package gym.Exception;

public class InvalidAgeException extends Exception {

    private int invalidAge;

    public InvalidAgeException(String message, int invalidAge) {
        super(message);
        this.invalidAge = invalidAge;
    }
}
