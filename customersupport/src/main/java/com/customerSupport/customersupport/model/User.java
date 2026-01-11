package com.customerSupport.customersupport.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity

@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)// one user have multiple conversation
    private List<Conversation> conversation;
}
