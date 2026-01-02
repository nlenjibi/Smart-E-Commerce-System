package com.smartecommerce.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FXML Loader with caching for improved performance
 * Reduces redundant FXML parsing and improves scene loading time
 */
public class FXMLCache {
    private static final Logger logger = LoggerFactory.getLogger(FXMLCache.class);
    private static final Map<String, Parent> cache = new ConcurrentHashMap<>();
    private static volatile boolean cachingEnabled = true;

    private FXMLCache() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Load FXML with caching
     */
    public static Parent load(String fxmlPath) throws IOException {
        if (!cachingEnabled) {
            return loadFresh(fxmlPath);
        }

        return cache.computeIfAbsent(fxmlPath, path -> {
            try {
                logger.debug("Loading and caching FXML: {}", path);
                return loadFresh(path);
            } catch (IOException e) {
                logger.error("Failed to load FXML: {}", path, e);
                throw new RuntimeException("Failed to load FXML: " + path, e);
            }
        });
    }

    /**
     * Load FXML without caching
     */
    public static Parent loadFresh(String fxmlPath) throws IOException {
        URL resource = FXMLCache.class.getResource(fxmlPath);
        if (resource == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        return loader.load();
    }

    /**
     * Load FXML with controller
     */
    public static <T> LoadResult<T> loadWithController(String fxmlPath) throws IOException {
        URL resource = FXMLCache.class.getResource(fxmlPath);
        if (resource == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        Parent parent = loader.load();
        T controller = loader.getController();

        return new LoadResult<>(parent, controller);
    }

    /**
     * Load result container
     */
    public static class LoadResult<T> {
        private final Parent parent;
        private final T controller;

        public LoadResult(Parent parent, T controller) {
            this.parent = parent;
            this.controller = controller;
        }

        public Parent getParent() {
            return parent;
        }

        public T getController() {
            return controller;
        }
    }

    /**
     * Preload FXML files
     */
    public static void preload(String... fxmlPaths) {
        for (String path : fxmlPaths) {
            try {
                load(path);
                logger.info("Preloaded FXML: {}", path);
            } catch (IOException e) {
                logger.warn("Failed to preload FXML: {}", path, e);
            }
        }
    }

    /**
     * Clear specific cache entry
     */
    public static void invalidate(String fxmlPath) {
        cache.remove(fxmlPath);
        logger.debug("Invalidated cache for: {}", fxmlPath);
    }

    /**
     * Clear all cache
     */
    public static void clearCache() {
        cache.clear();
        logger.info("FXML cache cleared");
    }

    /**
     * Enable/disable caching
     */
    public static void setCachingEnabled(boolean enabled) {
        cachingEnabled = enabled;
        logger.info("FXML caching {}", enabled ? "enabled" : "disabled");
    }

    /**
     * Get cache size
     */
    public static int getCacheSize() {
        return cache.size();
    }

    /**
     * Check if path is cached
     */
    public static boolean isCached(String fxmlPath) {
        return cache.containsKey(fxmlPath);
    }

    /**
     * Get cache statistics
     */
    public static String getCacheStats() {
        return String.format("FXML Cache: %d files cached, Caching %s",
                cache.size(),
                cachingEnabled ? "enabled" : "disabled");
    }
}
