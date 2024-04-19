package rpms.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import rpms.models.enums.Department;

@Data
public class RegistrationFacultyDTO {
    @NotNull(message = "Username can't be empty")
    @Pattern(regexp = "^PES[12](CS|EC|EE|ME|AI)[0-9][0-9][0-9]$", message = "Invalid Username")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 5, message = "Password needs to have at least 5 characters")
    private String password;

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotNull(message = "Department can't be empty")
    private Department department;

    @NotBlank(message = "Domain can't be empty")
    private String domain;
}
