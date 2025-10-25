package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission  {

  @Id
  @Column(length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;
}
