package hellojpa.jpa.repository;


import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {


    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원_저장조회_성공")
    @Rollback(false)
    void test1() throws Exception{
        //given
        Member member = new Member();
        member.setName("test");
        member.setAddress(new Address("suwon","0tong","zzz"));

        memberRepository.saveMember(member);

        //when
        Member findMember = memberRepository.findMember(member.getId());

        //then
        assertThat(findMember.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("전체_회원_조회")
    void test2() throws Exception{
        //given
        Member memberA = new Member();
        memberA.setName("test");
        memberA.setAddress(new Address("suwon","0tong","zzz"));

        Member memberB = new Member();
        memberB.setName("test");
        memberB.setAddress(new Address("suwon","0tong","zzz"));


//        when
        memberRepository.saveMember(memberA);
        memberRepository.saveMember(memberB);

        //then
        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(memberA, memberB);
    }
}
