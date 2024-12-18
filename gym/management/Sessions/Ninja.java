package gym.management.Sessions;

import gym.customers.Client;
import gym.management.Instructor;

public class Ninja extends Session {
    public Ninja(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        super(sessionType, data, forumType, instructor);
    }

    @Override
    public int getPrice() {
        return 150;
    }

    @Override
    public int getMaxCapacity() {
        return 5;
    }

    @Override
    public String toString() {
        return "Ninja{}";
    }
}
