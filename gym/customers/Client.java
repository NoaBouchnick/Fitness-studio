package gym.customers;

import gym.management.Sessions.ForumType;
import gym.management.Sessions.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Client extends Person {
    private List<String> notifications = new ArrayList<>();
    private List<Session> mySessions = new ArrayList<>();
    private ForumType clientType;

    public Client(String name, int accountBalance, Gender gender, String data,
                  List<String> notifications, List<Session> mySessions) {
        super(name, accountBalance, gender, data);
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

    public List<Session> getMySessions() {
        return mySessions;
    }
    public ForumType getClientType() {
        return clientType;
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public int deductMoney(int money) {
        if (money > getMoneyBalance()) {
            throw new IllegalArgumentException("Insufficient funds: Cannot deduct " + money + " from balance " + getMoneyBalance());
        }
        setMoneyBalance(getMoneyBalance() - money);
        return getMoneyBalance();
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
}
