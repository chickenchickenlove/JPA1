package hellojpa.jpa.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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


}
