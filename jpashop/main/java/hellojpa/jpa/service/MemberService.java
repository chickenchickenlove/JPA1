package hellojpa.jpa.service;

import hellojpa.jpa.controller.MemberForm;
import hellojpa.jpa.domain.Address;
import hellojpa.jpa.domain.Member;
import hellojpa.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    /**
     * 핵심 로직은 모두 memberRepository에 있음.
     */


    /**
     * 회원 가입
     * 중복 회원이 있을 경우 예외 터뜨리며 돌아간다.
     */
    @Transactional
    public Long join(Member member) {
        // 문제가 발생하면 예외를 터뜨려서 위로 보낸다.
        validateDuplicatedMember(member);
        return memberRepository.saveMember(member);
    }

    /**
     * 전체 회원 조회
     */


    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long id) {
        return memberRepository.findMember(id);
    }

    /**
     * 중복 이름 회원 있을 시, IllegalStateException throw.
     * 멀티 쓰레드 고민이 필요하다. → 멀티 쓰레드 환경에서 동일한 이름이 동시에 가입할 경우 어떻게 처리할지에 대한 고민이 필요함.
     */

    private void validateDuplicatedMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    @Transactional
    public void updateMember(Long id, String name) {
        Member member = memberRepository.findMember(id);
        member.setName(name);

    }
}
