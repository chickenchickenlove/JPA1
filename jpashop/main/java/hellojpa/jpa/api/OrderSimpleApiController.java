package hellojpa.jpa.api;


import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderStatus;
import hellojpa.jpa.repository.OrderRepository;
import hellojpa.jpa.repository.OrderSearch;
import hellojpa.jpa.repository.order.simplerquery.OrderSimpleQueryRepository;
import hellojpa.jpa.repository.order.simplerquery.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderSimpleApiController {


    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;


    // Order 엔티티를 직접 반환하는 API
    // N+1번 쿼리가 발생한다.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // member, delivery, orderItems
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getStatus();
        }
        return orders;
    }

    // order를 DTO로 반환하는 API //
    // N+1문제 있음
    @GetMapping("/api/v2/simple-orders")
    public ResultSimpleOrderDto ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        System.out.println(orders);

        ResultSimpleOrderDto resultSimpleOrderDto = new ResultSimpleOrderDto();
        List<SimpleOrderDto> collect = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        resultSimpleOrderDto.setOrders(collect);
        return resultSimpleOrderDto;
    }


    @GetMapping("/api/v3/simple-orders")
    public ResultSimpleOrderDto ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        System.out.println(orders);

        ResultSimpleOrderDto resultSimpleOrderDto = new ResultSimpleOrderDto();
        List<SimpleOrderDto> collect = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        resultSimpleOrderDto.setOrders(collect);
        return resultSimpleOrderDto;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }







    @Data
    static class SimpleOrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order o) {
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getDelivery().getAddress();
        }
    }


    @Data
    static class ResultSimpleOrderDto {
        private List<SimpleOrderDto> orders;
    }




}
