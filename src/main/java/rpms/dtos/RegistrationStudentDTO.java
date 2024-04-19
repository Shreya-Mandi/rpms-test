package rpms.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import rpms.models.enums.Department;

@Data
public class RegistrationStudentDTO {
    @NotNull(message = "Username can't be empty")
    @Pattern(regexp = "^PES[12](UG|PG)[0-9][0-9](CS|EC|EE|ME|AI)[0-9][0-9][0-9]$", message = "Invalid Username")
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

    @Min(value = 1, message = "Invalid semester")
    @Max(value = 8, message = "Invalid semester")
    @NotNull(message = "Semester can't be empty")
    private Integer semester;

    @Pattern(regexp = "^[a-zA-Z]$", message = "Invalid section")
    private String section;
}
