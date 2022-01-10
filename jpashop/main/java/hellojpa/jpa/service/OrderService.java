package hellojpa.jpa.service;

import hellojpa.jpa.domain.*;
import hellojpa.jpa.domain.item.Item;
import hellojpa.jpa.repository.ItemRepository;
import hellojpa.jpa.repository.MemberRepository;
import hellojpa.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    // 주문
    // 엔티티를 만들어서 넘겨주지마라. 무조건 서비스 객체에서 만든다.


    // 어떤 멤버가 어떤 item에 대해서 몇개씩 주문했는지.
    public Long Order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberService.findOne(memberId);
        Item item = itemService.findOne(itemId);

        // 배송정보 가져오기
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrder(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        return orderRepository.save(order);
    }


    // 주문 취소
    public void cancel(Long id) {
        Order order = orderRepository.findOne(id);
        order.cancel();
    }

    // 주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findByName()
//
//    }



}
