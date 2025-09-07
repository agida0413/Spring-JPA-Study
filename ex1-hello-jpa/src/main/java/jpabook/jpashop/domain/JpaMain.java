package jpabook.jpashop.domain;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

        Order order =new Order();
//        order.addOrderItem(new OrderItem());

            OrderItem orderItem = new OrderItem();
           orderItem.setOrder(order);
           em.persist(orderItem);

            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }


        emf.close();


    }

}
