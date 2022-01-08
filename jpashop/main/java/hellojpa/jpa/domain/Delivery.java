package hellojpa.jpa.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id // pk 맵핑
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;




}
