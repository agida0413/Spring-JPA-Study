package com.kyj.jpa.service;

import com.kyj.jpa.domain.Address;
import com.kyj.jpa.domain.Member;
import com.kyj.jpa.domain.Order;
import com.kyj.jpa.domain.OrderStatus;
import com.kyj.jpa.domain.item.Book;
import com.kyj.jpa.domain.item.Item;
import com.kyj.jpa.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123"));
        em.persist(member);

        Item book = new Book();
        book.setName("시골 jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        System.out.println("\"이때 쿼리\" = " + "이때 쿼리");
        em.persist(book);
        System.out.println("\"이때 쿼리\" = " + "이때 쿼리");
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        Order findOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, findOrder.getStatus(), "상품 주문시 상태는 ORDER");
//        Assertions.assertEquals(OrderStatus.ORDER, findOrder.getStatus(), "상품 주문시 상태는 ORDER");
    }

    @Test
    public void 주문취소() throws Exception{

    }

    @Test()
    public void 상품주문_재고수량초과() throws Exception{

    }
}