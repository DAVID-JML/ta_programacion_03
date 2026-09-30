package pe.com.mascovet.config;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionContext {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private TransactionContext() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn == null) {
            conn = DBManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            connectionHolder.set(conn);
        }
        return conn;
    }

    public static Connection getCurrentConnection() {
        return connectionHolder.get();
    }

    public static void commit() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            conn.commit();
        }
    }

    public static void rollback() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void close() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }
}
