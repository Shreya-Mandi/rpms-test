package rpms.mapper;

import rpms.dtos.FacultyDTO;
import rpms.dtos.RegistrationFacultyDTO;
import rpms.models.Faculty;

public class FacultyMapper {
    public static Faculty mapRegistrationToFaculty(RegistrationFacultyDTO registrationFacultyDTO) {
        Faculty faculty = new Faculty();
        faculty.setId(registrationFacultyDTO.getUsername());
        faculty.setDepartment(registrationFacultyDTO.getDepartment());
        faculty.setDomain(registrationFacultyDTO.getDomain());
        faculty.setAccount(null);
        faculty.setProjectList(null);
        return faculty;
    }

    public static FacultyDTO mapFacultyToFacultyDTO(Faculty faculty) {
        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setUsername(faculty.getId());
        facultyDTO.setFirstName(faculty.getAccount().getFirstName());
        facultyDTO.setLastName(faculty.getAccount().getLastName());
        facultyDTO.setDepartment(faculty.getDepartment());
        facultyDTO.setDomain(faculty.getDomain());
        return facultyDTO;
    }
}
