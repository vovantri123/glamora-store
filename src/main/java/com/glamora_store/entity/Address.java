package com.glamora_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "addresses")
public class Address extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "receiver_name", nullable = false)
  @NotBlank(message = "RECEIVER_NAME_REQUIRED")
  private String receiverName;

  @Column(name = "receiver_phone", nullable = false, length = 15)
  @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
  private String receiverPhone;

  @Column(name = "province", nullable = false)
  private String province;

  @Column(name = "district", nullable = false)
  private String district;

  @Column(name = "ward", nullable = false)
  private String ward;

  @Column(name = "street_detail", nullable = false)
  private String streetDetail;

  @Column(name = "latitude", columnDefinition = "DECIMAL(10,7)")
  private Double latitude;

  @Column(name = "longitude", columnDefinition = "DECIMAL(10,7)")
  private Double longitude;

  @Column(name = "is_default")
  private boolean isDefault;

  // Soft delete field
  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
