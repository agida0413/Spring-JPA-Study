package com.kyj.jpa.service;

import com.kyj.jpa.domain.Member;
import com.kyj.jpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {
        @Autowired
        MemberService memberService;
        @Autowired
        MemberRepository memberRepository;


    @Test
    public void 회원가입() throws Exception{
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);
        assertEquals(member,memberRepository.findOne(savedId));


    }

    @Test
    public void 중복_회원_예외() throws Exception{
        Member member = new Member();
        member.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        memberService.join(member);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }


        fail("예외 발생");
    }
}