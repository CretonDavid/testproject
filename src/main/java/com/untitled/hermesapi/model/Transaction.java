package com.untitled.hermesapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
@SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 10)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    private Long id;

    @Column(name = "amount")
    private float amount;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "debit")
    private boolean isDebit;

    @Column(name = "credit")
    private boolean isCredit;

    @Column(name = "description")
    private String description;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
