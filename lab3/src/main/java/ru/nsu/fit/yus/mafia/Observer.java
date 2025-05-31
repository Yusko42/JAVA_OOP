package ru.nsu.fit.yus.mafia;

import java.util.Map;

public interface Observer {
    void onGameEvent(EventType type, Map<String, Object> data);
}
