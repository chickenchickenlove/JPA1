package hellojpa.jpa.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorColumn(name = "B")
public class Book extends Item{

    private String author;
    private String isbn;


}
