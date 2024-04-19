package rpms.services;

import rpms.dtos.ProjectDTO;
import rpms.models.Message;
import rpms.models.Project;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getProjects();

    List<ProjectDTO> getProjects(String username);

    boolean isAccountInProject(String username, Integer projectId);

    boolean isAccountNotInProject(String username, Integer projectId);

    boolean isProjectPresent(Integer projectId);

    ProjectDTO getProject(Integer projectId);

    Project getProjectRaw(Integer projectId);

    List<String> getStudentNames(Integer projectId);

    List<String> getFacultyNames(Integer projectId);

    boolean saveProject(ProjectDTO projectDTO, List<String> studentUsernames, List<String> facultyUsernames);

    boolean deleteProject(Integer projectId);

    List<Message> getMessages(Integer projectId);
}
