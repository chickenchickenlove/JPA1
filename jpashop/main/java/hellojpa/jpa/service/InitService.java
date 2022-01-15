package hellojpa.jpa.service;


import hellojpa.jpa.domain.*;
import hellojpa.jpa.domain.item.Book;
import hellojpa.jpa.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;

    public void dbinit1() {
        Member member = createMember("userA", "수원", "청구", "아파트");
        em.persist(member);

        Item book1 = createBook("시골 JPA1", 10000, 15000);
        em.persist(book1);

        Item book2 = createBook("시골 JPA2", 20000, 133330);
        em.persist(book2);


        OrderItem orderItem1 = createOrderItem(book1.getPrice(), book1.getStockQuantity(), book1);
        OrderItem orderItem2 = createOrderItem(book2.getPrice(), book2.getStockQuantity(), book2);
        Delivery delivery = createDelivery(member);

        Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
        em.persist(order);
    }

    public void dbinit2() {
        Member member = createMember("userB", "대구", "구청", "트아파");
        em.persist(member);

        Item book1 = createBook("SPRING1", 10110, 1340);
        em.persist(book1);

        Item book2 = createBook("SPRING2", 2200, 132350);
        em.persist(book2);


        OrderItem orderItem1 = createOrderItem(book1.getPrice(), book1.getStockQuantity(), book1);
        OrderItem orderItem2 = createOrderItem(book2.getPrice(), book2.getStockQuantity(), book2);
        Delivery delivery = createDelivery(member);

        Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
        em.persist(order);
    }



    private Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        return delivery;
    }



    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);

        return book;
    }



    private Member createMember(String name, String city, String street, String zipCode) {
        Member member = new Member();
        Address address = new Address(city, street, zipCode);
        member.setName(name);
        member.setAddress(address);

        return member;
    }


    private OrderItem createOrderItem(int price, int count, Item book) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(price);
        orderItem.setCount(count);
        orderItem.setItem(book);
        return orderItem;
    }

}

