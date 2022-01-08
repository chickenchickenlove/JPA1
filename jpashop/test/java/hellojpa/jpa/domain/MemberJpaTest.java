package hellojpa.jpa.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class MemberJpaTest {

    @Autowired
    EntityManager em;


    @Test
    @Rollback(value = false)
    void test() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");



        em.persist(member);


        System.out.println("headr==========================================");

        Order order = new Order();
        order.setMember(member);

        em.persist(order);

        //when

        //then
    }


}
