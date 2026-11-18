package pe.com.mascovet.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {
    private static DBManager instance;
    private final String hostname;
    private final String user;
    private final String password;
    private final String port;
    private final String database;
    private final String DB_CREDENTIALS_FILE = "db";
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private DBManager() {
        ResourceBundle db = ResourceBundle.getBundle(DB_CREDENTIALS_FILE);
        hostname = db.getString("db.hostname");
        user = db.getString("db.user");
        password = db.getString("db.password");
        port = db.getString("db.port");
        database = db.getString("db.database");
    }

    public static DBManager getInstance() {
        if (instance == null) instance = new DBManager();
        return instance;
    }

    private Connection nuevaConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el controlador MySQL", ex);
        }
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database
                + "?sslMode=REQUIRED"
                + "&fallbackToSystemKeyStore=false"
                + "&fallbackToSystemTrustStore=false";
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        Connection actual = connectionHolder.get();
        if (actual != null) {

            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(), new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("close")) return null;
                        try {
                            return method.invoke(actual, args);
                        } catch (InvocationTargetException ex) {
                            throw ex.getCause();
                        }
                    });
        }
        try {
            return nuevaConexion();
        } catch (SQLException ex) {
            throw new RuntimeException("ERROR al conectar con la BD: " + ex.getMessage(), ex);
        }
    }

    Connection iniciarTransaccion() throws SQLException {
        Connection actual = connectionHolder.get();
        if (actual == null) {
            actual = nuevaConexion();
            actual.setAutoCommit(false);
            connectionHolder.set(actual);
        }
        return actual;
    }

    Connection conexionTransaccionalActual() {
        return connectionHolder.get();
    }

    void confirmarTransaccion() throws SQLException {
        Connection actual = connectionHolder.get();
        if (actual != null) actual.commit();
    }

    void revertirTransaccion() {
        Connection actual = connectionHolder.get();
        if (actual != null) {
            try {
                actual.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    void cerrarTransaccion() {
        Connection actual = connectionHolder.get();
        if (actual != null) {
            try {
                actual.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }
}
