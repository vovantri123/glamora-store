package com.glamora_store.entity;

import com.glamora_store.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Audited
@Table(name = "users")
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "full_name")
  @NotBlank(message = "FULL_NAME_REQUIRED")
  @Size(min = 1, max = 100, message = "FULL_NAME_INVALID")
  private String fullName;

  @Column(name = "gender", length = 10)
  @Enumerated(EnumType.STRING) // It save name to DB, not displayName
  private Gender gender;

  @Column(name = "dob")
  @PastOrPresent(message = "DOB_INVALID")
  private LocalDate dob;

  @Column(name = "email", unique = true, nullable = false)
  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  @Column(name = "password", nullable = false)
  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String password; // Default in flyway is 12345678

  @Column(name = "phone_number", length = 15)
  @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
  private String phoneNumber;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "image", columnDefinition = "TEXT")
  private String image;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_name")
  )
  private Set<Role> roles;


  //  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  //  private Set<Order> orders;
  //
  //  @OneToMany(mappedBy = "user")
  //  private Set<CartItem> cartItems;
}
