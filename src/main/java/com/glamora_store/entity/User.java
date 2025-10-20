package com.glamora_store.entity;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "full_name", nullable = false)
  @NotBlank(message = "FULL_NAME_REQUIRED")
  private String fullName;

  @Column(name = "gender", length = 10)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "dob")
  @DobConstraint(min = 6, message = "DOB_INVALID")
  private LocalDate dob;

  @Column(name = "email", unique = true, nullable = false)
  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  @Column(name = "password", nullable = false)
  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String password;

  @Column(name = "avatar", columnDefinition = "TEXT")
  private String avatar;

  // Soft delete field
  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_name"))
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Address> addresses = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;
}
