package com.smartecommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigManager loads application properties from config/app.properties with environment overrides.
 */
public class ConfigManager {
    private static final String DEFAULT_CONFIG_PATH = "config/app.properties";
    private static final String ENV_FILE = ".env";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^:}]+):?([^}]*)}");

    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        loadProperties();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        loadEnvFile();

        // Try filesystem path first so local overrides win
        Path configPath = Path.of(DEFAULT_CONFIG_PATH);
        if (Files.exists(configPath)) {
            try (InputStream in = Files.newInputStream(configPath)) {
                properties.load(in);
                return;
            } catch (IOException ignored) {
                // Fall back to classpath
            }
        }

        // Fallback: classpath resource
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException ignored) {
            // Leave properties empty if load fails
        }
    }

    /**
     * Lightweight .env parser to populate System properties for placeholder resolution.
     */
    private void loadEnvFile() {
        Path envPath = Path.of(ENV_FILE);
        if (!Files.exists(envPath)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(envPath)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int idx = trimmed.indexOf('=');
                if (idx <= 0) {
                    continue;
                }
                String key = trimmed.substring(0, idx).trim();
                String value = trimmed.substring(idx + 1).trim();
                if (!key.isEmpty()) {
                    properties.putIfAbsent(key, value);
                }
            }
        } catch (IOException ignored) {
            // best-effort; ignore if cannot read
        }
    }

    private String resolvePlaceholders(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(rawValue);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String envKey = matcher.group(1);
            String defaultValue = matcher.group(2);
            String envValue = System.getenv(envKey);
            String propertyValue = properties.getProperty(envKey);
            String resolved = envValue != null ? envValue : (propertyValue != null ? propertyValue : defaultValue);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(resolved));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);
        value = resolvePlaceholders(value);
        return value != null ? value : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, String.valueOf(defaultValue)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(getString(key, String.valueOf(defaultValue)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    public Map<String, String> getAdditionalJdbcProperties() {
        Map<String, String> result = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("database.jdbc.additionalProperties.")) {
                String propKey = key.substring("database.jdbc.additionalProperties.".length());
                result.put(propKey, resolvePlaceholders(properties.getProperty(key)));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    public String getDatabaseType() {
        return getString("database.type", "mysql").toLowerCase();
    }

    public String getJdbcDriverClass() {
        return switch (getDatabaseType()) {
            case "postgresql", "postgres" -> "org.postgresql.Driver";
            default -> "com.mysql.cj.jdbc.Driver";
        };
    }

    public String buildJdbcUrl() {
        String type = getDatabaseType();
        String host = getString("database.host", "localhost");
        String port = getString("database.port", type.equals("postgresql") ? "5432" : "3306");
        String name = getString("database.name", "smart_ecommerce");
        StringBuilder url = new StringBuilder();

        if ("postgresql".equals(type) || "postgres".equals(type)) {
            url.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(name);
        } else {
            url.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(name);
        }

        Map<String, String> extras = getAdditionalJdbcProperties();
        if (!extras.isEmpty()) {
            url.append("?");
            boolean first = true;
            for (Map.Entry<String, String> entry : extras.entrySet()) {
                if (!first) {
                    url.append("&");
                }
                url.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }
        return url.toString();
    }
}

