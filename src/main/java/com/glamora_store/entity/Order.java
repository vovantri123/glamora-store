package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double shippingFee;

    private Double totalAmount;

    private LocalDateTime orderDate;

    private String status;

    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="voucher_id")
    private Voucher voucher;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;
}
