package hellojpa.jpa.repository;

import hellojpa.jpa.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;


    // 주문 저장
    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

    // 주문 단건 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }



    // 주문 검색
    // OrderSearch라는 이름의 객체를 넘겨줄꺼다.
    // 이름으로 검색,
    // 추후 개발
//    public List<Order> findAllByName(String name) {
//
//        String query = "select o from Order o";
//
//
//    }






}
