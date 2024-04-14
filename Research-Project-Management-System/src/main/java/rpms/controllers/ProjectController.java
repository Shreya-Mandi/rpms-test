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
        ArrayList<String> studentUsernames = new ArrayList<String>;
        ArrayList<String> facultyUsernames = new ArrayList<String>;
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("studentUsernames", studentUsernames);
        model.addAttribute("facultyUsernames", facultyUsernames);
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

    // FIXME (Check if necessary here, otherwise implement in html only) -  Assuming a simple confirmation page for deletion could be useful
    @GetMapping("/project/{projectId}/delete")
    public String confirmDelete(@PathVariable Integer projectId, Model model) {
        if (!projectService.isProjectPresent(projectId)) {
            System.out.println("Project does not exist!");
            return "projects-dashboard";
        }
        else{
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectController.class");
            System.out.println("deleteProject(); POST /project/{projectId}/delete");
            return "projects-dashboard";
        }
//
//        model.addAttribute("projectId", projectId);
//        return "project-delete"; // TODO Create this if necessary only
    }

    @PostMapping("/project/{projectId}/addMessage")
    public String addMessage(@PathVariable Integer projectId, @RequestParam String message) {
        if (!projectService.isProjectPresent(projectId)) {
            System.out.println("Project does not exist!");
            return "projects-dashboard";
        }
        // TODO use MessageService::addMessage(String username, Integer projectID, String content)
        return "redirect:/project/{projectId}";
    }
}
