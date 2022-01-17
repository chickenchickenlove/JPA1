package hellojpa.jpa.api;

import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderStatus;
import hellojpa.jpa.repository.OrderRepository;
import hellojpa.jpa.repository.OrderSearch;
import hellojpa.jpa.repository.order.query.OrderQueryDto;
import hellojpa.jpa.repository.order.query.OrderQueryRepository;
import hellojpa.jpa.repository.order.simplequery.OrderSimpleQueryRepository;
import hellojpa.jpa.repository.order.simplequery.SimpleQueryDto;
import hellojpa.jpa.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    private final OrderQueryRepository orderQueryRepository;


    // 주문 toOne 관계만 Entity로 돌려주기
    // 너무 많은 정보가 포함되어 나간다.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        orders.forEach(order ->
        {
            order.getMember().getName();
            order.getDelivery().getStatus();
        });
        return orders;
    }


    // Dto로 바꿔서 돌려주기
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());

        return orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }


    // N+1 문제 해결
    // 너무 많은 정보를 가져온다.
    // 엔티티로 조회했더니, 엔티티를 가져와서 여기서 뭔가 더 해줄 수 있다.
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }


    // dto로 조회한다.
    // repository는 엔티티와 관련된 곳인데, dto를 조회한다는 건..
    // 논리적으로 이제 분리가 필요하다.

//    @GetMapping("/api/v4/simple-orders")
//    public List<OrderQueryDto> ordersV4() {
//        return orderQueryRepository.findOrderQueryDtos();
//    }
////




    @Data
    static class SimpleOrderDto {

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










}
