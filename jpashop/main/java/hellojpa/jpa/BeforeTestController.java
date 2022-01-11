package hellojpa.jpa;

import hellojpa.jpa.controller.MemberForm;
import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Member;
import hellojpa.jpa.domain.item.Book;
import hellojpa.jpa.service.ItemService;
import hellojpa.jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class BeforeTestController {


    private final MemberService memberService;
    private final ItemService itemService;

    @PostConstruct
    public void init() {

        MemberForm form = new MemberForm();
        form.setCity("수원");
        form.setName("김철수");
        form.setStreet("영통");
        form.setZipcode("123");
        memberService.join(form);


        Book book = new Book();
        book.setName("시골JPA");
        book.setAuthor("시골JPA");
        book.setPrice(100000);
        book.setIsbn("ABCD");
        book.setStockQuantity(20000);

        itemService.saveItem(book);

    }
}
