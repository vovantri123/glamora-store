package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name="sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product")
    private Set<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product")
    private Set<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems;

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails;
}
