package com.kyj.jpa.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders extends BaseEntity{

    private String orderContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    public void setOrderItems(OrderItem orderItem){
          this.getOrderItems().add(orderItem);
          orderItem.setOrders(this);

    }
}
