package rpms.mapper;

import rpms.dtos.MessageDTO;
import rpms.models.Message;

public class MessageMapper {
    public static MessageDTO mapMessageToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setName(message.getAccount().getFirstName() + " " + message.getAccount().getLastName());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        return messageDTO;
    }
}
