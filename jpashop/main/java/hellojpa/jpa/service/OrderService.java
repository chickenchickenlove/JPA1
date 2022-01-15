package hellojpa.jpa.service;


import hellojpa.jpa.domain.Delivery;
import hellojpa.jpa.domain.Member;
import hellojpa.jpa.domain.Order;
import hellojpa.jpa.domain.OrderItem;
import hellojpa.jpa.domain.item.Item;
import hellojpa.jpa.repository.OrderRepository;
import hellojpa.jpa.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {


    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    // 주문 생성 + 저장
    public Long order(Long memberId, Long itemId, int count) {


        Member member = memberService.findOne(memberId);
        Item item = itemService.findOne(itemId);


        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.saveOrder(order);

        return order.getId();
    }

    // 주문 cancel
    public void cancel(Long orderId) {
        orderRepository.findOne(orderId).cancel();
    }


    // 회원 조회
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }




}
