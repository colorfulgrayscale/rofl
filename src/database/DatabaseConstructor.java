package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class DatabaseConstructor {

	public static void main(final String[] args) {
		new DatabaseConstructor().run();
	}

	public final void run() {
		Connection dbConnection = null;
		Properties props = new Properties();
		try {
			Class.forName("org.postgresql.Driver");
			props = new Properties();
			props.setProperty("user", "postgres");
			props.setProperty("password", "Typewriter39");
			dbConnection = DriverManager.getConnection(
					"jdbc:postgresql://cortex.ddns.uark.edu:5432/postgres",
					props);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		if (dbConnection != null) {
			try {
				dbConnection.createStatement().execute(
						"drop database cortexrestaurant;");
				dbConnection.createStatement().execute(
						"create database cortexrestaurant;");
				dbConnection.close();
				dbConnection = DriverManager
						.getConnection(
								"jdbc:postgresql://cortex.ddns.uark.edu:5432/cortexrestaurant",
								props);
				StringBuilder query = new StringBuilder();
				query.append("begin; ");
				Scanner in = new Scanner(new File(
						"..\\Restaurant\\database\\setup.sql"));
				while (in.hasNextLine()) {
					query.append(in.nextLine());
				}
				query.append("commit; ");
				dbConnection.createStatement().execute(query.toString());
				query = new StringBuilder();
				query.append("begin; ");
				in = new Scanner(new File(
						"..\\Restaurant\\database\\testdata.sql"));
				while (in.hasNextLine()) {
					query.append(in.nextLine());
				}
				query.append("commit; ");
				dbConnection.createStatement().execute(
						query.toString().replace("$NOW$",
								String.valueOf(System.currentTimeMillis())));
				dbConnection.close();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
