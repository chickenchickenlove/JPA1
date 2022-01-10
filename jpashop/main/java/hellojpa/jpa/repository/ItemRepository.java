package hellojpa.jpa.repository;

import hellojpa.jpa.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;


    /**
     * 신규 상품 등록
     * 상품 업데이트
     */

    public Long save(Item item) {

        if (item.getId() == null) {
            em.persist(item); //
        } else {
            em.merge(item);
        }
        return item.getId();
    }

    public Item findOne(Long id) {
        return  em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Item updateItem(Item item, String name, int price, int stockQuantity) {
        return item.changItemValue(name, price, stockQuantity);
    }



}
