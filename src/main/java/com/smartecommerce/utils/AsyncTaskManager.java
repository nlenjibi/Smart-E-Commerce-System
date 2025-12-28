package com.smartecommerce.utils;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Async Task Manager for running background operations
 * Prevents UI freezing during long-running operations
 * Optimized to reuse executor services and prevent resource leaks
 */
public class AsyncTaskManager {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskManager.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new ThreadFactory() {
                private int counter = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("AsyncTask-" + counter++);
                    thread.setDaemon(true);
                    return thread;
                }
            });

    // Shared scheduler to prevent resource leaks
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            2,
            new ThreadFactory() {
                private int counter = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Scheduler-" + counter++);
                    thread.setDaemon(true);
                    return thread;
                }
            });

    private AsyncTaskManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Run task in background and update UI on completion
     */
    public static <T> CompletableFuture<T> runAsync(
            Callable<T> backgroundTask,
            Consumer<T> onSuccess,
            Consumer<Throwable> onError) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.debug("Starting async task on thread: {}", Thread.currentThread().getName());
                return backgroundTask.call();
            } catch (Exception e) {
                logger.error("Error in async task", e);
                throw new RuntimeException(e);
            }
        }, executorService).whenComplete((result, throwable) -> {
            Platform.runLater(() -> {
                if (throwable != null) {
                    if (onError != null) {
                        onError.accept(throwable);
                    }
                } else {
                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                }
            });
        });
    }

    /**
     * Run void task in background
     */
    public static CompletableFuture<Void> runAsync(
            Runnable backgroundTask,
            Runnable onSuccess,
            Consumer<Throwable> onError) {

        return CompletableFuture.runAsync(() -> {
            try {
                logger.debug("Starting async void task on thread: {}", Thread.currentThread().getName());
                backgroundTask.run();
            } catch (Exception e) {
                logger.error("Error in async void task", e);
                throw new RuntimeException(e);
            }
        }, executorService).whenComplete((result, throwable) -> {
            Platform.runLater(() -> {
                if (throwable != null) {
                    if (onError != null) {
                        onError.accept(throwable);
                    }
                } else {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                }
            });
        });
    }

    /**
     * Run task with timeout
     */
    public static <T> CompletableFuture<T> runAsyncWithTimeout(
            Callable<T> backgroundTask,
            long timeout,
            TimeUnit timeUnit,
            Consumer<T> onSuccess,
            Consumer<Throwable> onError) {

        CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
            try {
                return backgroundTask.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService);

        // Add timeout using shared scheduler
        CompletableFuture<T> timeoutFuture = new CompletableFuture<>();
        ScheduledFuture<?> timeoutTask = scheduler.schedule(() -> {
            if (!future.isDone()) {
                timeoutFuture.completeExceptionally(new TimeoutException("Task timed out"));
            }
        }, timeout, timeUnit);

        return CompletableFuture.anyOf(future, timeoutFuture)
                .thenApply(result -> (T) result)
                .whenComplete((result, throwable) -> {
                    timeoutTask.cancel(false);
                    Platform.runLater(() -> {
                        if (throwable != null) {
                            if (onError != null) {
                                onError.accept(throwable);
                            }
                        } else {
                            if (onSuccess != null) {
                                onSuccess.accept(result);
                            }
                        }
                    });
                });
    }

    /**
     * Run multiple tasks in parallel
     */
    @SafeVarargs
    public static <T> CompletableFuture<Void> runAllAsync(
            Consumer<Throwable> onError,
            Callable<T>... tasks) {

        CompletableFuture<?>[] futures = new CompletableFuture[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            final int index = i;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    return tasks[index].call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executorService);
        }

        return CompletableFuture.allOf(futures).exceptionally(throwable -> {
            Platform.runLater(() -> {
                if (onError != null) {
                    onError.accept(throwable);
                }
            });
            return null;
        });
    }

    /**
     * Schedule task with delay using shared scheduler
     */
    public static ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(task, delay, timeUnit);
    }

    /**
     * Schedule periodic task using shared scheduler
     */
    public static ScheduledFuture<?> schedulePeriodicTask(
            Runnable task,
            long initialDelay,
            long period,
            TimeUnit timeUnit) {
        return scheduler.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    /**
     * Run on JavaFX UI thread
     */
    public static void runOnUIThread(Runnable task) {
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    /**
     * Shutdown all executor services
     */
    public static void shutdown() {
        logger.info("Shutting down AsyncTaskManager...");

        // Shutdown main executor
        executorService.shutdown();

        // Shutdown scheduler
        scheduler.shutdown();

        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            logger.info("AsyncTaskManager shut down successfully");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error("Error during shutdown", e);
        }
    }

    /**
     * Get executor service stats
     */
    public static String getStats() {
        if (executorService instanceof ThreadPoolExecutor tpe) {
            return String.format("Active: %d, Completed: %d, Queue: %d",
                    tpe.getActiveCount(),
                    tpe.getCompletedTaskCount(),
                    tpe.getQueue().size());
        }
        return "Stats not available";
    }
}
