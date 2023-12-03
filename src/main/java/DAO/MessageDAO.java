package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

public class MessageDAO {
    // Create new message
    // Check message_text != blank, length < 255, posted_by reference real account
    public Message insertMessage(Message message)
    {
        if (message.message_text == "" || message.message_text.length() > 255)
        {
            return null;
        }

        Connection conn = ConnectionUtil.getConnection();

        // Verify account exists
        try {
            String findUserSQL = "SELECt * FROM account WHERE account_id = ?";
            PreparedStatement findUserStatement = conn.prepareStatement(findUserSQL, Statement.RETURN_GENERATED_KEYS);
            findUserStatement.setInt(1, message.posted_by);

            ResultSet foundUser = findUserStatement.executeQuery();

            // If user is valid post message
            if (foundUser.next())
            {
                String insertMessageSQL = "INSERT INTO message (message_text, posted_by, time_posted_epoch) VALUES (?, ?, ?)";
                PreparedStatement insertMessageStatement = conn.prepareStatement(insertMessageSQL, Statement.RETURN_GENERATED_KEYS);
                insertMessageStatement.setString(1, message.message_text);
                insertMessageStatement.setInt(2, message.posted_by);
                insertMessageStatement.setLong(3, message.time_posted_epoch);

                insertMessageStatement.executeUpdate();
                ResultSet insertedMessage = insertMessageStatement.getGeneratedKeys();

                if (insertedMessage.next())
                {
                    int generatedId = (int)insertedMessage.getLong(1);
                    return new Message(generatedId, message.posted_by, message.message_text, message.time_posted_epoch);
                }
            }
            else 
            {
                return null;
            }
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
