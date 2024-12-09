package gym.management;

import gym.customers.Person;

public class Gym {

    private String name;
    private Secretary secretary;
    private int balance;

    private static Gym instance;

    private Gym() {}

    public static Gym getInstance() {
        if (instance == null) {
            instance = new Gym();
        }
        return instance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Person person, int balance) {
        if (person instanceof Secretary) {
            this.secretary = (Secretary) person;
        } else {
            this.secretary = new Secretary(person.getName(), balance, person.getGender(), person.getData());
        }
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Gym Name: " + name + ", Balance: " + balance + ", Secretary: " + (secretary != null ? secretary.getName() : "None");
    }
}
