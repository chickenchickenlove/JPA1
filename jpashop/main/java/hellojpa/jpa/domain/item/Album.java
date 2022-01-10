package hellojpa.jpa.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorColumn(name = "A")
public class Album extends Item {

    private String artist;
    private String etc;


}
