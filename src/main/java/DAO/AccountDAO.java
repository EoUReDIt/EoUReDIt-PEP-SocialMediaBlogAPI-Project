package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

/**
 * DAO = talks to the `account` table only. No validation logic belongs here —
 * that's the Service layer's job. This class should be "dumb": give it data,
 * it runs SQL, it hands back Java objects (or null).
 */
public class AccountDAO {

    /**
     * Look up an account by username.
     * Used by both registration (duplicate check) and login.
     *
     * @return the matching Account, or null if no row matches.
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // TODO 1: Bind `username` into the SQL's single placeholder.

            // Lookup: PreparedStatement has several `setXxx(index, value)` methods.
            // Which type is `username`, and what index does the FIRST `?` use in JDBC?
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            // TODO 2: Which ResultSet method moves the cursor to the first row
            // AND returns false if there were no rows at all? Use it as the `if` condition.
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Look up an account by its id. Needed so MessageService can check that
     * `posted_by` refers to a real account before inserting a message.
     *
     * @return the matching Account, or null if no row matches.
     */
    public Account getAccountById(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // TODO 3: 

            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert a new account and return it WITH the database-generated account_id
     * filled in. This is the tricky one — read the TODOs in order.
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // TODO 4: `prepareStatement` has an overload that takes a second argument
            // telling the driver "give me the auto-generated key back afterward."
            // Lookup: java.sql.Statement — a constant field, not a method.
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            // TODO 5: Now retrieve the generated key. Which PreparedStatement method
            // returns a ResultSet containing the generated key(s)?
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                return new Account(generatedId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}