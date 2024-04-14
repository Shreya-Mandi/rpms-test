package rpms.mapper;

import rpms.dtos.RegistrationFacultyDTO;
import rpms.dtos.RegistrationStudentDTO;
import rpms.models.Account;
import rpms.models.enums.AccountType;

public class AccountMapper {
    public static Account mapRegistrationToAccount(RegistrationStudentDTO registrationStudentDTO) {
        Account account = new Account();
        account.setUsername(registrationStudentDTO.getUsername());
        account.setPassword(registrationStudentDTO.getPassword());
        account.setFirstName(registrationStudentDTO.getFirstName());
        account.setLastName(registrationStudentDTO.getLastName());
        account.setType(AccountType.STUDENT);
        account.setIsApproved(false);
        account.setMessageList(null);
        return account;
    }

    public static Account mapRegistrationToAccount(RegistrationFacultyDTO registrationFacultyDTO) {
        Account account = new Account();
        account.setUsername(registrationFacultyDTO.getUsername());
        account.setPassword(registrationFacultyDTO.getPassword());
        account.setFirstName(registrationFacultyDTO.getFirstName());
        account.setLastName(registrationFacultyDTO.getLastName());
        account.setType(AccountType.FACULTY);
        account.setIsApproved(false);
        account.setMessageList(null);
        return account;
    }
}
