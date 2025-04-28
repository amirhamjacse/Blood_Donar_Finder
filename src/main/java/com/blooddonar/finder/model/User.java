package com.blooddonar.finder.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    private enum BloodGroup {
        A_POS,
        A_NEG,
        B_POS,
        B_NEG,
        AB_POS,
        AB_NEG,
        O_POS,
        O_NEG
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(length = 255)
    private String phone;

    @Column(length = 255)
    private String city;

    @Column(length = 255)
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;
}
