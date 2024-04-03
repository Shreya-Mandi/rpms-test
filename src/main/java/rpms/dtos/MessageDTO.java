package rpms.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageDTO {
    private String name;
    private String content;
    private Timestamp timestamp;
}
