package rpms.controllers;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rpms.dtos.MessageDTO;
import rpms.dtos.ProjectDTO;
import rpms.dtos.UsernamesDTO;
import rpms.models.enums.Status;
import rpms.services.AccountService;
import rpms.services.MessageService;
import rpms.services.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class ProjectController {
    final AccountService accountService;
    final ProjectService projectService;
    final MessageService messageService;

    @Autowired
    public ProjectController(AccountService accountService, ProjectService projectService, MessageService messageService) {
        this.accountService = accountService;
        this.projectService = projectService;
        this.messageService = messageService;

    }

    @GetMapping("/")
    public String home() {
        String username = accountService.getSessionAccount();
        if (username == null || accountService.isStudent(username) || accountService.isFaculty(username)) {
            return "redirect:/projects";
        } else {
            return "redirect:/adminDashboard";
        }
    }

    @GetMapping("/projects")
    public String displayAllProjects(Model model) {
        String sessionUsername = accountService.getSessionAccount();
        List<ProjectDTO> projectDTOList;
        if (sessionUsername == null) {
            projectDTOList = projectService.getProjects();
        } else if (accountService.isStudent(sessionUsername) || accountService.isFaculty(sessionUsername)) {
            projectDTOList = projectService.getProjects(sessionUsername);
        } else {
            return "redirect:/";
        }
        if (projectDTOList == null) {
            projectDTOList = new ArrayList<>();
        }

        model.addAttribute("projectDTOList", projectDTOList);
        model.addAttribute("isGuest", (sessionUsername == null));
        return "projects-dashboard";
    }

    @GetMapping("/project/{projectId}")
    public String displayProject(@PathVariable Integer projectId, Model model) {
        if (projectService.isProjectPresent(projectId)) {
            String sessionUsername = accountService.getSessionAccount();
            if ((sessionUsername == null && projectService.getProject(projectId).getStatus() != Status.PUBLISHED) ||
                    (sessionUsername != null && projectService.isAccountNotInProject(sessionUsername, projectId))) {
                return "redirect:/";
            }

            ProjectDTO projectDTO = projectService.getProject(projectId);
            if (projectDTO == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("displayProject(); GET /project/{projectId}");
                return "error";
            }

            List<String> studentNames = projectService.getStudentNames(projectId);
            if (studentNames == null || studentNames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 2");
                System.out.println("displayProject(); GET /project/{projectId}");
                return "error";
            }

            List<String> facultyNames = projectService.getFacultyNames(projectId);
            if (facultyNames == null || facultyNames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 3");
                System.out.println("displayProject(); GET /project/{projectId}");
                return "error";
            }

            List<MessageDTO> messageDTOList = messageService.getMessages(projectId);
            if (messageDTOList == null) {
                messageDTOList = new ArrayList<>();
            }

            model.addAttribute("projectDTO", projectDTO);
            model.addAttribute("studentNames", new UsernamesDTO(studentNames));
            model.addAttribute("facultyNames", new UsernamesDTO(facultyNames));
            model.addAttribute("isGuest", (sessionUsername == null));
            if (sessionUsername != null && projectService.isAccountInProject(sessionUsername, projectId)) {
                model.addAttribute("messages", messageDTOList);
            }
            return "project-read";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/project/create")
    public String displayProjectCreateForm(Model model) {
        List<String> studentUsernames = accountService.getStudentsAccepted();
        if (studentUsernames == null || studentUsernames.isEmpty()) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 1");
            System.out.println("displayProjectCreateForm(); GET /project/create");
            return "error";
        }

        List<String> facultyUsernames = accountService.getFacultyAccepted();
        if (facultyUsernames == null || facultyUsernames.isEmpty()) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 2");
            System.out.println("displayProjectCreateForm(); GET /project/create");
            return "error";
        }

        model.addAttribute("projectDTO", new ProjectDTO());
        model.addAttribute("studentUsernames", new UsernamesDTO(studentUsernames));
        model.addAttribute("facultyUsernames", new UsernamesDTO(facultyUsernames));
        return "project-create";
    }

    @PostMapping("/project/create")
    public String createProject(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO, BindingResult bindingResult,
                                @ModelAttribute("studentUsernames") UsernamesDTO studentUsernames,
                                @ModelAttribute("facultyUsernames") UsernamesDTO facultyUsernames,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectDTO", projectDTO);

            List<String> studentUsernamesNew = accountService.getStudentsAccepted();
            if (studentUsernames == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("createProject(); POST /project/create");
                return "error";
            }

            List<String> facultyUsernamesNew = accountService.getFacultyAccepted();
            if (facultyUsernames == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 2");
                System.out.println("createProject(); POST /project/create");
                return "error";
            }

            model.addAttribute("projectDTO", projectDTO);
            model.addAttribute("studentUsernames", new UsernamesDTO(studentUsernamesNew));
            model.addAttribute("facultyUsernames", new UsernamesDTO(facultyUsernamesNew));
            return "project-create";
        }

        String sessionUsername = accountService.getSessionAccount();
        if (sessionUsername == null) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 3");
            System.out.println("createProject(); POST /project/create");
            return "error";
        }

        // this is jugaad, can't fix, don't change

        List<String> studentUsernamesActual = new ArrayList<>();
        List<String> facultyUsernamesActual = new ArrayList<>();

        if (!jugaadHelper(sessionUsername, studentUsernames, facultyUsernames, studentUsernamesActual, facultyUsernamesActual)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 4");
            System.out.println("createProject(); POST /project/create");
            return "error";
        }

        if (!projectService.saveProject(projectDTO, studentUsernamesActual, facultyUsernamesActual)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 5");
            System.out.println("createProject(); POST /project/create");
            return "error";
        }

        return "redirect:/";
    }

    @GetMapping("/project/{projectId}/update")
    public String displayProjectUpdateForm(@PathVariable Integer projectId, Model model) {
        if (projectService.isProjectPresent(projectId) && projectService.isAccountInProject(accountService.getSessionAccount(), projectId)) {
            List<String> studentUsernames = accountService.getStudentsAccepted();
            if (studentUsernames == null || studentUsernames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("displayProjectUpdateForm(); GET /project/{projectId}/update");
                return "error";
            }

            List<String> facultyUsernames = accountService.getFacultyAccepted();
            if (facultyUsernames == null || facultyUsernames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 2");
                System.out.println("displayProjectUpdateForm(); GET /project/{projectId}/update");
                return "error";
            }

            List<String> selectedStudentUsernames = projectService.getStudentNames(projectId);
            if (selectedStudentUsernames == null || selectedStudentUsernames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 3");
                System.out.println("displayProjectUpdateForm(); GET /project/{projectId}/update");
                return "error";
            }

            List<String> selectedFacultyUsernames = projectService.getFacultyNames(projectId);
            if (selectedFacultyUsernames == null || selectedFacultyUsernames.isEmpty()) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 4");
                System.out.println("displayProjectUpdateForm(); GET /project/{projectId}/update");
                return "error";
            }

            model.addAttribute("projectDTO", projectService.getProject(projectId));
            model.addAttribute("studentUsernames", new UsernamesDTO(studentUsernames));
            model.addAttribute("selectedStudentUsernames", new UsernamesDTO(selectedStudentUsernames));
            model.addAttribute("facultyUsernames", new UsernamesDTO(facultyUsernames));
            model.addAttribute("selectedFacultyUsernames", new UsernamesDTO(selectedFacultyUsernames));
            return "project-update";
        }
        return "redirect:/";
    }

    @PostMapping("/project/{projectId}/update")
    public String updateProject(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO, BindingResult bindingResult,
                                @PathVariable String projectId,
                                @ModelAttribute("studentUsernames") UsernamesDTO studentUsernames,
                                @ModelAttribute("facultyUsernames") UsernamesDTO facultyUsernames,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectDTO", projectDTO);

            List<String> studentUsernamesNew = accountService.getStudentsAccepted();
            if (studentUsernames == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("updateProject(); POST /project/{projectId}/update");
                return "error";
            }

            List<String> facultyUsernamesNew = accountService.getFacultyAccepted();
            if (facultyUsernames == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 2");
                System.out.println("updateProject(); POST /project/{projectId}/update");
                return "error";
            }

            model.addAttribute("projectDTO", projectDTO);
            model.addAttribute("studentUsernames", new UsernamesDTO(studentUsernamesNew));
            model.addAttribute("selectedStudentUsernames", studentUsernames);
            model.addAttribute("facultyUsernames", new UsernamesDTO(facultyUsernamesNew));
            model.addAttribute("selectedFacultyUsernames", facultyUsernames);
            return "project-update";
        }

        String sessionUsername = accountService.getSessionAccount();
        if (sessionUsername == null) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 3");
            System.out.println("updateProject(); POST /project/{projectId}/update");
            return "error";
        }

        // this is jugaad, can't fix, don't change

        List<String> studentUsernamesActual = new ArrayList<>();
        List<String> facultyUsernamesActual = new ArrayList<>();

        if (!jugaadHelper(sessionUsername, studentUsernames, facultyUsernames, studentUsernamesActual, facultyUsernamesActual)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 4");
            System.out.println("updateProject(); POST /project/{projectId}/update");
            return "error";
        }

        projectDTO.setId(Integer.parseInt(projectId));

        if (!projectService.saveProject(projectDTO, studentUsernamesActual, facultyUsernamesActual)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 5");
            System.out.println("updateProject(); POST /project/{projectId}/update");
            return "error";
        }

        return "redirect:/project/{projectId}";
    }

    @GetMapping("/project/{projectId}/delete")
    public String deleteProject(@PathVariable String projectId) {
        if (projectService.isProjectPresent(Integer.parseInt(projectId)) &&
                (projectService.isAccountInProject(accountService.getSessionAccount(), Integer.parseInt(projectId)) ||
                        accountService.isAdmin(accountService.getSessionAccount()))) {
            if (!projectService.deleteProject(Integer.parseInt(projectId))) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("deleteProject(); GET /project/{projectId}/delete");
                return "error";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/project/{projectId}/addMessage")
    public String addMessage(@PathVariable String projectId, @RequestParam String content) {
        if (projectService.isProjectPresent(Integer.parseInt(projectId))) {
            String username = accountService.getSessionAccount();
            if (username != null && projectService.isAccountInProject(username, Integer.parseInt(projectId))) {
                if (!messageService.addMessage(username, Integer.parseInt(projectId), content)) {
                    System.out.println("Something Went Wrong!!");
                    System.out.println("ProjectController.class 1");
                    System.out.println("addMessage(); POST /project/{projectId}/addMessage");
                    return "error";
                }
            }
            return "redirect:/project/{projectId}";
        } else {
            return "redirect:/";
        }
    }

    private boolean jugaadHelper(String sessionUsername, UsernamesDTO studentUsernames, UsernamesDTO facultyUsernames, List<String> studentUsernamesActual, List<String> facultyUsernamesActual) {
        for (String username : Stream.concat(studentUsernames.getUsernames().stream(), facultyUsernames.getUsernames().stream()).toList()) {
            if (accountService.isStudent(username) && !studentUsernamesActual.contains(username)) {
                studentUsernamesActual.add(username);
            }
            if (accountService.isFaculty(username) && !facultyUsernamesActual.contains(username)) {
                facultyUsernamesActual.add(username);
            }
        }

        if (accountService.isFaculty(sessionUsername) && !facultyUsernamesActual.contains(sessionUsername)) {
            facultyUsernamesActual.add(sessionUsername);
        } else if (accountService.isStudent(sessionUsername) && !studentUsernamesActual.contains(sessionUsername)) {
            studentUsernamesActual.add(sessionUsername);
        } else return accountService.isFaculty(sessionUsername) || accountService.isStudent(sessionUsername);

        return true;
    }
}