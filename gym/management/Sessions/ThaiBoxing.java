package gym.management.Sessions;

import gym.customers.Client;
import gym.management.Instructor;

public class ThaiBoxing extends Session {
    public ThaiBoxing(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        super(sessionType, data, forumType, instructor);
    }

    @Override
    public int getPrice() {
        return 100;
    }

    @Override
    public int getMaxCapacity() {
        return 20;
    }

    @Override
    public String toString() {
        return "ThaiBoxing{}";
    }
}
