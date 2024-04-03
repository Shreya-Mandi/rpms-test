package rpms.dtos;

import lombok.Data;
import rpms.models.enums.Department;

@Data
public class StudentDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Department department;
    private Integer semester;
    private Character section;
}
