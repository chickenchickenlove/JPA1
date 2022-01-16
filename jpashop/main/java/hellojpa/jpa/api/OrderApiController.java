package hellojpa.jpa.api;

import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderItem;
import hellojpa.jpa.domain.OrderStatus;
import hellojpa.jpa.repository.OrderRepository;
import hellojpa.jpa.repository.OrderSearch;
import hellojpa.jpa.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    //== V1 : 엔티티 직접 노출 ==//
    // 컬렉션까지 직접 노출
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findOrders(new OrderSearch());

        orders.stream()
                .forEach(o ->
                        {
                            o.getMember().getName();
                            o.getDelivery().getStatus();
                            o.getOrderItems().stream()
                                    .forEach(oi -> oi.getItem().getName());
                        });
        return orders;
    }

    // 컬렉션까지 DTO로 반환하기
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {

        List<Order> orders = orderService.findOrders(new OrderSearch());

        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());
    }



    // DB 입장에서는 ORDER가 2배로 늘어났다. 따라서 ORDER TABLE 관점에서는 페이징이 안된다.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {

        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());

    }



    //페이징이 가능하도록 배치 사이즈를 설정한다
    @BatchSize(size = 100)
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "100") int limit
    ){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);


        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());

    }





    @Data
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderData;
        private OrderStatus orderStatus;
        private Address address;
//        private List<OrderItem> orderItems;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderData = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();

//            order.getOrderItems().forEach(orderItem -> orderItem.getItem().getName());
//            this.orderItems = order.getOrderItems();

            this.orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }


    @Data
    static  class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;


        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }




}
