package gym.Exception;

import gym.customers.Client;

public class DuplicateClientException extends RuntimeException {

    private Client client;

    public DuplicateClientException(String message, Client client) {
        super(message);
        this.client = client;
    }
}
