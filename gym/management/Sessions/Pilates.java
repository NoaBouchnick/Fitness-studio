package gym.management.Sessions;

import gym.customers.Client;
import gym.management.Instructor;

public class Pilates extends Session {
    public Pilates(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        super(sessionType, data, forumType, instructor);
    }

    @Override
    public int getPrice() {
        return 60;
    }

    @Override
    public int getMaxCapacity() {
        return 30;
    }

    @Override
    public String toString() {
        return "Pilates{}";
    }
}
