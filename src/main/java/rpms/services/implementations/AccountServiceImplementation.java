package rpms.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rpms.dtos.FacultyDTO;
import rpms.dtos.RegistrationFacultyDTO;
import rpms.dtos.RegistrationStudentDTO;
import rpms.dtos.StudentDTO;
import rpms.mapper.AccountMapper;
import rpms.mapper.FacultyMapper;
import rpms.mapper.StudentMapper;
import rpms.models.Account;
import rpms.models.Faculty;
import rpms.models.Student;
import rpms.models.enums.AccountType;
import rpms.respositories.AccountRepository;
import rpms.services.AccountService;
import rpms.services.FacultyService;
import rpms.services.StudentService;

import java.util.*;

@Service
public class AccountServiceImplementation implements AccountService {
    private final AccountRepository accountRepository;
    private final StudentService studentService;
    private final FacultyService facultyService;

    @Autowired
    public AccountServiceImplementation(AccountRepository accountRepository, StudentService studentService, FacultyService facultyService) {
        this.accountRepository = accountRepository;
        this.studentService = studentService;
        this.facultyService = facultyService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (!account.getIsApproved())
                throw new UsernameNotFoundException("Invalid username and password");

            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(account.getType().name()))
            );
        } else {
            throw new UsernameNotFoundException("Invalid username and password");
        }
    }

    @Override
    public String getSessionAccount() {
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

    @Override
    public boolean isAccountPresent(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.isPresent();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean isAccountPresent(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAccountNotPresent(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.isEmpty();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean isAccountNotPresent(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isStudent(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.filter(account -> account.getType() == AccountType.STUDENT).isPresent();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean isStudent(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isFaculty(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.filter(account -> account.getType() == AccountType.FACULTY).isPresent();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean isFaculty(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAdmin(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.filter(account -> account.getType() == AccountType.ADMIN).isPresent();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean isAdmin(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Account getAccountRaw(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            return accountOptional.orElse(null);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("Account getAccount(String)");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean saveAccount(RegistrationStudentDTO registrationStudentDTO) {
        try {
            if (isAccountPresent(registrationStudentDTO.getUsername()))
                return false;
            Student student = StudentMapper.mapRegistrationToStudent(registrationStudentDTO);
            Account account = AccountMapper.mapRegistrationToAccount(registrationStudentDTO);
            student.setAccount(account);
            return studentService.saveStudent(student);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean saveAccount(RegistrationStudentDTO)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveAccount(RegistrationFacultyDTO registrationFacultyDTO) {
        try {
            if (isAccountPresent(registrationFacultyDTO.getUsername()))
                return false;
            Faculty faculty = FacultyMapper.mapRegistrationToFaculty(registrationFacultyDTO);
            Account account = AccountMapper.mapRegistrationToAccount(registrationFacultyDTO);
            faculty.setAccount(account);
            return facultyService.saveFaculty(faculty);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean saveAccount(RegistrationFacultyDTO)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<StudentDTO> getStudentsPending() {
        try {
            List<Account> accountList = accountRepository.findAllByIsApprovedIsFalseAndTypeIs(AccountType.STUDENT);
            List<String> usernames = accountList.stream().map(Account::getUsername).toList();
            return studentService.getStudents(usernames);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("List<StudentDTO> getStudentsPending()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<FacultyDTO> getFacultyPending() {
        try {
            List<Account> accountList = accountRepository.findAllByIsApprovedIsFalseAndTypeIs(AccountType.FACULTY);
            List<String> usernames = accountList.stream().map(Account::getUsername).toList();
            return facultyService.getFaculty(usernames);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("List<FacultyDTO> getFacultyPending()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getStudentsAccepted() {
        try {
            List<Account> accountList = accountRepository.findAllByIsApprovedIsTrueAndTypeIs(AccountType.STUDENT);
            return accountList.stream().map(Account::getUsername).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("List<String> getStudentsAccepted()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getFacultyAccepted() {
        try {
            List<Account> accountList = accountRepository.findAllByIsApprovedIsTrueAndTypeIs(AccountType.FACULTY);
            return accountList.stream().map(Account::getUsername).toList();
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("List<String> getFacultyAccepted()");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean acceptAccount(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                account.setIsApproved(true);
                accountRepository.save(account);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean acceptAccount(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rejectAccount(String username) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(username);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                boolean deleted = false;
                switch (account.getType()) {
                    case STUDENT -> deleted = studentService.deleteStudent(account.getUsername());
                    case FACULTY -> deleted = facultyService.deleteFaculty(account.getUsername());
                }
                if (deleted) {
                    accountRepository.delete(account);
                    return true;
                } else
                    return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong!!");
            System.out.println("AccountServiceImplementation.class");
            System.out.println("boolean rejectAccount(String)");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
