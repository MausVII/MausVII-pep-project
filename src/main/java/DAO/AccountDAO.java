package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
    // Account registration
    // body will contain json, no id. Successful if name is not balnk, password.length > 3,
    // and username doesn't exist already. Response should contain json of account with id, 200.
    // Otherwise return 400

    public Account registerAccount(Account account)
    {
        // Username must not be blank and password length must be <= 4
        if (account.username == "" || account.password.length() < 4)
        {
            return null;
        }

        Connection conn = ConnectionUtil.getConnection();

        try 
        {
            String findUserSQL = "SELECT * FROM account WHERE username = ?";
            PreparedStatement findUserStatement = conn.prepareStatement(findUserSQL, Statement.RETURN_GENERATED_KEYS);
            findUserStatement.setString(1, account.username);

            findUserStatement.executeQuery();
            ResultSet foundUsername = findUserStatement.getGeneratedKeys();

            // If username already exists, error
            if (foundUsername.next())
            {
                return null;
            }
            else
            {
                String insertUserSQL = "INSERT INTO account (username, password) VALUES (?, ?)";
                PreparedStatement insertUserStatement = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);
                insertUserStatement.setString(1, account.username);
                insertUserStatement.setString(2, account.password);

                insertUserStatement.executeUpdate();
                ResultSet insertResultSet = insertUserStatement.getGeneratedKeys();

                if (insertResultSet.next()) {
                    int generatedUserId = (int)insertResultSet.getLong(1);
                    return new Account(generatedUserId, account.username, account.password);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Login
    // Request contains account json, no id. if success response body should contain json of account including id with 200 which is default
    // If login is unsuccessful response status should be 401

    public Account login(Account account)
    {
        Connection conn = ConnectionUtil.getConnection();

        try 
        {
            String findUserSQL = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement findUserStatement = conn.prepareStatement(findUserSQL, Statement.RETURN_GENERATED_KEYS);
            findUserStatement.setString(1, account.username);
            findUserStatement.setString(2, account.password);

            ResultSet foundUser = findUserStatement.executeQuery();

            if (foundUser.next())
            {
                Account foundAccount = new Account(
                    foundUser.getInt("account_id"),
                    foundUser.getString("username"),
                    foundUser.getString("password")
                );

                return foundAccount;
            }
        } 
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
