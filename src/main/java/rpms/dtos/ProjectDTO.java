package rpms.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import rpms.models.enums.Status;

import java.util.Date;

@Data
public class ProjectDTO {
    private Integer id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Start Date cannot be empty")
    private Date startDate;

    private Date endDate;

    @NotNull(message = "Status cannot be empty")
    private Status status;

    @AssertTrue(message = "End Date should be after Start Date")
    public boolean isValidRange() {
        if (endDate == null)
            return true;
        return endDate.compareTo(startDate) >= 0;
    }
}
