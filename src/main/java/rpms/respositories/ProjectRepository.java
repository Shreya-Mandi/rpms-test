package rpms.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rpms.models.Project;
import rpms.models.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findAllByStatusIs(Status status);
}
