package gym.customers;

import gym.management.Sessions.ForumType;
import gym.management.Sessions.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Client extends Person {
    private List<String> notifications = new ArrayList<>();
    private List<Session> mySessions = new ArrayList<>();


    public Client(String name, int accountBalance, Gender gender, String data,
                  List<String> notifications, List<Session> mySessions) {
        super(name, accountBalance, gender, data);  // <-- מקצה ID חדש
        this.notifications = initilaize(notifications);
        this.mySessions = initilaize(mySessions);
    }

    // בנאי שמשדרג מ-Person קיים (שומר ID קיים)
    public Client(Person existingPerson, List<String> notifications, List<Session> mySessions) {
        super(existingPerson);
        this.notifications = initilaize(notifications);
        this.mySessions = initilaize(mySessions);
    }

    private <T> List<T> initilaize(List<T> list) {
        return (list != null) ? list : new ArrayList<>();
    }

    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    public void addSessionClient(Session session) {
        this.mySessions.add(session);
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Client client = (Client) o;
        return Objects.equals(notifications, client.notifications) &&
                Objects.equals(mySessions, client.mySessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), notifications, mySessions);
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " | Name: " + getName() + " | Gender: " + getGender() +
                " | Birthday: " + getData() + " | Age: " + getAge() +
                " | Balance: " + getMoneyBalance();
    }

}
