package rpms.services.implementations;

import org.springframework.stereotype.Service;
import rpms.dtos.MessageDTO;
import rpms.mapper.MessageMapper;
import rpms.models.Message;
import rpms.respositories.MessageRepository;
import rpms.services.AccountService;
import rpms.services.MessageService;
import rpms.services.ProjectService;

import java.util.List;

@Service
public class MessageServiceImplementation implements MessageService {
    private final MessageRepository messageRepository;
    private final ProjectService projectService;
    private final AccountService accountService;

    public MessageServiceImplementation(MessageRepository messageRepository, ProjectService projectService, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.projectService = projectService;
        this.accountService = accountService;
    }

    @Override
    public List<MessageDTO> getMessages(Integer projectId) {
        try {
            if (projectService.isProjectPresent(projectId)) {
                List<Message> messageList = projectService.getMessages(projectId);
                return messageList.stream().map(MessageMapper::mapMessageToMessageDTO).toList();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("MessageServiceImplementation.class");
            System.out.println("List<MessageDTO> getMessages(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean addMessage(String username, Integer projectID, String content) {
        try {
            Message message = new Message();
            if (projectService.isProjectPresent(projectID)) {
                message.setProject(projectService.getProjectRaw(projectID));
            } else {
                return false;
            }
            if (accountService.isAccountPresent(username)) {
                message.setAccount(accountService.getAccountRaw(username));
            } else {
                return false;
            }
            message.setContent(content);
            messageRepository.save(message);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("MessageServiceImplementation.class");
            System.out.println("boolean addMessage(String, Integer, String)");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
