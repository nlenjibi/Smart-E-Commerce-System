package com.smartecommerce.dao;

import com.smartecommerce.models.User;
import com.smartecommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;
import static com.smartecommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * UserDAO handles all database operations for User entity
 */
public class UserDAO {

    /**
     * Create a new user
     * @return the generated user ID, or -1 if creation failed
     */
    public int create(User user) {
        String sql = "INSERT INTO Users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole());

        if (insertResult.hasError()) {
            printE("Error creating user: " + insertResult.getError());
            return -1;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            user.setUserId(generatedId.intValue());
            return generatedId.intValue();
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return (affectedRows != null && affectedRows > 0) ? user.getUserId() : -1;
    }

    /**
     * Find user by ID
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, userId);

        if (queryResult.hasError()) {
            printE("Error finding user: " + queryResult.getError());
            return null;
        }

        return mapSingleUser(queryResult);
    }

    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        QueryResult queryResult = executePreparedQuery(sql, username);

        if (queryResult.hasError()) {
            printE("Error finding user by username: " + queryResult.getError());
            return null;
        }

        return mapSingleUser(queryResult);
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE LOWER(email) = LOWER(?)";
        QueryResult queryResult = executePreparedQuery(sql, email);

        if (queryResult.hasError()) {
            printE("Error finding user by email: " + queryResult.getError());
            return null;
        }

        return mapSingleUser(queryResult);
    }

    /**
     * Authenticate user
     */
    public User authenticate(String username, String passwordHash) {
        String sql = "SELECT * FROM Users WHERE (username = ? OR email = ?) AND password_hash = ?";
        QueryResult queryResult = executePreparedQuery(sql, username, username, passwordHash);

        if (queryResult.hasError()) {
            printE("Error authenticating user: " + queryResult.getError());
            return null;
        }

        return mapSingleUser(queryResult);
    }

    /**
     * Get all users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY created_at DESC";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error retrieving users: " + queryResult.getError());
            return users;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return users;
        }

        for (Map<String, Object> row : rows) {
            User mappedUser = mapRow(row);
            if (mappedUser != null) {
                users.add(mappedUser);
            }
        }
        return users;
    }

    /**
     * Find users by role
     */
    public List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE role = ? ORDER BY created_at DESC";
        QueryResult queryResult = executePreparedQuery(sql, role);

        if (queryResult.hasError()) {
            printE("Error finding users by role: " + queryResult.getError());
            return users;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return users;
        }

        for (Map<String, Object> row : rows) {
            User mappedUser = mapRow(row);
            if (mappedUser != null) {
                users.add(mappedUser);
            }
        }
        return users;
    }

    /**
     * Update user
     */
    public boolean update(User user) {
        String sql = "UPDATE Users SET username = ?, email = ?, password_hash = ?, role = ? WHERE user_id = ?";
        QueryResult updateResult = executePreparedQuery(
                sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                user.getUserId());

        if (updateResult.hasError()) {
            printE("Error updating user: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Update user password by user ID
     * @param userId User ID
     * @param newPasswordHash New password hash
     * @return true if password was updated successfully
     */
    public boolean updatePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE Users SET password_hash = ? WHERE user_id = ?";
        QueryResult updateResult = executePreparedQuery(sql, newPasswordHash, userId);

        if (updateResult.hasError()) {
            printE("Error updating password: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Delete user
     */
    public int delete(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        QueryResult deleteResult = executePreparedQuery(sql, userId);

        if (deleteResult.hasError()) {
            printE("Error deleting user: " + deleteResult.getError());
            return 0;
        }

        Integer affectedRows = deleteResult.getAffectedRows();
        return affectedRows != null ? affectedRows : 0;
    }

    /**
     * Search users by username or email (partial match, case-insensitive)
     */
    public List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE LOWER(username) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY created_at DESC";
        QueryResult queryResult = executePreparedQuery(sql, "%" + searchTerm + "%", "%" + searchTerm + "%");

        if (queryResult.hasError()) {
            printE("Error searching users: " + queryResult.getError());
            return users;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return users;
        }

        for (Map<String, Object> row : rows) {
            User mappedUser = mapRow(row);
            if (mappedUser != null) {
                users.add(mappedUser);
            }
        }
        return users;
    }

    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM Users WHERE LOWER(username) = LOWER(?)";
        QueryResult queryResult = executePreparedQuery(sql, username);

        if (queryResult.hasError()) {
            printE("Error checking username: " + queryResult.getError());
            return false;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            Object countObj = rows.get(0).get("count");
            int count = asInt(countObj);
            return count > 0;
        }
        return false;
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM Users WHERE LOWER(email) = LOWER(?)";
        QueryResult queryResult = executePreparedQuery(sql, email);

        if (queryResult.hasError()) {
            printE("Error checking email: " + queryResult.getError());
            return false;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            Object countObj = rows.get(0).get("count");
            int count = asInt(countObj);
            return count > 0;
        }
        return false;
    }

    /**
     * Get total count of users
     */
    public int getUserCount() {
        String sql = "SELECT COUNT(*) as count FROM Users";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error counting users: " + queryResult.getError());
            return 0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            Object countObj = rows.get(0).get("count");
            return asInt(countObj);
        }
        return 0;
    }

    /**
     * Get count of users by role
     */
    public int getCountByRole(String role) {
        String sql = "SELECT COUNT(*) as count FROM Users WHERE role = ?";
        QueryResult queryResult = executePreparedQuery(sql, role);

        if (queryResult.hasError()) {
            printE("Error counting users by role: " + queryResult.getError());
            return 0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            Object countObj = rows.get(0).get("count");
            return asInt(countObj);
        }
        return 0;
    }

    /**
     * Get user registration statistics by date range
     * Analytics function for dashboard reports
     */
    public Map<String, Integer> getUserRegistrationStats() {
        Map<String, Integer> stats = new java.util.HashMap<>();

        // Get users registered in last 24 hours
        String sql24h = "SELECT COUNT(*) as count FROM Users WHERE created_at >= DATEADD(HOUR, -24, GETDATE())";
        QueryResult result24h = executePreparedQuery(sql24h);
        if (!result24h.hasError() && result24h.getResultSet() != null && !result24h.getResultSet().isEmpty()) {
            stats.put("last24Hours", asInt(result24h.getResultSet().get(0).get("count")));
        } else {
            stats.put("last24Hours", 0);
        }

        // Get users registered in last 7 days
        String sql7d = "SELECT COUNT(*) as count FROM Users WHERE created_at >= DATEADD(DAY, -7, GETDATE())";
        QueryResult result7d = executePreparedQuery(sql7d);
        if (!result7d.hasError() && result7d.getResultSet() != null && !result7d.getResultSet().isEmpty()) {
            stats.put("last7Days", asInt(result7d.getResultSet().get(0).get("count")));
        } else {
            stats.put("last7Days", 0);
        }

        // Get users registered in last 30 days
        String sql30d = "SELECT COUNT(*) as count FROM Users WHERE created_at >= DATEADD(DAY, -30, GETDATE())";
        QueryResult result30d = executePreparedQuery(sql30d);
        if (!result30d.hasError() && result30d.getResultSet() != null && !result30d.getResultSet().isEmpty()) {
            stats.put("last30Days", asInt(result30d.getResultSet().get(0).get("count")));
        } else {
            stats.put("last30Days", 0);
        }

        return stats;
    }

    /**
     * Get active users count (users who have placed orders)
     * Analytics function - requires Orders table
     */
    public int getActiveUsersCount() {
        String sql = "SELECT COUNT(DISTINCT user_id) as count FROM Orders";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error counting active users: " + queryResult.getError());
            return 0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            Object countObj = rows.get(0).get("count");
            return asInt(countObj);
        }
        return 0;
    }

    /**
     * Get user role distribution for analytics
     * Returns map of role -> count
     */
    public Map<String, Integer> getUserRoleDistribution() {
        Map<String, Integer> distribution = new java.util.HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM Users GROUP BY role";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error getting role distribution: " + queryResult.getError());
            return distribution;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                String role = asString(row.get("role"));
                int count = asInt(row.get("count"));
                distribution.put(role, count);
            }
        }
        return distribution;
    }

    /**
     * Get recent user registrations (for activity feed)
     * @param limit Maximum number of users to return
     */
    public List<User> getRecentRegistrations(int limit) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY created_at DESC LIMIT ?";
        QueryResult queryResult = executePreparedQuery(sql, limit);

        if (queryResult.hasError()) {
            printE("Error getting recent registrations: " + queryResult.getError());
            return users;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return users;
        }

        for (Map<String, Object> row : rows) {
            User mappedUser = mapRow(row);
            if (mappedUser != null) {
                users.add(mappedUser);
            }
        }
        return users;
    }

    /**
     * Map the first row from a query result to a User
     */
    private User mapSingleUser(QueryResult queryResult) {
        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return mapRow(rows.get(0));
    }

    /**
     * Convert a row map into a User instance
     */
    private User mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        User user = new User();
        user.setUserId(asInt(row.get("user_id")));
        user.setUsername(asString(row.get("username")));
        user.setEmail(asString(row.get("email")));
        user.setPasswordHash(asString(row.get("password_hash")));
        user.setRole(asString(row.get("role")));
        user.setCreatedAt(asLocalDateTime(row.get("created_at")));
        user.setUpdatedAt(asLocalDateTime(row.get("updated_at")));
        return user;
    }


}
