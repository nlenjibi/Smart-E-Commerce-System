package com.smartecommerce.app;

import com.smartecommerce.models.User;
import com.smartecommerce.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.smartecommerce.utils.AppUtils.println;

/**
 * SessionManager handles user sessions with security features
 * Implements Singleton pattern
 */
public class SessionManager {

    private static SessionManager instance;
    private User currentUser;
    private String sessionId;
    private LocalDateTime sessionStartTime;
    private LocalDateTime lastActivityTime;
    private static final long SESSION_TIMEOUT_MINUTES = 30;

    private SessionManager() {
        // Private constructor for singleton
    }

    /**
     * Get singleton instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Create new session
     */
    public void createSession() {
        this.sessionId = SecurityUtils.generateSessionId();
        this.sessionStartTime = LocalDateTime.now();
        this.lastActivityTime = LocalDateTime.now();

        println("Session created: " + sessionId);
        println("Session start time: " + sessionStartTime);
    }

    /**
     * Destroy current session
     */
    public void destroySession() {
        if (currentUser != null) {
            println("Session destroyed for user: " + currentUser.getUsername());
        }
        this.currentUser = null;
        this.sessionId = null;
        this.sessionStartTime = null;
        this.lastActivityTime = null;
    }

    /**
     * Update last activity time
     */
    public void updateActivity() {
        this.lastActivityTime = LocalDateTime.now();
    }

    /**
     * Check if session is valid
     */
    public boolean isSessionValid() {
        if (sessionId == null || currentUser == null) {
            return false;
        }

        // Check session timeout
        if (lastActivityTime != null) {
            long minutesSinceLastActivity = ChronoUnit.MINUTES.between(lastActivityTime, LocalDateTime.now());
            if (minutesSinceLastActivity > SESSION_TIMEOUT_MINUTES) {
                println("Session timed out");
                destroySession();
                return false;
            }
        }

        return true;
    }

    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        return isSessionValid();
    }

    /**
     * Check if user has specific role
     */
    public boolean hasRole(String role) {
        return currentUser != null && role.equals(currentUser.getRole());
    }

    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Get session duration in minutes
     */
    public long getSessionDuration() {
        if (sessionStartTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(sessionStartTime, LocalDateTime.now());
    }

    /**
     * Get time until session timeout
     */
    public long getTimeUntilTimeout() {
        if (lastActivityTime == null) {
            return 0;
        }
        long minutesSinceLastActivity = ChronoUnit.MINUTES.between(lastActivityTime, LocalDateTime.now());
        return SESSION_TIMEOUT_MINUTES - minutesSinceLastActivity;
    }

    // Getters and setters
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Static helper to get current user from session
     */
    public static User getCurrentUserStatic() {
        return getInstance().getCurrentUser();
    }

    public Integer getUserId() {
        return currentUser != null ? currentUser.getUserId() : null;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Static helper to logout user and destroy session
     */
    public static void logout() {
        getInstance().destroySession();
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public LocalDateTime getLastActivityTime() {
        return lastActivityTime;
    }

    public static long getSessionTimeoutMinutes() {
        return SESSION_TIMEOUT_MINUTES;
    }
}

