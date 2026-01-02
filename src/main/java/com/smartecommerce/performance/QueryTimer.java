package com.smartecommerce.performance;

import java.util.HashMap;
import java.util.Map;

/**
 * QueryTimer measures and tracks database query performance
 * Used for performance analysis and optimization
 */
public class QueryTimer {
    private static final Map<String, Long> queryTimes = new HashMap<>();
    private static final Map<String, Integer> queryExecutions = new HashMap<>();

    /**
     * Start timing a query
     */
    public static long startTimer() {
        return System.nanoTime();
    }

    /**
     * End timing and record the query
     */
    public static void endTimer(String queryName, long startTime) {
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Store timing information
        queryTimes.put(queryName, queryTimes.getOrDefault(queryName, 0L) + duration);
        queryExecutions.put(queryName, queryExecutions.getOrDefault(queryName, 0) + 1);
    }

    /**
     * Get average execution time for a query in milliseconds
     */
    public static double getAverageTime(String queryName) {
        if (!queryExecutions.containsKey(queryName) || queryExecutions.get(queryName) == 0) {
            return 0.0;
        }
        long totalTime = queryTimes.get(queryName);
        int executions = queryExecutions.get(queryName);
        return (totalTime / executions) / 1_000_000.0; // Convert to milliseconds
    }

    /**
     * Get total execution time for a query in milliseconds
     */
    public static double getTotalTime(String queryName) {
        if (!queryTimes.containsKey(queryName)) {
            return 0.0;
        }
        return queryTimes.get(queryName) / 1_000_000.0; // Convert to milliseconds
    }

    /**
     * Get number of executions for a query
     */
    public static int getExecutionCount(String queryName) {
        return queryExecutions.getOrDefault(queryName, 0);
    }

    /**
     * Get all query statistics
     */
    public static Map<String, String> getAllStats() {
        Map<String, String> stats = new HashMap<>();
        for (String queryName : queryTimes.keySet()) {
            String stat = String.format("Executions: %d, Avg Time: %.2f ms, Total Time: %.2f ms",
                    getExecutionCount(queryName),
                    getAverageTime(queryName),
                    getTotalTime(queryName));
            stats.put(queryName, stat);
        }
        return stats;
    }

    /**
     * Reset all statistics
     */
    public static void reset() {
        queryTimes.clear();
        queryExecutions.clear();
    }

    /**
     * Print statistics to console
     */
    public static void printStats() {
        System.out.println("\n===== Query Performance Statistics =====");
        getAllStats().forEach((queryName, stats) -> {
            System.out.println(queryName + ": " + stats);
        });
        System.out.println("========================================\n");
    }
}

