package rpms.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpms.dtos.FacultyDTO;
import rpms.mapper.FacultyMapper;
import rpms.models.Faculty;
import rpms.models.Project;
import rpms.respositories.FacultyRepository;
import rpms.services.FacultyService;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImplementation implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImplementation(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public boolean saveFaculty(Faculty faculty) {
        try {
            facultyRepository.save(faculty);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("FacultyServiceImplementation.class");
            System.out.println("boolean saveFaculty(Faculty)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteFaculty(String username) {
        try {
            facultyRepository.deleteById(username);
            return true;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("FacultyServiceImplementation.class");
            System.out.println("boolean deleteFaculty(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<FacultyDTO> getFaculty(List<String> usernames) {
        try {
            List<Faculty> facultyList = facultyRepository.findAllById(usernames);
            return facultyList.stream().map(FacultyMapper::mapFacultyToFacultyDTO).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("FacultyServiceImplementation.class");
            System.out.println("List<FacultyDTO> getFaculty(List<String>)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Project> getProjects(String username) {
        try {
            Optional<Faculty> facultyOptional = facultyRepository.findById(username);
            return facultyOptional.map(Faculty::getProjectList).orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("FacultyServiceImplementation.class");
            System.out.println("List<Project> getProjects(String)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Faculty> getFacultyRaw(List<String> usernames) {
        try {
            return facultyRepository.findAllById(usernames);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("FacultyServiceImplementation.class");
            System.out.println("List<Faculty> getFacultyRaw(List<String>)");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
