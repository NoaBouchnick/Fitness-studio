package gym.management.Sessions;

public enum ForumType {
    All("All genders"),
    Female("Female only"),
    Seniors("Seniors only"),
    Male("Male only");

    private final String description;

    ForumType(String description) {
        this.description = description;
    }
}
