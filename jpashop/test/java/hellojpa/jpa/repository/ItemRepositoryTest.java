package hellojpa.jpa.repository;


import hellojpa.jpa.domain.item.Book;
import hellojpa.jpa.domain.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;


    /**
     * save
     * findone
     * findAll
     * updateItem
     */




    @Test
    void 하나만_조회_성공() throws Exception{
        //given
        Item item = new Book();

        item.setName("bookA");
        item.setPrice(1000);
        item.setStockQuantity(1000);

        //when
        Long saveId = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findOne(saveId);

        assertThat(findItem.getName()).isEqualTo(item.getName());
        assertThat(findItem.getPrice()).isEqualTo(item.getPrice());
        assertThat(findItem.getStockQuantity()).isEqualTo(item.getStockQuantity());
    }



    @Test
    void 전체_조회_성공() throws Exception{
        //given
        Item item = new Book();

        item.setName("bookA");
        item.setPrice(1000);
        item.setStockQuantity(1000);

        Item itemB = new Book();

        itemB.setName("bookA");
        itemB.setPrice(1000);
        itemB.setStockQuantity(1000);

        //when
        itemRepository.save(item);
        itemRepository.save(itemB);

        //then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(2);
    }


    @Test
    void 전체_조회_실패() throws Exception{
        //given
        Item item = new Book();

        item.setName("bookA");
        item.setPrice(1000);
        item.setStockQuantity(1000);

        Item itemB = new Book();

        itemB.setName("bookA");
        itemB.setPrice(1000);
        itemB.setStockQuantity(1000);

        //when
        itemRepository.save(item);
        itemRepository.save(itemB);

        //then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size() == 100).isEqualTo(false);
    }


    @Test
    void 업데이트_성공() throws Exception{

        //given
        Item item = new Book();

        item.setName("bookA");
        item.setPrice(1000);
        item.setStockQuantity(1000);

        //when
        itemRepository.updateItem(item, "bookC", 1234,10);

        //then

        assertThat(item.getName()).isEqualTo("bookC");
        assertThat(item.getPrice()).isEqualTo(1234);
        assertThat(item.getStockQuantity()).isEqualTo(10);

    }

    @Test
    void 업데이트_실패() throws Exception{
        //given
        Item item = new Book();

        item.setName("bookA");
        item.setPrice(1000);
        item.setStockQuantity(1000);

        //when
        itemRepository.updateItem(item, "bookC", 1234,10);

        //then

        assertThat(item.getName().equals("bookA")).isEqualTo(false);
    }








}
