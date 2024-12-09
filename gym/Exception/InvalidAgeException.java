package gym.Exception;

public class InvalidAgeException extends Exception {

    private int invalidAge;

    public InvalidAgeException(String message, int age) {
        super(message);
        this.invalidAge = age;
    }

    public int getInvalidAge() {
        return invalidAge;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Age: " + invalidAge;
    }
}
