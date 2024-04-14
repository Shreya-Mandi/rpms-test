package rpms.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rpms.models.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
}
