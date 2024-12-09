package gym.management;

public enum ForumType {
    All("All genders"),
    Female("Female only"),
    Seniors("Seniors only"),
    Male("Male only");

    private final String description;

    ForumType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
