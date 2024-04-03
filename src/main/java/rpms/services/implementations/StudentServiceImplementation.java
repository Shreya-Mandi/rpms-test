package rpms.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpms.dtos.StudentDTO;
import rpms.mapper.StudentMapper;
import rpms.models.Project;
import rpms.models.Student;
import rpms.respositories.StudentRepository;
import rpms.services.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImplementation implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImplementation(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean saveStudent(Student student) {
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("StudentServiceImplementation.class");
            System.out.println("boolean saveStudent(Student)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteStudent(String username) {
        try {
            studentRepository.deleteById(username);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("StudentServiceImplementation.class");
            System.out.println("boolean deleteStudent(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<StudentDTO> getStudents(List<String> usernames) {
        try {
            List<Student> studentList = studentRepository.findAllById(usernames);
            return studentList.stream().map(StudentMapper::mapStudentToStudentDTO).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("StudentServiceImplementation.class");
            System.out.println("List<StudentDTO> getStudents(List<String>)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Project> getProjects(String username) {
        try {
            Optional<Student> studentOptional = studentRepository.findById(username);
            return studentOptional.map(Student::getProjectList).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("StudentServiceImplementation.class");
            System.out.println("List<Project> getProjects(String)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Student> getStudentsRaw(List<String> usernames) {
        try {
            return studentRepository.findAllById(usernames);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("StudentServiceImplementation.class");
            System.out.println("List<Student> getStudentsRaw(List<String>)");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
