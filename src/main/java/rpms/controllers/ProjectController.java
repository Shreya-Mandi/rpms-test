package rpms.controllers;

/*

GET /project/{projectId}/update

POST /project/{projectId}/update

*/

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
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 1");
            System.out.println("displayAllProjects(); GET /projects");
            return "error";
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
                return "redirect:/projects";
            }

            ProjectDTO projectDTO = projectService.getProject(projectId);
            if (projectDTO == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("displayProject(); GET /project/{projectId}");
                return "error";
            }

            List<String> studentNames = projectService.getStudentNames(projectId);
            if (studentNames == null) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 2");
                System.out.println("displayProject(); GET /project/{projectId}");
                return "error";
            }

            List<String> facultyNames = projectService.getFacultyNames(projectId);
            if (facultyNames == null) {
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
            return "redirect:/projects";
        }
    }

    @GetMapping("/project/create")
    public String displayProjectCreateForm(Model model) {
        List<String> studentUsernames = accountService.getStudentsAccepted();
        if (studentUsernames == null) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class 1");
            System.out.println("displayProjectCreateForm(); GET /project/create");
            return "error";
        }

        List<String> facultyUsernames = accountService.getFacultyAccepted();
        if (facultyUsernames == null) {
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
    public String createProject(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO, BindingResult bindingResult, @ModelAttribute("studentUsernames") UsernamesDTO studentUsernames, @ModelAttribute("facultyUsernames") UsernamesDTO facultyUsernames, Model model) {

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

        // this is jugaad

        List<String> studentUsernamesActual = new ArrayList<>();
        List<String> facultyUsernamesActual = new ArrayList<>();

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
        } else if (!accountService.isFaculty(sessionUsername) && !accountService.isStudent(sessionUsername)) {
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

        return "redirect:/projects";
    }

    @GetMapping("/project/{projectId}/update")
    public String displayProjectUpdateForm(@PathVariable Integer projectId, Model model) {
        return "index";
    }

    @PostMapping("/project/{projectId}/update")
    public String updateProject(@PathVariable String projectId) {
        return "index";
    }

    @GetMapping("/project/{projectId}/delete")
    public String deleteProject(@PathVariable String projectId) {
        if (projectService.isProjectPresent(Integer.parseInt(projectId)) &&
                projectService.isAccountInProject(accountService.getSessionAccount(), Integer.parseInt(projectId))) {
            if (!projectService.deleteProject(Integer.parseInt(projectId))) {
                System.out.println("Something Went Wrong!!");
                System.out.println("ProjectController.class 1");
                System.out.println("deleteProject(); GET /project/{projectId}/delete");
                return "error";
            }
        }
        return "redirect:/projects";
    }

    @PostMapping("/project/{projectId}/addMessage")
    public String addMessage(@PathVariable String projectId, @RequestParam String content) {
        if (projectService.isProjectPresent(Integer.parseInt(projectId))) {
            String username = accountService.getSessionAccount();
            if (projectService.isAccountInProject(username, Integer.parseInt(projectId))) {
                if (!messageService.addMessage(username, Integer.parseInt(projectId), content)) {
                    System.out.println("Something Went Wrong!!");
                    System.out.println("ProjectController.class 1");
                    System.out.println("addMessage(); POST /project/{projectId}/addMessage");
                    return "error";
                }
            }
            return "redirect:/project/{projectId}";
        } else {
            return "redirect:/projects";
        }
    }
}

/*

    @GetMapping("/project/{projectId}/update")
    public String showUpdateForm(@PathVariable Integer projectId, Model model) {
        if (!projectService.isProjectPresent(projectId)) {
            System.out.println("Project Id does not exist!");
            return "projects-dashboard";
        }
        ProjectDTO projectDTO = projectService.getProject(projectId);
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("studentUsernames", projectService.getStudentNames(projectId));
        model.addAttribute("facultyUsernames", projectService.getFacultyNames(projectId));
        return "project-update";
    }

    @PostMapping("/project/{projectId}/update")
    public String updateProject(@PathVariable Integer projectId, @ModelAttribute @Valid ProjectDTO projectDTO, BindingResult bindingResult, @RequestParam List<String> studentUsernames, @RequestParam List<String> facultyUsernames) {
        if (bindingResult.hasErrors()) {
            return "project-update";
        }
        projectDTO.setId(projectId); // TODO check if this is needed (Breakpoint and debug)
        if (!projectService.saveProject(projectDTO, studentUsernames, facultyUsernames)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class");
            System.out.println("updateProject(); POST /project/{projectId}/update");
        }
        return "redirect:/project/{projectId}";
    }

*/