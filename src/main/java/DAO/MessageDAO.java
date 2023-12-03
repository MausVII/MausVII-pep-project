package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public Message insertMessage(Message message)
    {
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

    public ArrayList<Message> getAll()
    {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String getMessagesSQL = "SELECT * FROM message";
            PreparedStatement getMessagesStatement = conn.prepareStatement(getMessagesSQL);

            ResultSet allMessages = getMessagesStatement.executeQuery();

            ArrayList<Message> messages = new ArrayList<Message>();

            while (allMessages.next()) {
                messages.add(new Message(
                    allMessages.getInt("message_id"), 
                    allMessages.getInt("posted_by"),
                    allMessages.getString("message_text"), 
                    allMessages.getLong("time_posted_epoch")
                    ));
            }

            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<Message>();
    }

    public Message getByID(int id)
    {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String getMessageSQL = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement getMessageStatement = conn.prepareStatement(getMessageSQL);
            getMessageStatement.setInt(1, id);

            ResultSet foundMessage = getMessageStatement.executeQuery();

            if (foundMessage.next()) {
                Message message = new Message(
                    foundMessage.getInt("message_id"), 
                    foundMessage.getInt("posted_by"),
                    foundMessage.getString("message_text"), 
                    foundMessage.getLong("time_posted_epoch")
                    );
                return message;
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message delete(int id)
    {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String getMessageSQL = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement getMessageStatement = conn.prepareStatement(getMessageSQL);
            getMessageStatement.setInt(1, id);

            ResultSet foundMessage = getMessageStatement.executeQuery();

            if (foundMessage.next()) {
                String deleteMessageSQL = "DELETE * FROM message WHERE message_id = ?";
                PreparedStatement deleteMessageStatement = conn.prepareStatement(deleteMessageSQL);
                deleteMessageStatement.setInt(1, id);

                Message message = new Message(
                    foundMessage.getInt("message_id"), 
                    foundMessage.getInt("posted_by"),
                    foundMessage.getString("message_text"), 
                    foundMessage.getLong("time_posted_epoch")
                    );

                return message;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message update(int id, String new_message)
    {
        Connection conn = ConnectionUtil.getConnection();

        // Verify account exists
        try {
            String findMessageSQL = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement findMessageStatement = conn.prepareStatement(findMessageSQL);
            findMessageStatement.setInt(1, id);

            ResultSet foundMessage = findMessageStatement.executeQuery();

            // If user is valid post message
            if (foundMessage.next())
            {
                String updateMessageSQL = "UPDATE message SET message_text = ?";
                PreparedStatement updateMessageStatement = conn.prepareStatement(updateMessageSQL, Statement.RETURN_GENERATED_KEYS);
                updateMessageStatement.setString(1, new_message);

                updateMessageStatement.executeUpdate();

                return new Message(id, foundMessage.getInt("posted_by"), new_message, foundMessage.getLong("time_posted_epoch"));
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

    public ArrayList<Message> getByUserID(int id)
    {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String getMessageSQL = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message JOIN account ON posted_by = account_id WHERE account_id = ?";
            PreparedStatement getMessageStatement = conn.prepareStatement(getMessageSQL);
            getMessageStatement.setInt(1, id);

            ResultSet foundMessages = getMessageStatement.executeQuery();

            ArrayList<Message> messages = new ArrayList<Message>();

            while (foundMessages.next()) {
                messages.add(new Message(
                    foundMessages.getInt("message_id"), 
                    foundMessages.getInt("posted_by"),
                    foundMessages.getString("message_text"), 
                    foundMessages.getLong("time_posted_epoch")
                    ));
            }

            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<Message>();
    }
}
