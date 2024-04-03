package rpms.dtos;

import lombok.Data;
import rpms.models.enums.Department;

@Data
public class FacultyDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Department department;
    private String domain;
}
