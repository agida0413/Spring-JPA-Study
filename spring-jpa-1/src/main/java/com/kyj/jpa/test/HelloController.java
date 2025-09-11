package com.kyj.jpa.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final EntityManager em;

    @Transactional
    @GetMapping("/test")
    public String test() {



//        List<Member> selectMFromMemberM = em.createQuery("SELECT M FROM Member M", Member.class)
//                .getResultList();
//        
//        for(Member m:selectMFromMemberM){
//            System.out.println("m.getUsername() = " + m.getUsername());
//        }

        List<Orders> selectOFromOrdersO = em.createQuery("SELECT o from Orders o JOIN FETCH o.member", Orders.class)
                .getResultList();
            for(Orders or : selectOFromOrdersO){
                System.out.println("\"or\" = " + "이떄쿼리ㅏ??");
                System.out.println("or.getMember().getUsername() = " + or.getMember().getUsername());
                System.out.println("\"or\" = " + "이떄쿼리ㅏ??");
            }

        return "hello";
    }
}
