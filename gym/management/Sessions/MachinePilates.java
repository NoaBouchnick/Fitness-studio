package gym.management.Sessions;

import gym.customers.Client;
import gym.management.Instructor;

public class MachinePilates extends Session {

    public MachinePilates(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        super(sessionType, data, forumType, instructor);
    }

    @Override
    public int getPrice() {
        return 80;
    }

    @Override
    public int getMaxCapacity() {
        return 10;
    }

    @Override
    public String toString() {
        return "MachinePilates{}";
    }
}
