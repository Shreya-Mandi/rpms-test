package rpms.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rpms.dtos.FacultyDTO;
import rpms.dtos.StudentDTO;
import rpms.dtos.RegistrationStudentDTO;
import rpms.dtos.RegistrationFacultyDTO;
import rpms.services.AccountService;

import java.util.List;

@Controller
public class AccountController {
    final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registerStudent")
    public String displayStudentRegistrationForm(Model model) {
        model.addAttribute("registrationStudentDTO", new RegistrationStudentDTO());
        return "register-student";
    }

    @PostMapping("/registerStudent")
    public String registerStudent(@Valid @ModelAttribute RegistrationStudentDTO registrationStudentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-student";
        }

        if (accountService.isAccountNotPresent(registrationStudentDTO.getUsername())) {
            if (!accountService.saveAccount(registrationStudentDTO)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class 1");
                System.out.println("registerStudent(); POST /registerStudent");
                return "error";
            }
            return "redirect:/login?registered";
        } else if (accountService.isAccountPresent(registrationStudentDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
            return "register-student";
        } else {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 2");
            System.out.println("registerStudent(); POST /registerStudent");
            return "error";
        }
    }

    @GetMapping("/registerFaculty")
    public String displayFacultyRegistrationForm(Model model) {
        model.addAttribute("registrationFacultyDTO", new RegistrationFacultyDTO());
        return "register-faculty";
    }

    @PostMapping("/registerFaculty")
    public String registerFaculty(@Valid @ModelAttribute RegistrationFacultyDTO registrationFacultyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-faculty";
        }
        if (accountService.isAccountNotPresent(registrationFacultyDTO.getUsername())) {
            if (!accountService.saveAccount(registrationFacultyDTO)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class 1");
                System.out.println("registerFaculty(); POST /registerFaculty");
                return "error";
            }
            return "redirect:/login?registered";
        } else if (accountService.isAccountNotPresent(registrationFacultyDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
            return "register-faculty";
        } else {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 2");
            System.out.println("registerFaculty(); POST /registerFaculty");
            return "error";
        }
    }

    @GetMapping("/adminDashboard")
    public String displayAdminDashboard(Model model) {
        List<StudentDTO> studentDTOList = accountService.getStudentsPending();
        if (studentDTOList == null) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 1");
            System.out.println("displayAdminDashboard(); GET /adminDashboard");
            return "error";
        }

        List<FacultyDTO> facultyDTOList = accountService.getFacultyPending();
        if (facultyDTOList == null) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 2");
            System.out.println("displayAdminDashboard(); GET /adminDashboard");
            return "error";
        }

        model.addAttribute("studentDTOList", studentDTOList);
        model.addAttribute("facultyDTOList", facultyDTOList);

        return "admin-dashboard";
    }

    @GetMapping("/adminDashboard/{username}/accept")
    public String acceptUser(@PathVariable String username) {
        if (accountService.isAccountPresent(username)) {
            if (!accountService.acceptAccount(username)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class 1");
                System.out.println("acceptUser(); GET /adminDashboard/{username}/accept");
                return "error";
            }
            return "redirect:/adminDashboard";
        } else if (accountService.isAccountNotPresent(username)) {
            System.out.println("Cannot find username");
            return "error";
        } else {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 2");
            System.out.println("acceptUser(); GET /adminDashboard/{username}/accept");
            return "error";
        }
    }

    @GetMapping("/adminDashboard/{username}/reject")
    public String rejectUser(@PathVariable String username) {
        if (accountService.isAccountPresent(username)) {
            if (!accountService.rejectAccount(username)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class 1");
                System.out.println("rejectUser(); GET /adminDashboard/{username}/reject");
                return "error";
            }
            return "redirect:/adminDashboard";
        } else if (accountService.isAccountNotPresent(username)) {
            System.out.println("Cannot find username");
            return "error";
        } else {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountController.class 2");
            System.out.println("rejectUser(); GET /adminDashboard/{username}/reject");
            return "error";
        }
    }
}
