package org.youcode.DevSync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.enums.TokenType;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Request() {
    }


    public Request(User user, Task task, TokenType tokenType) {
        this.user = user;
        this.task = task;
        this.tokenType = tokenType; // Store as TokenType
        this.timestamp = LocalDateTime.now();
        this.status = RequestStatus.IN_PROGRESS; // Default status
    }
}
