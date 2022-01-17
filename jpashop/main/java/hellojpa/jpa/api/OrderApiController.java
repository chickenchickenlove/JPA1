package hellojpa.jpa.api;

import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderItem;
import hellojpa.jpa.domain.OrderStatus;
import hellojpa.jpa.repository.OrderRepository;
import hellojpa.jpa.repository.OrderSearch;
import hellojpa.jpa.repository.order.query.OrderQueryDto;
import hellojpa.jpa.repository.order.query.OrderQueryRepository;
import hellojpa.jpa.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    //== V1 : 엔티티 직접 노출==//
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        orders.forEach(o ->
                {
                    o.getMember().getName();
                    o.getDelivery().getAddress();
                    o.getOrderItems().stream().forEach(orderItem -> orderItem.getItem().getName());
                }
        );

        return orders;
    }

    //== V2 : OrderDto로 노출==//


    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> result = orderRepository.findAllByString(new OrderSearch());

//        return result.stream().
//                map(order -> {
//
//                            List<OrderItemDto> collect = order.getOrderItems().stream().map(
//                                            orderItem -> new OrderItemDto(orderItem.getItem().getName(), orderItem.getOrderPrice(), orderItem.getCount()))
//                                    .collect(Collectors.toList());
//
//                            return new OrderDto(order.getId(), order.getMember().getName(), order.getOrderDate(), order.getStatus(),
//                                    order.getDelivery().getAddress(), collect);
//
//                        }
//                ).collect(Collectors.toList());
        return result.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    //== V3 : N+1 문제 해결 ==//
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> result = orderQueryRepository.findAllFetchJoin();
        return result.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    //== V3.1 : 페이징 문제 해결 ==//
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page() {

        List<Order> result = orderQueryRepository.findAllWithMemberDelivery();
        return result.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    //== V4 : DTO로 직접 조회 ==//
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    //== V5 : DTO로 직접 조회 + N+1 문제 해결 ==//
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderQueryDtos_optimization();



    }


    //== V6 : FLAT DTO로 받아와서 DTO로 반환 ==//


    //== V1 : 엔티티 직접 노출 ==//
    // 컬렉션까지 직접 노출


    @Data
    static class OrderItemDto{

        private String name;
        private int orderPrice;
        private int count;

        public OrderItemDto(String name, int orderPrice, int count) {
            this.name = name;
            this.orderPrice = orderPrice;
            this.count = count;
        }

        public OrderItemDto(OrderItem oi) {
            this.name = oi.getItem().getName();
            this.orderPrice = oi.getOrderPrice();
            this.count = oi.getCount();
        }


    }



    @Data
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderTime;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orders;


        public OrderDto(Long orderId, String name, LocalDateTime orderTime, OrderStatus orderStatus, Address address, List<OrderItemDto> orders) {
            this.orderId = orderId;
            this.name = name;
            this.orderTime = orderTime;
            this.orderStatus = orderStatus;
            this.address = address;
            this.orders = orders;
        }

        public OrderDto(Order o) {
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            this.orderTime = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getDelivery().getAddress();

            this.orders = o.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());


        }
    }








}
