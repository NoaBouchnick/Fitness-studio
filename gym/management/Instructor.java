package gym.management;

import gym.Exception.InstructorNotQualifiedException;
import gym.customers.Person;
import gym.customers.Gender;
import gym.management.Sessions.SessionType;

import java.util.ArrayList;

public class Instructor extends Person {
    private ArrayList<SessionType> sessions;

    public Instructor(String name, int financialBalance, Gender gender, String data, ArrayList<SessionType> sessions) {
        super(name, financialBalance, gender, data);
        this.sessions = sessions;
    }

    public ArrayList<SessionType> getSessions() {
        return sessions;
    }

    public boolean isQualifiedForSession(SessionType requestedSessionType) throws InstructorNotQualifiedException {
        if (!sessions.contains(requestedSessionType)) {
            throw new InstructorNotQualifiedException(
                    "Instructor is not qualified for session: " + requestedSessionType,
                    this,
                    requestedSessionType
            );
        }
        return true;
    }
}
