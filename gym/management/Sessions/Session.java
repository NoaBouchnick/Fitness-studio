package gym.management.Sessions;

import gym.customers.*;
import gym.management.*;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private SessionType sessionType;
    private String data;
    private ForumType forumType;
    private Instructor instructor;
    private List<Client> lientsInSession;


    public Session(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        this.sessionType = sessionType;
        this.data = data;
        this.forumType = forumType;
        this.instructor = instructor;
        this.lientsInSession = new ArrayList<>();
    }


    public SessionType getSessionType() {
        return sessionType;
    }

    public List<Client> getLientsInSession() {
        return lientsInSession;
    }

    public boolean isFull() {
        return lientsInSession.size() >= sessionType.getMaxCapacity();
    }
    public boolean addClient(Client client) {
        if (!isFull()) {
            lientsInSession.add(client);
            return true;
        }
        return false;
    }
    public int getPrice() {
        return sessionType.getPrice();
    }
}
