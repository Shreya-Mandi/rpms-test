package rpms.controllers;

/*

TODO GET /login // Do Last

-done-GET /registerStudent

-done-POST /registerStudent

-done-GET /registerFaculty

-done-POST /registerFaculty

-done-GET /adminDashboard

-done-POST /adminDashboard/{username}/accept

-done-POST /adminDashboard/{username}/reject

*/

@Controller
public class AccountController {
    AccountService accountService;

    @Autowired
    public AccountController(AccountController accountController) {
        this.accountService = accountService;
    }

    @GetMapping("/registerStudent")
    public String getRegisterStudent() {
        return "register-student";
    }

    @PostMapping("/registerStudent")
    public String postRegisterStudent(@ModelAttribute RegistrationStudentDTO registrationStudentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-student";
        }

        if (accountService.isAccountNotPresent(registrationStudentDTO.username)) {
            if (!accountService.saveAccount(registrationStudentDTO)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class");
                System.out.println("postRegisterStudent(); POST /registerStudent");
            }

        } else {
            System.out.println("Account already present");
        }
        return "/login?registered";
    }

    @GetMapping("/registerFaculty")
    public String getRegisterFaculty() {
        return "register-faculty";
    }

    @PostMapping("/registerFaculty")
    public String postRegisterFaculty(@ModelAttribute RegistrationFacultytDTO registrationFacultyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-student";
        }
        if (accountService.isAccountNotPresent(registrationFacultyDTO.username)) {
            if (!accountService.saveAccount(registrationFacultyDTO)) {
                System.out.println("Something Went Wrong!!");
                System.out.println("AccountController.class");
                System.out.println("postRegisterFaculty(); POST /registerFaculty");
            }
        } else {
            System.out.println("Account already present");
        }
        return "/login?registered";
    }

    @GetMapping("/adminDashboard")
    public String getAdminDashboard(Model model) {
        List<StudentDTO> studentDTOList = accountService.getStudentsPending();
        List<FacultyDTO> facultyDTOList = accountService.getFacultyPending();

        model.addAttribute("studentDTOList", studentDTOList);
        model.addAttribute("facultyDTOList", facultyDTOList);

        return "admin-dashboard";
    }

    @PostMapping("adminDashboard/{username}/accept")
    public String postAdminAccept(@PathVariable String username) {
        if (accountService.isAccounrPresent(username)) {
            accountService.acceptAccount(username);
        } else {
            System.out.println("Cannot find username");
        }
        return "admin-dashboard";
    }

    @PostMapping("POST /adminDashboard/{username}/reject")
    public String postAdminReject(@PathVariable String username) {
        if (accountService.isAccounrPresent(username)) {
            accountService.rejectAccount(username);
        } else {
            System.out.println("Cannot find username");
        }
        return "admin-dashboard";
    }
}
