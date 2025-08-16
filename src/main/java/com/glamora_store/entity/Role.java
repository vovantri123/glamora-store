package com.glamora_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Audited
@Table(name = "role")
public class Role extends BaseEntity {
  @Id
  private String name;
  private String description;

  @ManyToMany
  @JoinTable(
    name = "role_permissions",
    joinColumns = @JoinColumn(name = "role_name"),
    inverseJoinColumns = @JoinColumn(name = "permission_name")
  )
  private Set<Permission> permissions;
}
