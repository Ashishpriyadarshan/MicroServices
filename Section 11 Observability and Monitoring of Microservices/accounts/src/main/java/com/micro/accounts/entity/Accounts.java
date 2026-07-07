package com.micro.accounts.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor@ToString
public class Accounts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // internal primary key

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber;  // business account number (random)

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "branch_address")
    private String branchAddress;
}