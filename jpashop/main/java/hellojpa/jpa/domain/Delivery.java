package hellojpa.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    private DeliveryStatus status;


    public Delivery() {
        this.status = DeliveryStatus.READY;
    }

    public void checkCancelStatus(){
        if (this.status.equals(DeliveryStatus.COMP)) {
            throw new IllegalStateException("배송이 완료된 상품입니다.");
        }
    }



}
