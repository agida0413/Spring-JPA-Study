package study.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id){
        Member member = memberRepository.findById(id).get();

            return member.getUsername();

    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){

        return member.getUsername();

    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
      return   memberRepository.findAll(pageable);
    }


    @GetMapping("/memberstest")
    public Page<Member> list(int age, Pageable pageable){
        return   memberRepository.findByAgeGreaterThan(age,pageable);
    }

    @PostConstruct
    public void inint(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
