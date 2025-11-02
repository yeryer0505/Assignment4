package graph.utils;

import java.util.Map;

public interface Metrics {
    void start();
    void stop();
    void increment(String operation);
    long getTime();
    Map<String, Integer> getCounters();
}
