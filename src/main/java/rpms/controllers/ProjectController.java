package rpms.controllers;

/*

GET / -> REDIRECT /projects TODO

GET /projects TODO

GET /project/{projectId} TODO

GET /project/create

POST /project/create

GET /project/{projectId}/update

POST /project/{projectId}/update

POST /project/{projectId}/delete

POST /project/{projectId}/addMessage

*/

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rpms.dtos.ProjectDTO;
import rpms.models.Project;
import rpms.services.ProjectService;

import java.util.List;

@Controller
public class ProjectController {
    ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/create")
    public String projectCreateForm(Model model) {
        ProjectDTO projectDTO = new ProjectDTO();
        model.addAttribute("projectDTO", projectDTO);
        return "project-create";
    }

    @PostMapping("/project/create")
    public String createProject(@Valid @ModelAttribute ProjectDTO projectDTO, BindingResult bindingResult, @RequestParam List<String> studentUsernames, @RequestParam List<String> facultyUsernames) {
        if (bindingResult.hasErrors()) {
            return "project-create";
        }
        if (!projectService.saveProject(projectDTO, studentUsernames, facultyUsernames)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class");
            System.out.println("createProject(); POST /project/create");
        }
        return "projects-dashboard";
    }

    @GetMapping("/project/{projectId}/update")
    public String showUpdateForm(@PathVariable Integer projectId, Model model) {
        if (!projectService.isProjectPresent(projectId)) {
            return "error-404"; // FIXME - we dont have this
        }
        Project project = projectService.getProject(projectId); // TODO, USE ONLY THOSE FIELDS WHICH ARE IN PROJECT_DTO IN HTML FILE
        model.addAttribute("projectDTO", project);
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

    // FIXME (Check if necessary here, otherwise implement in html only) -  Assuming a simple confirmation page for deletion could be useful
    @GetMapping("/project/{projectId}/delete")
    public String confirmDelete(@PathVariable Integer projectId, Model model) {
        if (!projectService.isProjectPresent(projectId)) {
            return "error-404"; // FIXME - we dont have this
        }
        model.addAttribute("projectId", projectId);
        return "project-delete"; // TODO Create this if necessary only
    }

    @PostMapping("/project/{projectId}/delete")
    public String deleteProject(@PathVariable Integer projectId) {
        if (!projectService.isProjectPresent(projectId)) {
            return "error/404"; // FIXME - we dont have this
        }
        if (!projectService.deleteProject(projectId)) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class");
            System.out.println("deleteProject(); POST /project/{projectId}/delete");
        }
        return "redirect:/projects";
    }

    @PostMapping("/project/{projectId}/addMessage")
    public String addMessage(@PathVariable Integer projectId, @RequestParam String message) {
        if (!projectService.isProjectPresent(projectId)) {
            return "error/404"; // FIXME - we dont have this
        }
        // TODO use MessageService::addMessage(String username, Integer projectID, String content)
        return "redirect:/project/{projectId}";
    }
}
