package hellojpa.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // order가 만들어지면 orderItem이 만들어지기 때문에 영속성 전이한다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;



    //== 연관관계 메서드==//
    //연관관계의 주인이 아니더라도, Order를 기준으로 모든 것들이 이루어지기 떄문에 비즈니스 로직윽 Order를 기준으로 짜는게 좋다.

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void addOrderItems(OrderItem orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
    }

    //== 생성 메서드==//
    // 주문 생성
    // 오더를 생성하는 것이기 때문에..


    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        order.setDelivery(delivery);
        order.setMember(member);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItems(orderItem);
        }

        return order;

    }

    //== 비즈니스 로직 ==//
    // 주문 조회


    /**
     * 주문 취소 → 잔고가 1개 올라간다.
     */

    public void cancel() {

        if (this.getStatus().equals(OrderStatus.CANCEL)) {
            throw new IllegalStateException("이미 취소된 상품은 다시 한번 취소가 불가능합니다.");
        }

        if (this.delivery.getStatus().equals(DeliveryStatus.COMP)) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다. ");
        }
        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }

    /**
     * 전체 주문 가격 조회
     * orderItem들의 주문 가격을 다 가져와서, 곱하고 더하면 된다.
     */


    public int getTotalPrice() {
        return this.orderItems.stream().mapToInt(OrderItem::getTotalPrice)
                .sum();
    }





}
