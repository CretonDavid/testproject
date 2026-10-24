package com.untitled.hermesapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_type")
@SequenceGenerator(name = "account_type_seq", sequenceName = "account_type_seq", allocationSize = 10)
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_type_seq")
    private Long id;

    @Column(name = "name")
    private String name;
}
