package com.beat.matrimonial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = true, length = 100)
    private String name;

    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = true, length = 10)
    private String gender;

    @Column(name = "religion", nullable = true, length = 50)
    private String religion;

    @Column(name = "occupation", nullable = true, length = 100)
    private String occupation;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}

