package hellojpa.jpa.repository;


import hellojpa.jpa.domain.Member;
import hellojpa.jpa.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    // Tx begin / start를 같이 해주는 것이다.

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;



    @Test
    void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kimaaaaaa");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberService.findOne(savedId));
    }



    @Test
    void 중복회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        Member memberA = new Member();
        memberA.setName("kim");

        //when
        memberService.join(member);

        //then
        IllegalStateException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(memberA));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }









}

