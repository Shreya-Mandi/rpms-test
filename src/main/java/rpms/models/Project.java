package rpms.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import rpms.models.enums.Status;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "projectList", fetch = FetchType.EAGER)
    private List<Student> studentList;

    @ManyToMany(mappedBy = "projectList", fetch = FetchType.EAGER)
    private List<Faculty> facultyList;

    @OneToMany(mappedBy = "project", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Message> messageList;
}
