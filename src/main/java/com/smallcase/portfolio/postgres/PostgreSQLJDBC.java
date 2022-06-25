package com.smallcase.portfolio.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSQLJDBC {

    private static Connection c;
    private static Optional<Connection> connection = null;
    private static final Logger LOGGER = Logger.getLogger(PostgreSQLJDBC.class.getName());

    static{
        c = createConnection();
    }

    private static Connection createConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/portfolio-management",
                    "postgres", "postgres");
            connection.setAutoCommit(false);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static Connection getConnection(){
        if(c == null)
            c = createConnection();
        return c;
    }

    public static Optional<Connection> getConnection1() {
        if (!connection.isPresent()) {
            String url = "jdbc:postgresql://localhost:5432/portfolio-management";
            String user = "postgres";
            String password = "postgres";

            try {
                connection = Optional.ofNullable(DriverManager.getConnection(url, user, password));
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }
}
