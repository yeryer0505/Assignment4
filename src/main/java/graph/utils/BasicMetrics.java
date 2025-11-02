package graph.utils;

import java.util.HashMap;
import java.util.Map;

public class BasicMetrics implements Metrics {
    private long startTime;
    private long endTime;
    private final Map<String, Integer> counters = new HashMap<>();

    @Override
    public void start() {
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        endTime = System.nanoTime();
    }

    @Override
    public void increment(String operation) {
        counters.put(operation, counters.getOrDefault(operation, 0) + 1);
    }

    @Override
    public long getTime() {
        return (endTime - startTime) / 1_000_000;
    }

    @Override
    public Map<String, Integer> getCounters() {
        return counters;
    }
}
