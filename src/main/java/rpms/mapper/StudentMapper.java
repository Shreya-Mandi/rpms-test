package rpms.mapper;

import rpms.dtos.RegistrationStudentDTO;
import rpms.dtos.StudentDTO;
import rpms.models.Student;

public class StudentMapper {
    public static Student mapRegistrationToStudent(RegistrationStudentDTO registrationStudentDTO) {
        Student student = new Student();
        student.setId(registrationStudentDTO.getUsername());
        student.setDepartment(registrationStudentDTO.getDepartment());
        student.setSemester(registrationStudentDTO.getSemester());
        student.setSection(registrationStudentDTO.getSection().toUpperCase().toCharArray()[0]);
        student.setAccount(null);
        student.setProjectList(null);
        return student;
    }

    public static StudentDTO mapStudentToStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUsername(student.getId());
        studentDTO.setFirstName(student.getAccount().getFirstName());
        studentDTO.setLastName(student.getAccount().getLastName());
        studentDTO.setDepartment(student.getDepartment());
        studentDTO.setSection(student.getSection());
        studentDTO.setSemester(student.getSemester());
        return studentDTO;
    }
}
