package rpms.models;

import jakarta.persistence.*;
import lombok.Data;
import rpms.models.enums.Department;

import java.util.List;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Character section;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private Account account;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_project",
            joinColumns = @JoinColumn(name = "student_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "project_id", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "project_id"})
    )
    private List<Project> projectList;
}
