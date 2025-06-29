package it.unibo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DAOUtils {

    // Establishes a connection to a MySQL daemon running locally at port 3306.
    //
    public static Connection localMySQLConnection() {
        try {
            String url      = "jdbc:mysql://localhost:3306/pl8?serverTimezone=UTC";
            String username = "root";
            String password = "Pb05L=18";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DAOException("Errore di connessione al database", e);
        }
    }


    // We must always prepare a statement to make sure we do not fall victim to SQL injection:
    // https://owasp.org/www-community/attacks/SQL_Injection
    //
    // This is a helper that prepares the statement with all the values we give it:
    //
    //     prepare(connection, MY_QUERY, query_arg1, query_arg2, ...)
    //
    public static PreparedStatement prepare(Connection connection, String query, Object... values) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            return stmt;
        } catch (SQLException e) {
            throw new DAOException("Errore preparing statement", e);
        }
    }

}
