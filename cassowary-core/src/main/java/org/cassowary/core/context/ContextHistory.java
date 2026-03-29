package org.cassowary.core.context;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextHistory {
    private final Map<String, LinkedList<Double>> history = new ConcurrentHashMap<>();
    private static final int WINDOW_SIZE = 5;

    public void addValue(String key, Double value) {
        history.computeIfAbsent(key, k -> new LinkedList<>()).addFirst(value);
        if (history.get(key).size() > WINDOW_SIZE) {
            history.get(key).removeLast();
        }
    }

    public Double getAverage(String key) {
        LinkedList<Double> values = history.get(key);
        if (values == null || values.isEmpty()) return null;
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
