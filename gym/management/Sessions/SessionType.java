package gym.management.Sessions;

public enum  SessionType {

    ThaiBoxing("Advanced"),
    MachinePilates("Intermediate"),
    Pilates("Beginner"),
    Ninja("Advanced");

    private String requiredQualification;

    SessionType(String requiredQualification) {
        this.requiredQualification = requiredQualification;
    }


    public String getRequiredQualification() {
        return requiredQualification;
    }
}


