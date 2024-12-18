package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {
    private final int hourlyWage;
    private List<SessionType> instructorSessions = new ArrayList<>();
    private List<Session> sessionsTaught = new ArrayList<>();

    public Instructor(String name, int moneyBalance, Gender gender, String data, List<SessionType> instructorSessions, int hourlyWage) {
        super(name, moneyBalance, gender, data);
        this.instructorSessions = instructorSessions == null ? new ArrayList<>() : instructorSessions;
        this.hourlyWage = hourlyWage;
    }

    @Override
    public boolean isInstructor() {
        return true;
    }

    public int getHourlyWage() {
        return hourlyWage;
    }

    public boolean mayTeach(SessionType requestedSessionType) throws InstructorNotQualifiedException {
        if (!instructorSessions.contains(requestedSessionType)) {
            throw new InstructorNotQualifiedException("Error: Instructor is not qualified to conduct this session type.");
        }
        return true;
    }

    public List<Session> getSessionsTaught() {
        return sessionsTaught;
    }

    public boolean isTeachingAtTime(LocalDateTime sessionTime) {
        for (Session session : sessionsTaught) {
            if (session.getSessionDate().equals(sessionTime)) {
                return true;
            }
        }
        return false;
    }
}
