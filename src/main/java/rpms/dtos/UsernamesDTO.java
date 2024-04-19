package rpms.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UsernamesDTO {
    private List<String> usernames;

    public UsernamesDTO(List<String> usernames) {
        this.usernames = usernames;
    }
}
