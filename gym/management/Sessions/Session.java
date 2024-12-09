package gym.management.Sessions;

import gym.customers.Client;
import gym.management.ForumType;
import gym.management.Instructor;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private SessionType sessionType;
    private String dueData;
    private ForumType forumType;
    private Instructor instructor;
    private List<Client> registeredClients;
    private int maxCapacity;


    public Session(SessionType sessionType, String dueData, ForumType forumType, Instructor instructor, int maxCapacity) {
        this.sessionType = sessionType;
        this.dueData = dueData;
        this.forumType = forumType;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity; // הגדרת קיבולת מקסימלית
        this.registeredClients = new ArrayList<>();
    }


    public SessionType getSessionType() {
        return sessionType;
    }

    public String getDueData() {
        return dueData;
    }

    public ForumType getForumType() {
        return forumType;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    public boolean isFull() {
        return registeredClients.size() >= maxCapacity;
    }
    public boolean addClient(Client client) {
        if (!isFull()) {
            registeredClients.add(client);
            return true;
        }
        return false;
    }

}
