package hellojpa.jpa.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long id;
    private String name;
    private int orderPrice;
    private int count;


    public OrderItemQueryDto(Long id, String name, int orderPrice, int count) {
        this.id = id;
        this.name = name;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
