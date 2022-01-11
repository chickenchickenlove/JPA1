package hellojpa.jpa.controller;


import hellojpa.jpa.domain.Member;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.item.Item;
import hellojpa.jpa.repository.OrderSearch;
import hellojpa.jpa.service.ItemService;
import hellojpa.jpa.service.MemberService;
import hellojpa.jpa.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    //== 의존관계 주입==/
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;


    //== 컨트롤러 ==//

    @GetMapping("/order")
    public String orderForm(Model model) {


        // 회원 필요
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        // 모델 전송
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }


    @PostMapping("/order")
    public String createOrder(@RequestParam(name = "memberId") Long memberId,
                              @RequestParam(name = "itemId") Long itemId,
                              @RequestParam(name = "count") int count) {

        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute(name = "orderSearch")
                                        OrderSearch orderSearch,
                            Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }



}
