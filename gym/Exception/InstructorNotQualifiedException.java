package gym.Exception;

import gym.management.*;
import gym.management.Sessions.*;

public class InstructorNotQualifiedException extends RuntimeException {

    public InstructorNotQualifiedException(String message) {
        super(message);

    }
}
