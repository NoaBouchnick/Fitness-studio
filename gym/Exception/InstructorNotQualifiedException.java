package gym.Exception;

import gym.management.Instructor;
import gym.management.Sessions.SessionType;

public class InstructorNotQualifiedException extends RuntimeException {

    private Instructor instructor;
    private SessionType sessionType;

    public InstructorNotQualifiedException(String message, Instructor instructor, SessionType sessionType) {
        super(message);
        this.instructor = instructor;
        this.sessionType = sessionType;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Instructor: " + instructor.getName() + " is not qualified for " + sessionType;
    }
}
