package me.ifling;

import me.liuwj.ktorm.database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseFactory {
    private static Database database = null;

    public static Database getInstance(String url) {
        if (database == null) {
            synchronized (DatabaseFactory.class) {
                if (database == null) {
                    Config config = Config.getInstance();
                    switch (config.db.type) {
                        case "sqlite3":
//                            database = Database.connect(url);
//                            Database.connect();
                    }
                }
            }
        }
        return database;
    }

    public void foo(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";
        System.out.println("xpqlo");
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("success");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("where are you");
        }
    }


    public static void main(String[] args) {
//        DatabaseFactory databaseFactory = new DatabaseFactory();
//        Database.connect("");
    }
}
