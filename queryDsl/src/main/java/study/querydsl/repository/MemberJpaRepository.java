package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@Repository
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Member member){
        em.persist(member);
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findById_querydsl(Long id){
       return Optional.ofNullable( queryFactory
               .select(member)
               .from(member)
               .where(member.id.eq(id))
               .fetchOne());
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAll_querydsl(){
        return queryFactory
                .select(member)
                .from(member)
                .fetch();
    }


    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username = :username",Member.class)
                .setParameter("username",username)
                .getResultList();
    }

    public List<Member> findByUsername_querydsl(String username){
        return queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetch();
    }


    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition memberSearchCondition){
       BooleanBuilder builder = new BooleanBuilder();

       if(StringUtils.hasText(memberSearchCondition.getUsername())){
        builder.and(member.username.eq(memberSearchCondition.getUsername()));
       }

       if (StringUtils.hasText(memberSearchCondition.getTeamName())){
           builder.and(team.name.eq(memberSearchCondition.getTeamName()));
       }

       if(memberSearchCondition.getAgeGoe() != null){
           builder.and(member.age.goe(memberSearchCondition.getAgeGoe()));
       }

        if(memberSearchCondition.getAgeLoe() != null){
            builder.and(member.age.loe(memberSearchCondition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                          member.id.as("memberId")
                        , member.username
                        , member.age
                        , team.id.as("team_id")
                        , team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team,team)
                .where(builder)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition){
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId")
                        , member.username
                        , member.age
                        , team.id.as("team_id")
                        , team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team,team)
                .where(
                        usernameEq(memberSearchCondition.getUsername())
                       ,teamNameEq(memberSearchCondition.getTeamName())
                       ,ageGoe(memberSearchCondition.getAgeGoe())
                       ,ageLoe(memberSearchCondition.getAgeLoe())
                )
                .fetch();
    }

    public List<Member> searchMember(MemberSearchCondition memberSearchCondition){
        return queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team,team)
                .where(
                        usernameEq(memberSearchCondition.getUsername())
                        ,teamNameEq(memberSearchCondition.getTeamName())
                        ,ageGoe(memberSearchCondition.getAgeGoe())
                        ,ageLoe(memberSearchCondition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression ageBetween(int a , int b){

        return ageGoe(a).and(ageGoe(b));

    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }
}
