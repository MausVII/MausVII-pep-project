package Service;

import Model.Message;
import DAO.MessageDAO;;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message insertMessage(Message message)
    {
        return this.messageDAO.insertMessage(message);
    }
}
