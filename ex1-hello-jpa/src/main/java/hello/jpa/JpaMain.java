//package hello.jpa;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;
//import jpabook.jpashop.domain.Member;
//
//public class JpaMain {
//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
//
//        EntityManager em = emf.createEntityManager();
//
//        EntityTransaction tx = em.getTransaction();
//
//        tx.begin();
//
//        Member member = new Member();
//        member.setId(1L);
//        member.setName("hello");
//        em.persist(member);
//
//        tx.commit();
//
//        em.close();
//
//        emf.close();
//
//    }
//
//}
