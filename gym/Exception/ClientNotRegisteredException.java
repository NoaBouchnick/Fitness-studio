package gym.Exception;

import gym.customers.Client;

public class ClientNotRegisteredException extends RuntimeException {

    private Client client;

    public ClientNotRegisteredException(String message, Client client) {
        super(message);
        this.client = client;
    }
}
