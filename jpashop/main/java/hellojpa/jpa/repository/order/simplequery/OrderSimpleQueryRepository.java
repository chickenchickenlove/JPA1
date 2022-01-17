package hellojpa.jpa.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {


    private final EntityManager em;

    public List<SimpleQueryDto> findOrderDtos() {

        //Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address
        // fetch를 하려면 여기에 반드시 나타나야한다.
        return em.createQuery("select new hellojpa.jpa.repository.order.simplequery.SimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", SimpleQueryDto.class)
                .getResultList();
    }

}
