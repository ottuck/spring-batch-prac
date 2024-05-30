package com.example.SpringBatchTutorial.core.domain.orders;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_item")
    private String orderItem;

    private Integer price;

    @Column(name = "order_date")
    private Date orderDate;

}
