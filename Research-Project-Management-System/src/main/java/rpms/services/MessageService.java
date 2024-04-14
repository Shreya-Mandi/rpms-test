package rpms.services;

import rpms.dtos.MessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getMessages(Integer projectId);

    boolean addMessage(String username, Integer projectID, String content);
}
