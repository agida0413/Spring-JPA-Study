package com.kyj.jpa.service;

import com.kyj.jpa.domain.Delivery;
import com.kyj.jpa.domain.Member;
import com.kyj.jpa.domain.Order;
import com.kyj.jpa.domain.OrderItem;
import com.kyj.jpa.domain.item.Item;
import com.kyj.jpa.repository.ItemRepository;
import com.kyj.jpa.repository.MemberRepository;
import com.kyj.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order (Long memberId, Long itemId , int count){
        Member member = memberRepository.findOne(memberId);

        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        return order.getId();
    }


    @Transactional
    public void cancelOrder(Long orderId){

        Order one = orderRepository.findOne(orderId);
        one.cancel();
        orderRepository.findOne(orderId);

    }
//
//    public List<Order> findOrders(OrderSearch orderSearch){
//
//    }
}
