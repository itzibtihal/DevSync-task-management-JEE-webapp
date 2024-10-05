package org.youcode.DevSync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.mindrot.jbcrypt.BCrypt;
import org.youcode.DevSync.domain.enums.Role;
import org.youcode.DevSync.domain.entities.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime created_at;
    private LocalDateTime deleted_at;

    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    private Integer availableTokens;
    private Integer deleteTokens;

    public User() {
        this.id = UUID.randomUUID();
        this.created_at = LocalDateTime.now();
        this.deleted_at = null;
        this.availableTokens = 2;
        this.deleteTokens = 1;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void assignTask(Task task) {
        tasks.add(task);
        task.setAssignedUser(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setAssignedUser(null);
    }

    public boolean useTokenForModification() {
        if (availableTokens > 0) {
            availableTokens--;
            return true;
        }
        return false;
    }

    public boolean useDeleteToken() {
        if (deleteTokens > 0) {
            deleteTokens--;
            return true;
        }
        return false;
    }

    public void resetDailyTokens() {
        this.availableTokens = 2;
    }

    public void resetMonthlyDeleteToken() {
        this.deleteTokens = 1;
    }
}
