package rpms.services;

import rpms.dtos.StudentDTO;
import rpms.models.Project;
import rpms.models.Student;

import java.util.List;

public interface StudentService {
    boolean saveStudent(Student student);

    boolean deleteStudent(String username);

    List<StudentDTO> getStudents(List<String> usernames);

    List<Project> getProjects(String username);

    List<Student> getStudentsRaw(List<String> usernames);
}
