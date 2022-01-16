package hellojpa.jpa.api;


import hellojpa.jpa.domain.Member;
import hellojpa.jpa.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {


    private final MemberService memberService;

    // 회원가입
    // 이름만 준다.
    // JSON으로 응답해야한다.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody Member member) {
        Long saveId = memberService.join(member);
        return new CreateMemberResponse(saveId);
    }

    // 엔티티를 그대로 받으면, 나중에 엔티티가 변경되었을 때 여러군데서 사용될 수도 있다.
    // 따라서 엔티티를 직접 받으면 변화에 아주 취약해진다.
    // 따라서 DTO 형태로 받아서 저장을 해준다.

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long saveId = memberService.join(member);
        return new CreateMemberResponse(saveId);
    }


    // 회원 수정
    @PutMapping("/api/v2/members/{memberId}")
    public UpdateMemberResponse updateMemberV2(@RequestBody UpdateMemberRequest request,
                                               @PathVariable(name = "memberId") Long id) {

        memberService.updateMember(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // 회원 조회 API 엔티티 직접 노출
    // 엔티티를 API로 직접 넘기니 스펙 변경에 민감하다.
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }


    //이름만 넘기는 API를 만들어보자
    // 엔티티를 DTO로 바꿔서 넘겨준다. 이 때, LIST가 가장 바깥에 나가도록 넘기면 안된다.
    @GetMapping("/api/v2/members")
    public List<MemberDto> membersV2() {
        List<Member> members = memberService.findMembers();

        return members.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/members")
    public Result membersV3() {
        List<Member> members = memberService.findMembers();

        List<MemberDto> collect = members.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());



        return new Result(collect);
    }


    @Data
    static class Result {
        private List<MemberDto> members;

        public Result(List<MemberDto> members) {
            this.members = members;
        }
    }


    @Data
    static class MemberDto {
        private String name;

        public MemberDto(String name) {
            this.name = name;
        }
    }





    @Getter
    static class UpdateMemberRequest {
        private String name;

        public UpdateMemberRequest(String name) {
            this.name = name;
        }
    }


    @Getter
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }







    @Getter
    static class CreateMemberRequest{
        private String name;
    }


    @Data
    @NoArgsConstructor
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }



}
