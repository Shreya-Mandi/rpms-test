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
    static String getSessionAccount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                return authentication.getName();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("String getSessionAccount()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    boolean isAccountPresent(String username);

    boolean isAccountNotPresent(String username);

    boolean isStudent(String username);

    boolean isFaculty(String username);

    Account getAccount(String username);

    boolean saveAccount(RegistrationStudentDTO registrationStudentDTO);

    boolean saveAccount(RegistrationFacultyDTO registrationFacultyDTO);

    List<StudentDTO> getStudentsPending();

    List<FacultyDTO> getFacultyPending();

    boolean acceptAccount(String username);

    boolean rejectAccount(String username);
}
