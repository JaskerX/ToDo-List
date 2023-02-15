package de.jaskerx.todolist.db;

import java.sql.*;

public class DbManager {

    public static Connection con;

    public static void init() {
        try {
            Class.forName("java.sql.Driver");
            con = DriverManager.getConnection("jdbc:sqlite:todo.db");

            executeUpdate("CREATE TABLE IF NOT EXISTS categories ('id' INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL)");
            //TODO: status/progress?
            executeUpdate("CREATE TABLE IF NOT EXISTS tasks ('id' INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL, 'description' TEXT, 'category' INTEGER NOT NULL, 'created' TIMESTAMP NOT NULL, 'until' TIMESTAMP, 'favorite' BOOLEAN DEFAULT 0)");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int executeUpdate(String query) {
        try {
            Statement statement = con.createStatement();
            return statement.executeUpdate(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ResultSet executeQuery(String query) {
        try {
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
