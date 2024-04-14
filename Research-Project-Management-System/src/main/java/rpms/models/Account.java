package rpms.models;

import jakarta.persistence.*;
import lombok.Data;
import rpms.models.enums.AccountType;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(nullable = false)
    private Boolean isApproved;

    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private List<Message> messageList;
}
