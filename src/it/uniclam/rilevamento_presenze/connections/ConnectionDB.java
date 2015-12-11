package it.uniclam.rilevamento_presenze.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
	String driverClassName = "com.mysql.jdbc.Driver";
	String connectionUrl = "jdbc:mysql://localhost:3306/";
    String dbName = "movedb";
	String dbUser = "root";
	String dbPwd = "juventus";

	private static ConnectionDB connectionFactory = null;

	private ConnectionDB() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(connectionUrl + dbName, dbUser, dbPwd);
		return conn;
	}

	public static ConnectionDB getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionDB();
		}
		return connectionFactory;
	}


}
