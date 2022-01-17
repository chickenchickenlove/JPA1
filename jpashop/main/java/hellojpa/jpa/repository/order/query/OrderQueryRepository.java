package hellojpa.jpa.repository.order.query;

import hellojpa.jpa.api.OrderApiController;
import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<Order> findAllFetchJoin() {

        return em.createQuery("select distinct o from Order o" +
                        " join fetch o.delivery d" +
                        " join fetch o.member m" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {

        return em.createQuery("select o " +
                        " from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> resultList = findOrderQuery();

        resultList.forEach(
                orderQueryDto -> orderQueryDto.setOrderItems(findOrderItems(orderQueryDto.getOrderId()))
        );
        return resultList;
    }

    private List<OrderQueryDto> findOrderQuery() {
        List<OrderQueryDto> resultList = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.delivery oi", OrderQueryDto.class).getResultList();
        return resultList;
    }


    public List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name , oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        List<OrderQueryDto> resultList = findOrderQuery();
        List<Long> orderIds = resultList.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
        resultList.forEach(

                // map의 key가 OrderId다
                // orderId에 맞는 value를 찾아서 넣어주면 된다.
                orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId()))
        );

        return resultList;


    }


    public Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
    }




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
    }

}
