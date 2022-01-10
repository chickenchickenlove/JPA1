package hellojpa.jpa.repository;


import hellojpa.jpa.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 회원 가입
    public Long saveMember(Member member) {

        em.persist(member);
        return member.getId();
    }

    // 회원 단 건 조회
    public Member findMember(Long id) {
        return em.find(Member.class, id);
    }

    // 전체 회원 조회
    // 엔티티 단계로 내려야할까?를 고민해보자.
    // tr단위는 아닌 거 같다.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }


    // 이름으로 멤버 찾기기
   public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
