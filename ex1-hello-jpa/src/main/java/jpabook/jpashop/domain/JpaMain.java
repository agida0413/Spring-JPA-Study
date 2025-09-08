package jpabook.jpashop.domain;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.jpashop.domain.entity.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
//            Member member = new Member();
//            member.setEmail("agida0413@naver.com");
//            member.setUserId("agida0413");
//
//            //글쓰기
//            ImageBoard imageBoard = new ImageBoard();
//            imageBoard.setImage("imageurl");
//            imageBoard.setContent("content");
//            imageBoard.setMember(member);
//
//            //디테일
//            BoardDetail boardDetail = new BoardDetail();
//            boardDetail.setDetailContent("detailcontent");
//            boardDetail.setBoard(imageBoard);
//
//            //댓글
//
//            Reply reply = new Reply();
//            reply.setReply("댓글입니다.");
//            reply.setBoard(imageBoard);
//
//            LIkeC like = new LIkeC();
//            like.setBoard(imageBoard);
//            like.setMember(member);
//
//            em.persist(member);
//            em.persist(imageBoard);
//            em.persist(boardDetail);
//            em.persist(reply);
//            em.persist(like);

//            BoardDetail boardDetail = em.find(BoardDetail.class, 1l);
//            boardDetail.setDetailContent("수정디테일");
//
//            FreeBoard freeBoard = new FreeBoard();
//            freeBoard.setFreeContent("free");
//            freeBoard.setContent("content2");
//            freeBoard.setMember(boardDetail.getBoard().getMember());
//            freeBoard.setTitle("title");
//
//            em.persist(freeBoard);
//
//            BoardDetail boardDetail2 = new BoardDetail();
//            boardDetail2.setBoard(freeBoard);
//            boardDetail2.setDetailContent("detial");
//            em.persist(boardDetail2);
//
//            em.persist(boardDetail2);


            Member member = new Member();
            member.setUserId("kim");
            member.setEmail("yon@naver");

            Board board = em.find(Board.class, 1l);

            LIkeC lIkeC = new LIkeC();
            lIkeC.setMember(member);
            lIkeC.setBoard(board);
            em.persist(member);
            em.persist(lIkeC);


            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }


        emf.close();


    }

}
