package rpms.services;

import rpms.dtos.FacultyDTO;
import rpms.models.Faculty;
import rpms.models.Project;

import java.util.List;

public interface FacultyService {
    boolean saveFaculty(Faculty faculty);

    boolean deleteFaculty(String username);

    List<FacultyDTO> getFaculty(List<String> usernames);

    List<Project> getProjects(String username);

    List<Faculty> getFacultyRaw(List<String> usernames);
}
