package hellojpa.jpa.repository.order.query;

import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {


    private final EntityManager em;




    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> orders = findOrders();

        System.out.println("OrderQueryRepository.findOrderQueryDtos");

        orders.forEach(orderQueryDto ->
                {

                    List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId());
                    orderQueryDto.setOrderItems(orderItems);
                }
        );
        System.out.println("OrderQueryRepository.findOrderQueryDtos return return");
        return orders;
    }


    public Map<Long, List<OrderItemQueryDto>> findAllByDto_optimization() {

        List<OrderQueryDto> orders = findOrders(); // 엔티티가 없기 때문에 지연로딩이 안된다.
        // where로 orderid가 하나인 것을 보낸다.
        // 그럼 in절로 orderId를 한번에 보내면 되지 않을까?

        List<Long> orderIds = orders.stream().
                map(o -> o.getOrderId()).
                collect(toList());

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
        return orderItemMap;
    }

    public Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds){

            List<OrderItemQueryDto> orderItems = em.createQuery("select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                            " from OrderItem oi" +
                            " join oi.item i" +
                            " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                    .setParameter("orderIds", orderIds)
                    .getResultList();


            Map<Long, List<OrderItemQueryDto>> collect = orderItems.stream()
                    .collect(groupingBy(OrderItemQueryDto::getId));

            return collect;
        }







    private List<OrderItemQueryDto> findOrderItems(Long orderId) {

        // select 쿼리를 치는데.. orderItems의 id가 매개변수 값인 item을 다 가져온다.
        System.out.println("OrderQueryRepository.findOrderItems");
        return em.createQuery(
                "select new hellojpa.jpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }



    // DTO로 직접 조회
    private List<OrderQueryDto> findOrders() {
        System.out.println("OrderQueryRepository.findOrders");
        return em.createQuery(
                "select new hellojpa.jpa.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }



    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public List<OrderFlatDto> findAllByDto_flat() {

        return em.createQuery("select new hellojpa.jpa.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.delivery d" +
                        " join o.member m" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();



//        return result.stream().
//                collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
//                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
//                )).entrySet().stream().
//                map(e -> new OrderQueryDto(e.getKey().getOrderId(),
//                        e.getKey().getName(), e.getKey().getOrderDate(),
//                        e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
//                .collect(toList());






    }
}
