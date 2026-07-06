package com.manoj.spring.ai.app.entity;

import com.google.rpc.Help;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "helpdesk_tickets")
public class HelpDeskTicket {

    public HelpDeskTicket() {

    }

    public HelpDeskTicket(Long id, String username, String issue, String status, LocalDateTime createdAt, LocalDateTime eta) {
        this.id = id;
        this.username = username;
        this.issue = issue;
        this.status = status;
        this.createdAt = createdAt;
        this.eta = eta;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String issue;

    private String status; // e.g., OPEN, IN_PROGRESS, CLOSED

    private LocalDateTime createdAt;

    private LocalDateTime eta;
}
