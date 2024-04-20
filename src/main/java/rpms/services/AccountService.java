package rpms.services;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import rpms.dtos.FacultyDTO;
import rpms.dtos.RegistrationFacultyDTO;
import rpms.dtos.RegistrationStudentDTO;
import rpms.dtos.StudentDTO;

import rpms.models.Account;

import java.util.List;

public interface AccountService extends UserDetailsService {
    String getSessionAccount();

    boolean isAccountPresent(String username);

    boolean isAccountNotPresent(String username);

    boolean isStudent(String username);

    boolean isFaculty(String username);

    boolean isAdmin(String username);

    Account getAccountRaw(String username);

    boolean saveAccount(RegistrationStudentDTO registrationStudentDTO);

    boolean saveAccount(RegistrationFacultyDTO registrationFacultyDTO);

    List<StudentDTO> getStudentsPending();

    List<FacultyDTO> getFacultyPending();

    List<String> getStudentsAccepted();

    List<String> getFacultyAccepted();

    boolean acceptAccount(String username);

    boolean rejectAccount(String username);
}
