package com.example.SpringBatchTutorial.core.domain.accounts;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@Entity
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_item")
    private String orderItem;

    private Integer price;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "account_date")
    private Date accountDate;
}
