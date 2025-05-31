package ru.nsu.fit.yus.mafia;

import java.util.List;
import java.util.Map;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    List<Observer> getObservers(); // не обязательно, но может быть полезен

    void notifyObservers(EventType type, Map<String, Object> data);
}
