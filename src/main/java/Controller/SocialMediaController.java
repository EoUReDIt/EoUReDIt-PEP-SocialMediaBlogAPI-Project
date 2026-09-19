package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private AccountService accountService;
    private MessageService messageService;
    

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // TODO 1: register the remaining routes. 
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        
        // Completed Routes:
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        
        // Standard REST route for getting messages tied to a specific account
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    private void registerHandler(Context ctx) {
        Account accountFromRequest = ctx.bodyAsClass(Account.class);
        Account registeredAccount = accountService.register(accountFromRequest);

        if (registeredAccount == null) {
            // TODO 2: two things need to happen on failure: set the status
            // code to 400, and make sure NOTHING is written to the body

            // Solution: set the status. By not calling ctx.json(), the body remains empty.
            ctx.status(400);
        } else {
            ctx.json(registeredAccount);
        }
    }

    private void loginHandler(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(credentials);

        // TODO 3: write the success/failure branching Remember:

        // failure here is 401

        // Solution: 
        if (loggedInAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(loggedInAccount);
        }
    }

    private void createMessageHandler(Context ctx) {
        Message messageFromRequest = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(messageFromRequest);

        if (createdMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(createdMessage);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) {
        // TODO 4: extract the path param and convert it to an int.

        // Solution: The string here must exactly match what is inside the {} in API()
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(messageId);

        // TODO 5: This is the "200 + empty body when not found" case. 

        // Solution: ctx.result("") guarantees a completely empty body instead of the string "null"
        if (message == null) {
            ctx.result("");
        } else {
            ctx.json(message);
        }
    }

    private void deleteMessageHandler(Context ctx) {
        // TODO 6: implement this handler.

        // "empty body if null" pattern as getMessageByIdHandler

        // Solution:
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        
        // Assuming the service returns the deleted message if successful, or null if it didn't exist
        Message deletedMessage = messageService.deleteMessageById(messageId);
        
        if (deletedMessage == null) {
            ctx.result(""); // Returns 200 OK but with an empty body
        } else {
            ctx.json(deletedMessage); // Returns 200 OK with the deleted message in the body
        }
    }

    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        // TODO 7: the request body only contains `message_text`, not a full

        // Message object. Extract just the text needed from the object.

        // Solution:
        Message messageFromRequest = ctx.bodyAsClass(Message.class);

        Message updatedMessage = messageService.updateMessageText(messageId, messageFromRequest.getMessage_text());

        // TODO 8: this endpoint's failure case is 400 (unlike GET/DELETE by

        // id, which use 200+empty). Write the branching.

        // Solution:
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(updatedMessage);
        }
    }

    private void getMessagesByAccountHandler(Context ctx) {
        // TODO 9: implement this whole handler. Path param name should match
        
        // Solution:
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        
        // Assuming the service has a method named getMessagesByAccountId
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        
        ctx.json(messages);
    }
}