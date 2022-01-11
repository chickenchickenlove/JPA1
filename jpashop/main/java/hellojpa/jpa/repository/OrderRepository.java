package hellojpa.jpa.repository;


import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderStatus;
import hellojpa.jpa.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

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
    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m"; // o + m
        boolean isFirstCondition = true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }


        if (orderSearch.getMemberName()!= null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class);


        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }


        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }


        return query.getResultList();
    }


}
