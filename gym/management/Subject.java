package gym.management;

import java.util.ArrayList;
import java.util.List;

public interface Subject {
    List<Observer> observers = new ArrayList<>();

    default void addObserver(Observer observer) {
        observers.add(observer);
    }

    default void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
