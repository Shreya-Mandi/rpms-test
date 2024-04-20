package rpms.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpms.dtos.ProjectDTO;
import rpms.mapper.ProjectMapper;
import rpms.models.Faculty;
import rpms.models.Message;
import rpms.models.Project;
import rpms.models.Student;
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
            if (accountService.isAccountNotPresent(username))
                return false;

            if (isProjectPresent(projectId)) {
                List<String> studentUsernames = getProjectRaw(projectId).getStudentList().stream().map(Student::getId).toList();
                List<String> facultyUsernames = getProjectRaw(projectId).getFacultyList().stream().map(Faculty::getId).toList();
                return studentUsernames.contains(username) || facultyUsernames.contains(username);
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean isAccountInProject(String, Integer)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAccountNotInProject(String username, Integer projectId) {
        try {
            if (accountService.isAccountNotPresent(username))
                return true;

            if (isProjectPresent(projectId)) {
                List<String> studentUsernames = getProjectRaw(projectId).getStudentList().stream().map(Student::getId).toList();
                List<String> facultyUsernames = getProjectRaw(projectId).getFacultyList().stream().map(Faculty::getId).toList();
                return !studentUsernames.contains(username) && !facultyUsernames.contains(username);
            } else {
                return true;
            }
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
            for (String username : studentUsernames) {
                if (!accountService.isStudent(username))
                    return false;
            }
            for (String username : facultyUsernames) {
                if (!accountService.isFaculty(username))
                    return false;
            }

            if (project.getId() != null) {
                Project oldProject = getProjectRaw(project.getId());

                List<Student> studentList = oldProject.getStudentList();
                if (studentList == null || studentList.isEmpty())
                    return false;

                List<Faculty> facultyList = oldProject.getFacultyList();
                if (facultyList == null || facultyList.isEmpty())
                    return false;

                for (Student student : studentList) {
                    student.getProjectList().remove(oldProject);
                }
                for (Faculty faculty : facultyList) {
                    faculty.getProjectList().remove(oldProject);
                }
                oldProject.getStudentList().clear();
                oldProject.getFacultyList().clear();

                oldProject.setTitle(project.getTitle());
                oldProject.setStartDate(project.getStartDate());
                oldProject.setEndDate(project.getEndDate());
                oldProject.setStatus(project.getStatus());

                return helper1(oldProject, studentUsernames, facultyUsernames);
            }

            return helper1(project, studentUsernames, facultyUsernames);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("ProjectServiceImplementation.class");
            System.out.println("boolean saveProject(ProjectDTO, List<String>, List<String>)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean helper1(Project project, List<String> studentUsernames, List<String> facultyUsernames) {
        List<Student> studentList = studentService.getStudentsRaw(studentUsernames);
        if (studentList == null || studentList.size() != studentUsernames.size())
            return false;

        List<Faculty> facultyList = facultyService.getFacultyRaw(facultyUsernames);
        if (facultyList == null || facultyList.size() != facultyUsernames.size())
            return false;

        for (Student student : studentList) {
            student.getProjectList().add(project);
        }
        for (Faculty faculty : facultyList) {
            faculty.getProjectList().add(project);
        }

        project.setStudentList(studentList);
        project.setFacultyList(facultyList);

        projectRepository.save(project);
        return true;
    }

    @Override
    public boolean deleteProject(Integer projectId) {
        try {
            if (isProjectPresent(projectId)) {
                projectRepository.findById(projectId).ifPresent(project -> {
                    project.getStudentList().forEach(student -> student.getProjectList().remove(project));
                    project.getFacultyList().forEach(faculty -> faculty.getProjectList().remove(project));
                });
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
