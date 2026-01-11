package com.customerSupport.customersupport.model;

import com.customerSupport.customersupport.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String referenceNumber;
    private String resolutionDetails;
    @Enumerated(value = EnumType.STRING) // to store enum as string in DB
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    @OneToOne
    @JoinColumn(name = "conversation_id") // foreign key column
    private Conversation conversation;

}
