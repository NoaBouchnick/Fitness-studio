package gym.management;

import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends Person {
    private List<SessionType> instructorSessions = new ArrayList<>();


    public Instructor(String name, int moneyBalance, Gender gender, String data, List<SessionType> instructorSessions) {
        super(name, moneyBalance, gender, data);
        this.instructorSessions = instructorSessions == null ? new ArrayList<>() : instructorSessions;
    }

    public List<SessionType> getInstructorSessions() {
        return Collections.unmodifiableList(instructorSessions);
    }


    public boolean mayTeach(SessionType requestedSessionType) throws InstructorNotQualifiedException {
        if (!instructorSessions.contains(requestedSessionType)) {
            throw new InstructorNotQualifiedException(
                    "Instructor is not qualified for session: " + requestedSessionType,
                    this, requestedSessionType
            );
        }
        return true;
    }
}
