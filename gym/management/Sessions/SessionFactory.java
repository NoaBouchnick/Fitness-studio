package gym.management.Sessions;

import gym.management.Instructor;

import java.util.HashMap;
import java.util.Map;

public class SessionFactory {

    private static final Map<SessionType, Integer> sessionCounters = new HashMap<>();

    public static Session createSession(SessionType sessionType, String data, ForumType forumType, Instructor instructor) {
        incrementSessionCount(sessionType);

        return switch (sessionType) {
            case Pilates -> new Pilates(sessionType, data, forumType, instructor);
            case ThaiBoxing -> new ThaiBoxing(sessionType, data, forumType, instructor);
            case MachinePilates -> new MachinePilates(sessionType, data, forumType, instructor);
            case Ninja -> new Ninja(sessionType, data, forumType, instructor);
            default -> throw new IllegalArgumentException("Unknown session type: " + sessionType);
        };
    }

    private static void incrementSessionCount(SessionType sessionType) {
        sessionCounters.put(sessionType, sessionCounters.getOrDefault(sessionType, 0) + 1);
    }
}
