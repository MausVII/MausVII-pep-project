package Service;

import Model.Message;

import java.util.ArrayList;

import DAO.MessageDAO;;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message insertMessage(Message message)
    {
        if (message.message_text == "" || message.message_text.length() > 255)
        {
            return null;
        }
        
        return this.messageDAO.insertMessage(message);
    }

    public ArrayList<Message> getAll()
    {
        return this.messageDAO.getAll();
    }

    public Message getByID(int id)
    {
        return this.messageDAO.getByID(id);
    }

    public Message delete(int id)
    {
        return this.messageDAO.delete(id);
    }

    public Message update(int id, String new_message)
    {
        if (new_message == "" || new_message.length() > 255)
        {
            return null;
        }

        return this.messageDAO.update(id, new_message);
    }

    public ArrayList<Message> getByUserId(int id)
    {
        return this.messageDAO.getByUserID(id);
    }
}
