package gym.Exception;

import gym.management.*;
import gym.management.Sessions.*;

public class InstructorNotQualifiedException extends RuntimeException {

    private Instructor instructor;
    private SessionType sessionType;

    public InstructorNotQualifiedException(String message, Instructor instructor, SessionType sessionType) {
        super(message);
        this.instructor = instructor;
        this.sessionType = sessionType;
    }
}
