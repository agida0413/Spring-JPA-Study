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
            Order order = em.find(Order.class,1L);

            Long memberId = order.getMemberId();

            em.find(Member.class,memberId);

            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }


        emf.close();


    }

}
