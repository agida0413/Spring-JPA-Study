package com.kyj.jpa.api;

import com.kyj.jpa.domain.Address;
import com.kyj.jpa.domain.Order;
import com.kyj.jpa.domain.OrderItem;
import com.kyj.jpa.domain.OrderStatus;
import com.kyj.jpa.repository.OrderRepository;
import com.kyj.jpa.repository.OrderSearch;
import com.kyj.jpa.repository.order.OrderQueryDto;
import com.kyj.jpa.repository.order.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for (Order order : allByString) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();

            orderItems.stream().forEach(o -> o.getItem().getName());

        }

        return allByString;
    }


    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderv2(){
        List<Order> orders= orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return   collect;
    }


    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findWithItem();
        for (Order order : orders) {
            System.out.println("order.getId() = " +order +" id= "+order.getId());

        }
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }


    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV31(@RequestParam(value = "offset", defaultValue = "0" ) int offset
            ,@RequestParam(value = "limit", defaultValue = "100" ) int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderv4(){
        return orderQueryRepository.findOrderQueryDtos();
    }
// 주문 수 + 맴버
    @Data
     static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice=orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
