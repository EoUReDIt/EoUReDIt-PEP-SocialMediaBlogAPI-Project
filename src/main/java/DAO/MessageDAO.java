package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * Insert a new message, return it with the generated message_id filled in.
     * This should mirror AccountDAO.insertAccount — same pattern, different table.
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            // TODO 1: request generated keys
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());;

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return every row in the message table, as a List. Empty list (not null!)
     * if the table has no rows — check what RetrieveAllMessagesTest expects.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            // TODO 3: Looping
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * @return the message with this id, or null if it doesn't exist.
     */
    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            // need: message_id, posted_by, message_text, time_posted_epoch.
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return new Message(rs.getInt("message_id"),
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch")
                ); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete the message with this id and return the row AS IT WAS before deletion.
     * Not found → return null.
     *
     * Design question: you need the row's data to return it, but DELETE doesn't
     * give you back what it deleted. What two-statement approach solves this?
     */
    public Message deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // TODO 5: Step 1 — fetch the message first (reuse getMessageById logic),
            Message existing = getMessageById(messageId);
            if (existing == null) {
                return null;
            }

            // TODO 6: Step 2 — now run the DELETE with a WHERE message_id = ? clause.
            String sql = "DELETE * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();

            return existing;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update message_text for an existing message and return the updated row.
     * Assumes the caller (Service layer) has already validated the new text.
     * If the message_id doesn't exist, return null — let the Service decide
     * that means a 400.
     */
    public Message updateMessageText(int messageId, String newText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newText);
            preparedStatement.setInt(2, messageId);

            // TODO 7: `executeUpdate()` returns an int — what does that int represent.
            //Validating that action was completed
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                return null;
            }
            return getMessageById(messageId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all messages posted by the given account_id. Empty list if none.
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            // TODO 8: write this whole method. Same loop pattern as getAllMessages,
            // but with a WHERE posted_by = ? filter.

            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            
            ResultSet rs = preparedStatement.executeQuery();

            // 2. Loop through the results just like in getAllMessages()
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}