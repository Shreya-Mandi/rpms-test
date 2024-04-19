package rpms.mapper;

import rpms.dtos.ProjectDTO;
import rpms.models.Project;

public class ProjectMapper {
    public static ProjectDTO mapProjectToProjectDTO(Project project){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setTitle(project.getTitle());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        return projectDTO;
    }

    public static Project mapProjectDTOToProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setTitle(projectDTO.getTitle());
        project.setStatus(projectDTO.getStatus());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStudentList(null);
        project.setFacultyList(null);
        project.setMessageList(null);
        return project;
    }
}
