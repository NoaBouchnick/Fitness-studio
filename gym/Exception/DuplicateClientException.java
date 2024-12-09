package gym.Exception;

import gym.customers.Client;

public class DuplicateClientException extends RuntimeException {

    private Client client;

    // בנאי המקבל את הודעת השגיאה ולקוח
    public DuplicateClientException(String message, Client client) {
        super(message);
        this.client = client;
    }

    // מחזיר את הלקוח
    public Client getClient() {
        return client;
    }

    // מתודה שמחזירה את הודעת השגיאה עם פרטי הלקוח
    @Override
    public String getMessage() {
        return super.getMessage() + " for client: " + client.getName();
    }
}
