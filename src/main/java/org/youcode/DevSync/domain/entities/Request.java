package org.youcode.DevSync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Request() {
    }

    public Request(User user, String tokenType) {
        this.user = user;
        this.tokenType = tokenType;
        this.timestamp = LocalDateTime.now();
    }
}