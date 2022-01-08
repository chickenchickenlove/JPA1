package hellojpa.jpa.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Orders")
public class Order {


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // 객체 맵핑
    @JoinColumn(name = "member_id") // 테이블 맵핑
    private Member member;

    @OneToMany(mappedBy = "order") // 객체 연관관계 설정 + 객체 FK 제거
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderData;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;






}
