package me.ifling;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseConn {
    Connection conn = null;

    public boolean execute(String sql) {
        boolean b = false;
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            b = stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }
}
