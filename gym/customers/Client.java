package gym.customers;

import java.util.List;

public class Client extends Person {
    private List<String> notifications;

    public Client(String name, int balance, Gender gender, String data, List<String> notifications) {
        super(name, balance, gender, data);
        if (notifications == null) {
            throw new IllegalArgumentException("Notifications list cannot be null");
        }
        this.notifications = notifications;
    }

    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    public List<String> getNotifications() {
        return notifications;
    }
}
