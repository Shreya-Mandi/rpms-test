package rpms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import rpms.dtos.FacultyDTO;
import rpms.dtos.RegistrationFacultyDTO;
import rpms.dtos.RegistrationStudentDTO;
import rpms.dtos.StudentDTO;
import rpms.models.enums.Department;
import rpms.services.AccountService;

import java.util.List;

@Controller
public class TestController {

    private final AccountService accountService;

    @Autowired
    public TestController(AccountService accountService) {
        this.accountService = accountService;
    }

//    @GetMapping("/")
//    public String test1() {
//        String username = AccountService.getSessionAccount();
//        System.out.println(username);
//        return "index";
//    }
}

/*
        RegistrationStudentDTO studentDTO = new RegistrationStudentDTO();
        studentDTO.setUsername("PES2UG21CS547");
        studentDTO.setPassword("PES2UG21CS547");
        studentDTO.setFirstName("uh");
        studentDTO.setLastName("lol");
        studentDTO.setDepartment(Department.CSE);
        studentDTO.setSection('A');
        studentDTO.setSemester(4);
        accountService.saveAccount(studentDTO);

        RegistrationFacultyDTO facultyDTO = new RegistrationFacultyDTO();
        facultyDTO.setUsername("PES2CS547");
        facultyDTO.setPassword("PES2CS547");
        facultyDTO.setFirstName("uh");
        facultyDTO.setLastName("lol");
        facultyDTO.setDepartment(Department.ECE);
        facultyDTO.setDomain("AI");
        accountService.saveAccount(facultyDTO);

        accountService.acceptAccount("PES2CS542");
        accountService.rejectAccount("PES2UG21CS542");

        List<FacultyDTO> facultyDTOList = accountService.getFacultyPending();
        List<StudentDTO> studentDTOList = accountService.getStudentsPending();

        for(FacultyDTO facultyDTO1: facultyDTOList) {
            System.out.println(facultyDTO1);
        }

        for(StudentDTO studentDTO1: studentDTOList) {
            System.out.println(studentDTO1);
        }

        System.out.println(accountService.isStudent("PES2CS542"));
        System.out.println(accountService.isFaculty("test-student"));
*/