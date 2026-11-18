package pe.com.mascovet.config;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionContext {
    private TransactionContext() {
    }

    public static Connection getConnection() throws SQLException {
        return DBManager.getInstance().iniciarTransaccion();
    }

    public static Connection getCurrentConnection() {
        if (DBManager.getInstance().conexionTransaccionalActual() == null) return null;
        return DBManager.getInstance().getConnection();
    }

    public static void commit() throws SQLException {
        DBManager.getInstance().confirmarTransaccion();
    }

    public static void rollback() {
        DBManager.getInstance().revertirTransaccion();
    }

    public static void close() {
        DBManager.getInstance().cerrarTransaccion();
    }
}
