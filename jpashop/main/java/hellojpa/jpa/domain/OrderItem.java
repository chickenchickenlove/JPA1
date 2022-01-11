package hellojpa.jpa.domain;

import hellojpa.jpa.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "orderitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;



    //== 비즈니스 로직 ==//
    public void cancel() {
        // 가지고 있는 Item 객체를 취소해야할 것 같다.
        getItem().addStock(this.count);
    }

    public int getTotalPrice() {

        return orderPrice * count;
    }


    //== 연관관계 메서드 ==//
    /**
     * 필요 없다.
     * item : 단방향
     * order : order에서 설정
     */

    //== 생성 메서드 ==//
    // order와는 관계없으면 될 듯.
    // 구매 가격, 구매 갯수, Item 넘겨줘야함. 테이블 맵핑 필요함.

    public static OrderItem createOrderItem(Item item, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(price);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }


}
