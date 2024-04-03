package rpms.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rpms.models.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
}
