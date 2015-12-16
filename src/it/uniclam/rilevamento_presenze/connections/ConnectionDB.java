package it.uniclam.rilevamento_presenze.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe ConnectionDB:
 * 1. si occupa della connessione con il database MySQL
 * 2. Interagisce con la classe Server
 */
public class ConnectionDB {

    private static ConnectionDB connectionFactory = null;
    //parametri per la connessione al database
    String driverClassName = "com.mysql.jdbc.Driver";
    String connectionUrl = "jdbc:mysql://localhost:3306/";
    String dbName = "movedb";
	String dbUser = "root";
	String dbPwd = "juventus";

    private ConnectionDB() {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionDB getInstance() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionDB();
        }
        return connectionFactory;
    }

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(connectionUrl + dbName, dbUser, dbPwd);
		return conn;
	}


}
