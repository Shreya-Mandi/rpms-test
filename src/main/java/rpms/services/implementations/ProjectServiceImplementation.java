package rpms.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpms.dtos.ProjectDTO;
import rpms.mapper.ProjectMapper;
import rpms.models.Message;
import rpms.models.Project;
import rpms.models.enums.Status;
import rpms.respositories.ProjectRepository;
import rpms.services.AccountService;
import rpms.services.FacultyService;
import rpms.services.ProjectService;
import rpms.services.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImplementation implements ProjectService {
    private final ProjectRepository projectRepository;
    private final AccountService accountService;
    private final StudentService studentService;
    private final FacultyService facultyService;

    @Autowired
    public ProjectServiceImplementation(ProjectRepository projectRepository, AccountService accountService, StudentService studentService, FacultyService facultyService) {
        this.projectRepository = projectRepository;
        this.accountService = accountService;
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @Override
    public List<ProjectDTO> getProjects() {
        try {
            List<Project> projectList = projectRepository.findAllByStatusIs(Status.PUBLISHED);
            return projectList.stream().map(ProjectMapper::mapProjectToProjectDTO).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("List<ProjectDTO> getProjects()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ProjectDTO> getProjects(String username) {
        try {
            if (accountService.isAccountNotPresent(username))
                return null;

            List<Project> projectList;
            if (accountService.isStudent(username)) {
                projectList = studentService.getProjects(username);
            } else if (accountService.isFaculty(username)) {
                projectList = facultyService.getProjects(username);
            } else {
                return null;
            }
            return projectList.stream().map(ProjectMapper::mapProjectToProjectDTO).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("List<ProjectDTO> getProjects(String)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isAccountInProject(String username, Integer projectId) {
        try {
            List<String> studentUsernames = getStudentNames(projectId);
            List<String> facultyUsernames = getStudentNames(projectId);
            return studentUsernames.contains(username) || facultyUsernames.contains(username);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean isAccountInProject(String, Integer)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isProjectPresent(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.isPresent();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean isProjectPresent(Integer)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ProjectDTO getProject(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.map(ProjectMapper::mapProjectToProjectDTO).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("ProjectDTO getProject(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Project getProjectRaw(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("Project getProjectRaw(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getStudentNames(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.map(project ->
                    project.getStudentList().stream().map(student ->
                            student.getAccount().getFirstName() + " " + student.getAccount().getLastName()
                    ).toList()
            ).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("List<String> getStudentNames(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getFacultyNames(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.map(project ->
                    project.getFacultyList().stream().map(faculty ->
                            faculty.getAccount().getFirstName() + " " + faculty.getAccount().getLastName()
                    ).toList()
            ).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("List<String> getFacultyNames(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean saveProject(ProjectDTO projectDTO, List<String> studentUsernames, List<String> facultyUsernames) {
        try {
            Project project = ProjectMapper.mapProjectDTOToProject(projectDTO);
            for(String username: studentUsernames) {
                if (!accountService.isStudent(username))
                    return false;
            }
            for(String username: facultyUsernames) {
                if (!accountService.isFaculty(username))
                    return false;
            }
            project.setStudentList(studentService.getStudentsRaw(studentUsernames));
            project.setFacultyList(facultyService.getFacultyRaw(facultyUsernames));
            projectRepository.save(project);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean saveProject(ProjectDTO, List<String>, List<String>)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteProject(Integer projectId) {
        try {
            if (isProjectPresent(projectId)) {
                projectRepository.deleteById(projectId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean deleteProject(Integer)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Message> getMessages(Integer projectId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            return projectOptional.map(Project::getMessageList).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("List<Message> getMessages(Integer)");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
