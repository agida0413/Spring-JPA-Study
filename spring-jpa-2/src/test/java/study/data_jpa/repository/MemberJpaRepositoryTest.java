package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;


    @Test
    public void testMember(){
        Member member = new Member("memberA");

        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());




        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
//        assertThat(findMember.getUserName()).isEqualTo(saveMember.getUserName());

    }

    @Test
    public void findByUsernameAng(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

            assertThat(result.get(0).getUsername()).isEqualTo("AAA");
            assertThat(result.get(0).getAge()).isEqualTo(20);
            assertThat(result.size()).isEqualTo(1);


    }
}