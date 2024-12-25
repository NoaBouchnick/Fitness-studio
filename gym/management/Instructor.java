package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Instructor extends Person {
    private final int hourlyWage;
    private List<SessionType> instructorSessions = new ArrayList<>();
    private List<Session> sessionsTaught = new ArrayList<>();

    public Instructor(String name, int moneyBalance, Gender gender, String data, List<SessionType> instructorSessions, int hourlyWage) {
        super(name, moneyBalance, gender, data);
        this.instructorSessions = instructorSessions == null ? new ArrayList<>() : instructorSessions;
        this.hourlyWage = hourlyWage;
    }

    public Instructor(Person existingPerson, int hourlyWage, List<SessionType> sessions) {
        super(existingPerson);
        this.hourlyWage = hourlyWage;
        this.instructorSessions = sessions == null ? new ArrayList<>() : sessions;
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

    @Override
    public String toString() {
        String certifiedClasses = instructorSessions.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        return "ID: " + getId() +
                " | Name: " + getName() +
                " | Gender: " + getGender() +
                " | Birthday: " + getData() +
                " | Age: " + getAge() +
                " | Balance: " + getMoneyBalance() +
                " | Role: Instructor" +
                " | Salary per Hour: " + getHourlyWage() +
                " | Certified Classes: " + certifiedClasses;
    }
}
