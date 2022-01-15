package hellojpa.jpa.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hellojpa.jpa.domain.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;

    private int stockQuantity;

    @JsonIgnore
    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();


    // 비즈니스 로직은 모두 엔티티에 있도록 한다.
    // 객체 지향 관점에서 비즈니스 로직이 엔티티에 있으면 좀 더 응집력 있게 결합이 가능하다.
    // 서비스 계층에서 Setter로 장난질 하게 되면 변경 지점이 정확하지 않다. 따라서 비즈니스 로직을 엔티티 계층에서 만들어서 제공해주는 방법이 좋다.


    /**
     * stock 증가 메서드
     *
     */

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * Stcok 감소 메소드
     * 재고는 0보다 줄어들 수 없다.
     */

    public void removeStock(int quantity) {

        int resultQuantity = this.stockQuantity - quantity;
        if (resultQuantity < 0) {
            throw new IllegalStateException("need more stock");
        }

        this.stockQuantity = resultQuantity;
    }


    //== 변경 메서드==//

    public Item changItemValue(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        return this;

    }

}
