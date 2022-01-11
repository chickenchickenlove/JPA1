package hellojpa.jpa.controller;


import hellojpa.jpa.domain.Member;
import hellojpa.jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;


    @GetMapping("/members/new")
    public String memberNew(Model model) {
        MemberForm form = new MemberForm();
        model.addAttribute("memberForm", form);

        return "members/createMemberForm";
    }


    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        // 성공 로직
        memberService.join(memberForm);

        //PRG 처리
        return "redirect:/";
    }



    @GetMapping("/members")
    public String memberList(Model model) {
        log.info("MEMBER LIST CONTROLLER START");

        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";


    }






}
