package hellojpa.jpa.repository;


import hellojpa.jpa.api.OrderApiController;
import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderItem;
import hellojpa.jpa.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;



    // 주문 저장
    public Long saveOrder(Order order) {
        em.persist(order);
        return order.getId();
    }

    // 회원 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 회원 전체 조회
    public List<Order> findAll() {
        return em.createQuery("select O from Order O").getResultList();
    }

    // 회원 이름으로 검색 쿼리
    public List<Order> findAllByString(OrderSearch ordersearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (ordersearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and"; // 2개 이상 붙어야 할 경우...
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(ordersearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }


        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (ordersearch.getOrderStatus() != null) {
            query = query.setParameter("status", ordersearch.getOrderStatus());
        }

        if (StringUtils.hasText(ordersearch.getMemberName())) {
            query = query.setParameter("name", ordersearch.getMemberName());
        }

        return query.getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery", Order.class).getResultList();
    }



    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.orderItems oi" +
                        " join fetch o.delivery d" +
                        " join fetch oi.item i"
                , Order.class
        ).getResultList();


    }




    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery", Order.class).
                setFirstResult(offset).
                setMaxResults(limit).getResultList();

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
