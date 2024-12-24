package gym.management;

import gym.customers.Client;
import gym.customers.Person;
import gym.management.Sessions.Session;

public class Gym {

    private String name;
    private Secretary secretary;
    private int balance = 0;

    private static Gym instance;

    private Gym() {
        if (instance != null) {
            throw new IllegalStateException("Gym already initialized");
        }
    }

    public static Gym getInstance() {
        if (instance == null) {
            instance = new Gym();
        }
        return instance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Person person, int balance) {
        try {
            if (this.secretary != null) {
                this.secretary.deactivate();
            }
            if (person instanceof Secretary) {
                this.secretary = (Secretary) person;
            } else {
                this.secretary = new Secretary(
                        person.getName(),
                        balance,
                        person.getGender(),
                        person.getData()
                );
            }
            Secretary.setCurrentSecretary(this.secretary);
            this.balance = balance;

            this.secretary.addActionToHistory(
                    "A new secretary has started working at the gym: " + this.secretary.getName()
            );

        } catch (IllegalStateException e) {
            System.out.println("Error: Former secretaries are not permitted to perform actions");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Gym Details
        builder.append("Gym Name: ").append(name).append("\n");
        builder.append("Gym Secretary: ");

        if (secretary != null) {
            builder.append("ID: ").append(secretary.getId())
                    .append(" | Name: ").append(secretary.getName())
                    .append(" | Gender: ").append(secretary.getGender())
                    .append(" | Birthday: ").append(secretary.getData())
                    .append(" | Age: ").append(secretary.getAge())
                    .append(" | Role: Secretary | Salary per Month: ").append(secretary.getSalary());
        } else {
            builder.append("None");
        }

        builder.append("\nGym Balance: ").append(balance).append("\n");

        // Clients Data
        builder.append("\nClients Data:\n");
        if (secretary != null && !secretary.getClients().isEmpty()) {
            for (Client client : secretary.getClients()) {
                builder.append(client.toString()).append("\n");
            }
        } else {
            builder.append("No clients available\n");
        }

        // Sessions Data
        builder.append("\nSessions Data:\n");
        if (secretary != null && secretary.getSessions() != null && !secretary.getSessions().isEmpty()) {
            for (Session session : secretary.getSessions()) {
                builder.append("Session Type: ").append(session.getSessionType())
                        .append(" | Date: ").append(session.getSessionDate())
                        .append(" | Forum: ").append(session.getForumType())
                        .append(" | Instructor: ").append(session.getInstructor())
                        .append(" | Participants: ").append(session.getPrice())
                        .append("/").append(session.getMaxCapacity()).append("\n");
            }
        } else {
            builder.append("No sessions available\n");
        }

        return builder.toString();
    }
}
