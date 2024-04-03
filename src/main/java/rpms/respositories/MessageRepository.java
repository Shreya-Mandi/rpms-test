package rpms.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rpms.models.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
