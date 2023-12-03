package Controller;

import Service.AccountService;
import Service.MessageService;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

// Done
public class SocialMediaController {
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    
    /**
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/<id>", this::getMessageByIdHandler);
        app.get("/accounts/<id>/messages", this::getAllByUserIdHandler);
        app.delete("/messages/<id>", this::deleteMessageHandler);
        app.patch("/messages/<id>", this::updateMessageHandler);


        return app;
    }

    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
      
        if (registeredAccount != null)
        {
            context.json(mapper.writeValueAsString(registeredAccount));
        } 
        else
        {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedInAccount = accountService.login(account);

        if (loggedInAccount != null)
        {
            context.json(mapper.writeValueAsString(loggedInAccount));
        }
        else
        {
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message insertedMessage = messageService.insertMessage(message);

        if (insertedMessage != null)
        {
            context.json(mapper.writeValueAsString(insertedMessage));
        }
        else
        {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ArrayList<Message> messages = messageService.getAll();
        context.json(messages, Message.class);
    }

    private void getMessageByIdHandler(Context context) {
        Message message = messageService.getByID(Integer.valueOf(context.pathParam("id")));
        if (message != null) {
            context.json(message, Message.class);
        }
    }

    private void deleteMessageHandler(Context context) {
        Message message = messageService.getByID(Integer.valueOf(context.pathParam("id")));
        if (message != null) {
            context.json(message, Message.class);
        }
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> body = mapper.readValue(context.body(), Map.class);
        Message message = messageService.update(Integer.valueOf(context.pathParam("id")), body.get("message_text"));
        if (message != null) {
            context.json(message, Message.class);
        } else {
            context.status(400);
        }
    }

    private void getAllByUserIdHandler(Context context) {
        ArrayList<Message> messages = messageService.getByUserId(Integer.valueOf(context.pathParam("id")));
        context.json(messages, Message.class);
    }
}