package com.glamora_store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.envers.Audited;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
public class Permission {
    @Id
    private String name;

    private String description;
}
