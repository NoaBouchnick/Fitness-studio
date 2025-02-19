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

    public void setSecretary(Person person, int secretarySalary) {
        try {
            if (this.secretary != null) {
                this.secretary.deactivate();
            }

            if (person instanceof Secretary) {
                this.secretary = (Secretary) person;
            } else {
                this.secretary = new Secretary(person, secretarySalary);
            }
            Secretary.setCurrentSecretary(this.secretary);

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

        builder.append("Gym Name: ").append(name).append("\n");

        builder.append("Gym Secretary: ");
        if (secretary != null) {
            builder.append("ID: ").append(secretary.getId())
                    .append(" | Name: ").append(secretary.getName())
                    .append(" | Gender: ").append(secretary.getGender())
                    .append(" | Birthday: ").append(secretary.getData())
                    .append(" | Age: ").append(secretary.getAge())
                    .append(" | Balance: ").append(secretary.getMoneyBalance())
                    .append(" | Role: Secretary | Salary per Month: ")
                    .append(secretary.getSalary());
        } else {
            builder.append("None");
        }

        builder.append("\nGym Balance: ").append(balance).append("\n");

        builder.append("\nClients Data:\n");
        if (secretary != null && !secretary.getClients().isEmpty()) {
            for (Client client : secretary.getClients()) {
                builder.append(client.toString()).append("\n");
            }
        } else {
            builder.append("No clients available\n");
        }

        builder.append("\nEmployees Data:\n");
        if (secretary != null) {
            for (Instructor i : Secretary.getInstructors()) {
                builder.append(i.toString()).append("\n");
            }
            builder.append(secretary.toString()).append("\n");
        }

        builder.append("\nSessions Data:\n");
        if (secretary != null && secretary.getSessions() != null && !secretary.getSessions().isEmpty()) {
            for (Session session : secretary.getSessions()) {

                String formattedDate = session.getSessionDate()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                String instructorName = session.getInstructor().getName();

                int numberOfParticipants = session.getClientsInSession().size();

                builder.append("Session Type: ").append(session.getSessionType())
                        .append(" | Date: ").append(formattedDate)
                        .append(" | Forum: ").append(session.getForumType())
                        .append(" | Instructor: ").append(instructorName)
                        .append(" | Participants: ")
                        .append(numberOfParticipants)
                        .append("/")
                        .append(session.getMaxCapacity())
                        .append("\n");
            }
        } else {
            builder.append("No sessions available\n");
        }

        return builder.toString().trim();
    }
}
