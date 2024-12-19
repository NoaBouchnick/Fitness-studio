package gym.management.Sessions;

import gym.customers.*;
import gym.management.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Session {

    private final SessionType sessionType;
    private final LocalDateTime sessionDate; // changed from String to LocalDateTime
    private final ForumType forumType;
    private final Instructor instructor;
    private final List<Client> clientsInSession;

    public Session(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        this.sessionType = sessionType;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        this.sessionDate = LocalDateTime.parse(data, formatter); // converting String to LocalDateTime
        this.forumType = forumType;
        this.instructor = instructor;
        this.clientsInSession = new ArrayList<>();
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public ForumType getForumType() {
        return forumType;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public List<Client> getClientsInSession() {
        return clientsInSession;
    }

    public abstract int getPrice();

    public abstract int getMaxCapacity();

    public boolean isFull() {
        return getClientsInSession().size() >= getMaxCapacity();
    }

    public void addClient(Client client) {
        if (!isFull()) {
            getClientsInSession().add(client);
        }
    }

    public Instructor getInstructor() {
        return instructor;
    }
}
