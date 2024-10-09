package org.youcode.DevSync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.youcode.DevSync.domain.enums.Role;
import org.youcode.DevSync.domain.enums.StatusTask;
import org.youcode.DevSync.domain.exceptions.TokenLimitExceededException;
import org.youcode.DevSync.domain.exceptions.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data

public class Task {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private  LocalDateTime startDate;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private StatusTask status;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    private boolean tokenUsed;

    public Task() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.status = StatusTask.NOT_STARTED;
        this.tokenUsed = false;
        this.deletedAt = null;
    }

    public Task(String title, String description, LocalDateTime dueDate, LocalDateTime startDate,User createdBy,User assignedUser) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.createdBy = createdBy;
        this.assignedUser = assignedUser;
    }

    public void assignNewUser(User currentUser, User newUser) throws TokenLimitExceededException, PermissionDeniedException {
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.MANAGER) {
            throw new PermissionDeniedException("Only a manager or admin can assign a task to another user.");
        }

        if (tokenUsed) {
            throw new TokenLimitExceededException("Task modification token already used.");
        }

        this.assignedUser = newUser;
        this.tokenUsed = true;
    }

    public void updateStatus() {
        this.status = StatusTask.determineStatus(dueDate, this.status == StatusTask.COMPLETED);
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
