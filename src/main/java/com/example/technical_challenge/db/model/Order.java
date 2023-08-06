package com.example.technical_challenge.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_who_created_order_id")
    private User userWhoCreatedOrder;

    private Boolean isFulfilled;
}
