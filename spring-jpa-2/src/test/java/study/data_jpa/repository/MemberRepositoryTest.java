package study.data_jpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
//        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
//        Member member = new Member("nameaa");
//        Member saveMember = memberRepository.save(member);
//
//        Member findMember = memberRepository.findById(member.getId()).get();
//
//        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
////        assertThat(findMember.getUserName()).isEqualTo(saveMember.getUserName());

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        System.out.println("result.size() = " + result.size());
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }
}