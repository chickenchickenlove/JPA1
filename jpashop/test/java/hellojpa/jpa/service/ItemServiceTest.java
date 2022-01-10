package hellojpa.jpa.service;


import hellojpa.jpa.domain.item.Book;
import hellojpa.jpa.domain.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class ItemServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ItemService itemService;



    @Test
    void 아이템저장() throws Exception{
        //given
        Item book = new Book();
        book.setName("시골 JPA");

        //when
        Long savedId = itemService.saveItem(book);

        em.flush();
        em.clear();

        //then
        Item findItem = itemService.findOne(savedId);
        assertThat(findItem.getName()).isEqualTo(book.getName());

    }


    @Test
    void test() throws Exception{
        //given

        Item book = new Book();
        book.setName("bookA");
        book.setStockQuantity(100);
        book.setPrice(10000);

        Long savedItem = itemService.saveItem(book);

        //when
        em.flush();
        em.clear();

        itemService.updateItem(savedItem, "bookB", 10, 10);

        em.flush();
        em.clear();

        //then
        Item findItem = itemService.findOne(savedItem);

        assertThat(findItem.getName()).isEqualTo("bookB");
        assertThat(findItem.getPrice()).isEqualTo(10);
        assertThat(findItem.getStockQuantity()).isEqualTo(10);

    }





}
