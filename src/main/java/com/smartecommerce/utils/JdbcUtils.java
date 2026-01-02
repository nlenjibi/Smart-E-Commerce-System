package com.smartecommerce.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static com.smartecommerce.config.DatabaseConfig.getConnectionWithProps;
import static com.smartecommerce.utils.AppUtils.*;

/**
 * JdbcUtils provides utility methods for JDBC operations
 * Helps with resource management and common database operations
 */
public class JdbcUtils {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static final String ERROR_ROLLBACK_MSG = "Error during rollback: ";
    private static final String QUERY_TYPE_SELECT = "select";
    private static final String QUERY_TYPE_SHOW = "show";
    private static final String QUERY_TYPE_INSERT = "insert";

    private JdbcUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }


    /**
     * Execute a prepared statement query with parameters
     * @param query SQL query with ? placeholders
     * @param data Array of parameters to bind to the query
     * @return QueryResult object containing results, affected rows, or generated keys
     */
    public static QueryResult executePreparedQuery(String query, Object... data) {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = getConnectionWithProps();
            if (con == null) {
                return new QueryResult("Failed to establish database connection");
            }

            con.setAutoCommit(false);
            logger.info("Executing Query: {}", query);
            logger.debug("Query Data: {}", Arrays.toString(data));

            stm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            bindParameters(stm, data);

            QueryResult result = executeQuery(stm, query);
            con.commit();
            logger.info("Transaction committed successfully");

            return result;

        } catch (Exception e) {
            handleTransactionError(con, e, query, data);
            return new QueryResult("Database error: " + e.getMessage());
        } finally {
            closeResources(null, stm, con);
        }
    }

    /**
     * Bind parameters to prepared statement
     */
    private static void bindParameters(PreparedStatement stm, Object... data) throws SQLException {
        for (int i = 0; i < data.length; i++) {
            stm.setObject(i + 1, data[i]);
        }
    }

    /**
     * Execute query based on type
     */
    private static QueryResult executeQuery(PreparedStatement stm, String query) throws SQLException {
        String queryType = query.trim().substring(0, Math.min(6, query.trim().length())).toLowerCase();

        if (queryType.startsWith(QUERY_TYPE_SELECT) || queryType.startsWith(QUERY_TYPE_SHOW)) {
            return executeSelectQuery(stm);
        } else if (queryType.startsWith(QUERY_TYPE_INSERT)) {
            return executeInsertQuery(stm);
        } else {
            return executeUpdateQuery(stm);
        }
    }

    /**
     * Execute SELECT query
     */
    private static QueryResult executeSelectQuery(PreparedStatement stm) throws SQLException {
        try (ResultSet rs = stm.executeQuery()) {
            List<Map<String, Object>> resultList = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
            return new QueryResult(resultList);
        }
    }

    /**
     * Execute INSERT query
     */
    private static QueryResult executeInsertQuery(PreparedStatement stm) throws SQLException {
        int affectedRows = stm.executeUpdate();
        try (ResultSet rs = stm.getGeneratedKeys()) {
            if (rs.next()) {
                return new QueryResult(rs.getLong(1));
            }
            return new QueryResult(affectedRows);
        }
    }

    /**
     * Execute UPDATE/DELETE query
     */
    private static QueryResult executeUpdateQuery(PreparedStatement stm) throws SQLException {
        int affectedRows = stm.executeUpdate();
        return new QueryResult(affectedRows);
    }

    /**
     * Handle transaction errors
     */
    private static void handleTransactionError(Connection con, Exception e, String query, Object... data) {
        if (con != null) {
            try {
                con.rollback();
                logger.error("Transaction rolled back due to error");
            } catch (Exception rollbackEx) {
                logger.error(ERROR_ROLLBACK_MSG + rollbackEx.getMessage());
            }
        }
        logger.error("Database Query Error: {} | Query: {} | Data: {}",
                e.getMessage(), query, Arrays.toString(data));
        logger.debug("Stack trace:", e);
    }

    /**
     * Close resources safely
     */
    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.warn("Error restoring auto-commit: {}", e.getMessage());
                }
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing resources: {}", e.getMessage());
        }
    }

    /**
     * Inner class to hold query results
     */
    public static class QueryResult {
        private List<Map<String, Object>> resultSet;
        private Long generatedKey;
        private Integer affectedRows;
        private String error;

        // Constructor for SELECT queries
        public QueryResult(List<Map<String, Object>> resultSet) {
            this.resultSet = resultSet;
        }

        // Constructor for INSERT queries (generated key)
        public QueryResult(Long generatedKey) {
            this.generatedKey = generatedKey;
        }

        // Constructor for UPDATE/DELETE queries (affected rows)
        public QueryResult(Integer affectedRows) {
            this.affectedRows = affectedRows;
        }

        // Constructor for errors
        public QueryResult(String error) {
            this.error = error;
        }

        public List<Map<String, Object>> getResultSet() {
            return resultSet;
        }

        public Long getGeneratedKey() {
            return generatedKey;
        }

        public Integer getAffectedRows() {
            return affectedRows;
        }

        public String getError() {
            return error;
        }

        public boolean hasError() {
            return error != null;
        }
    }

    /**
     * Close ResultSet safely
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Error closing ResultSet: {}", e.getMessage());
            }
        }
    }

    /**
     * Close Statement safely
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Error closing Statement: {}", e.getMessage());
            }
        }
    }

    /**
     * Close PreparedStatement safely
     */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Error closing PreparedStatement: {}", e.getMessage());
            }
        }
    }

    /**
     * Close Connection safely
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing Connection: {}", e.getMessage());
            }
        }
    }

    /**
     * Rollback transaction safely
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.error(ERROR_ROLLBACK_MSG + "{}", e.getMessage());
            }
        }
    }

    /**
     * Close all JDBC resources at once
     */
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }

    /**
     * Close all JDBC resources with PreparedStatement
     */
    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        closeResultSet(rs);
        closePreparedStatement(pstmt);
        closeConnection(conn);
    }
}

