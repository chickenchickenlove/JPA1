package hellojpa.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hellojpa.jpa.domain.OrderStatus.*;

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


    @BatchSize(size = 100)
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // order가 만들어지면 orderItem이 만들어지기 때문에 영속성 전이한다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    //== 비즈니스 메서드==//
    // 엔티티 내에서만 가질 수 있는 걸 해야한다.
    // 상품 조회 // 엔티티 내에서만 가질 수 잇는 걸 해야한다. 트랜잭션 없임.

    public int getTotalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice)
                .sum();
    }


    // 주문 취소 -> 상품 취소
    public void cancel() {

        // 배송 완료 체크 로직
        delivery.checkCancelStatus();

        // 배송 완료가 아니라면
        setStatus(CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 연관관계 편의 메서드==//
    /**
     * ORDER 중심으로 돌아간다.
     * ORDER에 연관관계 편의 메서드 작성한다.
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //== 생성 메서드==//
    public static Order createOrder(Member member,Delivery delivery, OrderItem... orderItems) {

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }










}
