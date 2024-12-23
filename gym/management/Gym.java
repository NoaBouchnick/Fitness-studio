package gym.management;

import gym.customers.Person;

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
            // 1) ביטול המזכירה הקודמת אם קיימת
            if (this.secretary != null) {
                this.secretary.deactivate();
            }

            // 2) אם person כבר Secretary, השתמש בו; אחרת צור מזכירה חדשה
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

            // 3) עדכן את ה־currentSecretary הסטטי במחלקת Secretary
            Secretary.setCurrentSecretary(this.secretary);

            // 4) עדכן את יתרת המכון
            this.balance = balance;

            // 5) הוספת הודעה להיסטוריה של המזכירה החדשה
            this.secretary.addActionToHistory(
                    "A new secretary has started working at the gym: " + this.secretary.getName()
            );

        } catch (IllegalStateException e) {
            // במקרה שיש בעיה בהחלפה
            System.out.println("Error: Former secretaries are not permitted to perform actions");
        }
    }

    @Override
    public String toString() {
        return "Gym Name: " + name + ", Balance: " + balance + ", Secretary: " + (secretary != null ? secretary.getName() : "None");
    }
}
