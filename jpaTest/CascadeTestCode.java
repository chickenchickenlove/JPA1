package hellojpa.jpa.domain;


import org.h2.util.ThreadDeadlockDetector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
public class CascadeTestCode {


    @Autowired
    EntityManager em;


    @Test
    @Rollback(value = false)
    void 연관관계의_주인() throws Exception{


        Parent parent = new Parent();
        Child child = new Child();
        Child child1 = new Child();

        child.addParent(parent);
        child1.addParent(parent);

        em.persist(parent);
        em.flush();
        em.clear();

        Parent findParent = em.find(Parent.class, parent.getId());
        List<Child> children = findParent.getChildren();

        for (Child child2 : children) {
            System.out.println("child2 = " + child2);
        }

        children.set(0, new Child()); // 변겅감지로 인해 insert 쿼리 발생.

        em.clear();

        Parent findParent11 = em.find(Parent.class, parent.getId());
        List<Child> children11 = findParent11.getChildren();

        for (Child child2 : children11) {
            System.out.println("child2 = " + child2);
        }



    }


    @Test
    @Rollback(value = false)
    void 연관관계_주인에만_저장() throws Exception{


        Parent parent = new Parent();
        Child child = new Child();
        parent.setName("parent");
        child.setName("child");

        child.setParent(parent);

        em.persist(parent);
        em.persist(child);

        em.flush();
        em.clear();


        Parent parent1 = em.find(Parent.class, parent.getId());
        parent1.getChildren().stream().forEach(child1 -> System.out.println("child1.getName() = " + child1.getName()));
        
        


    }



    @Test
    @Rollback(false)
    void 부모객체_영속성_전이_연관관계_없이_저장() throws Exception{

        Parent parent = new Parent();
        Child childA = new Child();
        Child childB = new Child();

        parent.setName("parent");
        childA.setName("childA");
        childB.setName("childB");

        parent.getChildren().add(childA);
        parent.getChildren().add(childB);

        em.persist(parent);
    }


    @Test
    @Rollback(false)
    void 부모객체_영속성_전이_저장() throws Exception{

        Parent parent = new Parent();

        Child childA = new Child();
        Child childB = new Child();

        childA.addParent(parent);
        childB.addParent(parent);

        em.persist(parent);
    }

    @Test
    @Rollback(false)
    void 부모객체_영속성_전이_삭제() throws Exception{

        Parent parent = new Parent();

        Child childA = new Child();
        Child childB = new Child();

        childA.addParent(parent);
        childB.addParent(parent);
        em.persist(parent);
        em.flush();
        em.clear();

        Parent findParent = em.find(Parent.class, parent.getId());
        em.remove(findParent);
    }


    @Test
    @Rollback(false)
    void 고아객체_영속성_전이_삭제() throws Exception{

        Parent parent = new Parent();

        Child childA = new Child();
        Child childB = new Child();

        childA.addParent(parent);
        childB.addParent(parent);
        em.persist(parent);
        em.flush();
        em.clear();

        System.out.println("=======================================");
        System.out.println("=======================================");
        System.out.println("=======================================");
        System.out.println("=======================================");
        System.out.println("=======================================");

        Parent findParent = em.find(Parent.class, parent.getId());
        findParent.getChildren().remove(0);
    }


    @Test
    @Rollback(false)
    void test() throws Exception{
        Parent parent = new Parent();

        Child childA = new Child();
        Child childB = new Child();

        childA.addParent(parent);
        childB.addParent(parent);

        em.persist(parent);
        em.flush();
        em.clear();


        // remove, detach는 이미 영속화 된 상태에서만 동작할 수 잇다.

        Parent findParent = em.find(Parent.class, parent.getId());


        em.remove(parent); // 삭제를 하기 위해선 JPA에 영속화 되어야 한다. 영속화 되었다는 것은 JPA가 PK를 가지고 있다는 뜻이다.




    }





}
