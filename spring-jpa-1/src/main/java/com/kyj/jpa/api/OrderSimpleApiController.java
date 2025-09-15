package com.kyj.jpa.api;

import com.kyj.jpa.domain.Address;
import com.kyj.jpa.domain.Order;
import com.kyj.jpa.domain.OrderStatus;
import com.kyj.jpa.repository.OrderRepository;
import com.kyj.jpa.repository.OrderSearch;
import com.kyj.jpa.repository.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> collect = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }


    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> all = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> collect = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4(){
        List<SimpleOrderQueryDto> all = orderRepository.findOrderDtos();

        return all;
    }

    @Data
    static class SimpleOrderDto {
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
    }


}
