package com.ekart.orderservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;
}
