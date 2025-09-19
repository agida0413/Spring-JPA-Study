package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired
    EntityManager em;
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

    @Test
    public void test(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> aaa = memberRepository.findUser("AAA", 10);
        assertThat(aaa.get(0)).isEqualTo(m1);
    }


    @Test
    public void test2(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> username = memberRepository.findUsername();

            for (String s : username) {
                System.out.println("s = " + s);
            }
        }

    @Test
    public void test3(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);
        
        List<String> names = List.of("AAA","BBB","CCC0");

        List<Member> byNames = memberRepository.findByNames(names);

        for (Member byName : byNames) {
            System.out.println("byName.getUsername() = " + byName.getUsername());
        }
    }



    @Test
    public void test4(){
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }


    @Test
    public void test5(){

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAAasdasdasd");
        System.out.println("aaa.size() = " + aaa.size());
        if(aaa == null){
            System.out.println("aaa is null = " + aaa);
        }

        if(aaa.isEmpty()){
            System.out.println("aaa is empty= " + aaa);
        }
//        Member aaa1 = memberRepository.findMemberByUsername("AAA");
//
//        System.out.println("aaa1.getUsername() = " + aaa1.getUsername());
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");

    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");


        Page<Member> byAge = memberRepository.findByAge(age, pageRequest);

        List<Member> content = byAge.getContent();
        for (Member member : content) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }
//        long totalElements = byAge.getTotalElements();
//        System.out.println("totalElements = " + totalElements);
//        System.out.println(byAge.getTotalPages());
        assertThat(content.size()).isEqualTo(3);
//        assertThat(byAge.getTotalElements()).isEqualTo(5);
        assertThat(byAge.getNumber()).isEqualTo(0);
//        assertThat(byAge.getTotalPages()).isEqualTo(2);
        assertThat(byAge.isFirst()).isTrue();
        assertThat(byAge.hasNext()).isTrue();

        Page<MemberDto> map = byAge.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));


        int resultCount = memberRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);


    }

    @Test
    public void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1",10,teamA));
        memberRepository.save(new Member("member2",19,teamB));

        em.flush();
        em.clear();

        List<Member> all = memberRepository.findByUsername("member1");
        for (Member member : all) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();

    }



    @Test
    public void test33(){
        memberRepository.findMemberCustom();
    }


    @Test
    public void JpaEvnet() throws InterruptedException {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);


        Thread.sleep(100);
         em.flush();
         em.clear();
        Member member = memberRepository.findById(member1.getId()).get();

        System.out.println(member.getLastModifiedDate());
        System.out.println(member.getCreatedDate());
        System.out.println(member.getCreatedBy());
        System.out.println(member.getLastModifiedBy());
    }
}