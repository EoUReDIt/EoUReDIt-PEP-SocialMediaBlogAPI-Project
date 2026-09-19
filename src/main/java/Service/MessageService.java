package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * Create a new message.
     *
     * Rules (derived from CreateMessageTest):
     *  - message_text must not be blank
     *  - message_text must be under 255 characters
     *  - posted_by must reference an account that actually exists
     *
     * @return the persisted Message if valid, otherwise null.
     */
    public Message createMessage(Message message) {
        // TODO 1: blank-text check.
        if (message.getMessage_text() == null || ___________________________) {
            return null;
        }

        // TODO 2: length check. Careful with the boundary: "under 255" means
        // 255 itself is INVALID. Which comparison operator enforces that
        // exactly (not >=, not >)?
        if (message.getMessage_text().length() ______ 255) {
            return null;
        }

        // TODO 3: does the posted_by account exist? You already wrote the DAO
        // method for this in AccountDAO — reuse it here via `accountDAO`.
        if (accountDAO._____________________________(message.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    /**
     * @return every message. This one has essentially no business rules —
     * think about why: is there any way this request could be "invalid"?
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * @return the message with this id, or null if not found.
     * (No validation needed — "not found" isn't an error here, per the spec:
     * the Controller returns 200 either way for this endpoint.)
     */
    public Message getMessageById(int messageId) {
        // TODO 4: one line — delegate to the DAO.
        return ___________________________________;
    }

    /**
     * Delete a message. Same "not found is not an error" logic as above.
     */
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    /**
     * Update a message's text.
     *
     * Rules (derived from UpdateMessageTextTest):
     *  - the message_id must already exist
     *  - the new text must not be blank
     *  - the new text must be under 255 characters
     *
     * Design question: this method needs to check "does the message exist"
     * BEFORE updating. Which DAO call from MessageDAO lets you check that,
     * and should you call it here in the Service, or trust MessageDAO's
     * updateMessageText to already return null on a missing id? Either design
     * can work — pick one and be able to explain why.
     *
     * @return the updated Message if valid, otherwise null.
     */
    public Message updateMessageText(int messageId, String newText) {
        // TODO 5: implement all three validation rules above, then call
        // messageDAO.updateMessageText(...) only if they all pass.
        return null;
    }

    /**
     * @return all messages posted by the given account. No validation rules —
     * an account with zero messages, or a nonexistent account_id, both just
     * mean "empty list," per RetrieveAllMessagesForUserTest.
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        // TODO 6: one line — delegate to the DAO.
        return ___________________________________;
    }
}