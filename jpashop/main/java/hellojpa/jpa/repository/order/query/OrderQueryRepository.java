package hellojpa.jpa.repository.order.query;

import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from  Order o" +
                " join fetch o.member" +
                " join fetch o.delivery d", Order.class).getResultList();
    }


    public List<Order> findAllParameter() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d" +
                " join fetch o.orderItems oi" +
                " join fetch oi.item i", Order.class).getResultList();
    }

    public List<OrderQueryDto> findAllFlatDto() {
        List<OrderFlatDto> resultList = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class).getResultList();


        return resultList.stream().
                collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getItemName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getItemName(), o.getOrderPrice(), o.getCount(), o.getOrderId()), Collectors.toList())))
                .entrySet().stream().map(e ->
                        new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue())
                ).collect(toList());
    }

    public List<OrderQueryDto> findAllOrderQueryDto() {
        List<OrderQueryDto> resultList = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();

        resultList.forEach(orderQueryDto ->{

                    List<OrderItemQueryDto> orderItemQueryDto = findOrderItemQueryDto(orderQueryDto.getOrderId()); // 여기서 두 번 실행되는 것이 문제다
            orderQueryDto.setOrders(orderItemQueryDto);
                }


        );
        return resultList;
    }

    public List<OrderItemQueryDto> findOrderItemQueryDto(Long orderId) {

        return em.createQuery("select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(i.name, oi.orderPrice, oi.count, oi.order.id)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();




    }


    public List<OrderQueryDto> findAllOrderQueryDto_optimization() {


        List<OrderQueryDto> result = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class).getResultList();

        List<Long> orderIds = result.stream().map(orderQueryDto -> orderQueryDto.getOrderId()).collect(toList());

        List<OrderItemQueryDto> orders = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(i.name, oi.orderPrice, oi.count, oi.order.id)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();


        Map<Long, List<OrderItemQueryDto>> collect = orders.stream().collect(groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        result.stream().forEach(orderQueryDto ->
        {
            List<OrderItemQueryDto> orderItemQueryDtos = collect.get(orderQueryDto.getOrderId());
            orderQueryDto.setOrders(orderItemQueryDtos);
        });

        return result;







    }
}
