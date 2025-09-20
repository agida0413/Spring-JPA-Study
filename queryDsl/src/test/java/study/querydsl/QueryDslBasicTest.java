package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.entity.Member;


import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;


import java.util.List;

import static com.querydsl.core.types.Projections.*;
import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;


@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;
    @BeforeEach
    public void before(){
         queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);

        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }


    @Test
    public void startQueryDsl(){
//        QMember m = new QMember("m");
        Member member = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        assertThat(member.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search(){
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(member1.getUsername()).isEqualTo("member1");

    }
    @Test
    public void resultFetch(){
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();



    }

    @Test
    public void desc(){
        em.persist(new Member(null ,100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6" ,100));
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = fetch.get(0);
        Member member6 = fetch.get(1);
        Member member7 = fetch.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(member7.getUsername()).isEqualTo(null);



    }

    @Test
    public void page(){
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(fetch.size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        List<Tuple> fetch = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = fetch.get(0);

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);

    }

    @Test
    public void group(){
//        Integer i = queryFactory
//                .select(
//                        member.age.sum()
//                )
//                .from(member)
//                .groupBy(member.team.name)
//                .fetchOne();
//
//        System.out.println("i = " + i);


    }

    @Test
    public void join(){
        List<Member> teamA = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(teamA)
                .extracting("username")
                .containsExactly("member1","member2");

    }

    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> fetch = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(fetch)
                .extracting("username")
                .containsExactly("teamA","teamB");
    }

    @Test
    public void join_on(){
        List<Tuple> teamA = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : teamA) {
            System.out.println("tuple = " + tuple);
        }

    }

    @Test
    public void join_on_norelation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> fetch = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }


    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        Member member1 = queryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(member1.getTeam());

        assertThat(loaded).as("페치조인미적용").isTrue();

    }

    @Test
    public void subQuery(){

        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .containsExactly(40);
    }

    @Test
    public void subQuery2(){

        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .containsExactly(30,40);
    }


    @Test
    public void subQuery3(){

        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(member.age.gt(10))
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .containsExactly(20,30,40);
    }
    
    
    @Test
    public void selectSubQuery(){
        QMember memberSub = new QMember("memberSub");
        List<Tuple> fetch = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();
    }
    
    @Test
    public void basicCase(){
        List<String> fetch = queryFactory
                .select(
                        member.age
                                .when(10).then("열살")
                                .when(20).then("스무살")
                                .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void complex(){
        queryFactory
                .select(
                        new CaseBuilder()
                                .when(member.age.between(0,20)).then("0~20살")
                                .when(member.age.between(21,30)).then("21~30살")
                                .otherwise("기타")
                )
                .from(member)
                .fetch();
    }
    
    @Test
    public void constant(){
        List<Tuple> fetch = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }
    }
    
    @Test
    public void conacat(){
        List<String> fetch = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void simpleProjection(){
        List<String> fetch = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void tuple(){
        List<Tuple> fetch = queryFactory.select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            String s = tuple.get(member.username);
            Integer i = tuple.get(member.age);
            
        }
    }
    
    @Test
    public void findByJpql(){
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username,m.age) from Member m"
                        , MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
        }

    }

    @Test
    public void findDtoFindbysetter(){
        List<MemberDto> fetch = queryFactory
                .select(bean(MemberDto.class
                        , member.username
                        , member.age
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : fetch) {
            System.out.println("memberDto = " + memberDto.getUsername());
        }
    }


    @Test
    public void findDtoFindbyfiled(){
        List<MemberDto> fetch = queryFactory
                .select(fields(MemberDto.class
                        , member.username
                        , member.age
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : fetch) {
            System.out.println("memberDto = " + memberDto.getUsername());
        }
    }

    @Test
    public void findDtoFindbyConstructor(){
        List<MemberDto> fetch = queryFactory
                .select(constructor(MemberDto.class
                        , member.username
                        , member.age
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : fetch) {
            System.out.println("memberDto = " + memberDto.getUsername());
        }
    }
    
    @Test
    public void findDtoQueryProject(){
        List<MemberDto> fetch = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : fetch) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQuery_booleanBuilder(){
        String usernameParam = "member1";
        Integer ageParam = 10;

       List<Member> result=  searchMember1(usernameParam,ageParam);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {

        BooleanBuilder builder = new BooleanBuilder();
        if(usernameParam != null){
            builder.and(member.username.eq(usernameParam));
        }

        if(ageParam!=null){
            builder.and(member.age.eq(ageParam));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }


    @Test
    public void dynamicQueryWhere(){
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result=  searchMember2(usernameParam,ageParam);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameParam),ageEq(ageParam))
                .fetch();
    }

    private Predicate ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam):null;
    }

    private Predicate usernameEq(String usernameParam) {
        if(usernameParam ==null){
            return null;
        }
        return member.username.eq(usernameParam);
    }
    
    @Test
    public void bulk(){
        
        Member member1 = new Member("test1",100);
        em.persist(member1);
        em.flush();
        em.clear();

        System.out.println("member1.getId() = " + member1.getId());
        
        Member findMember = em.find(Member.class,member1.getId());
        findMember.setUsername("dddddd");
        
        long count = queryFactory
                .update(member)
                .set(member.username, "test1000")
                .execute();
        em.flush();
        em.clear();
        
        Member change = em.find(Member.class,member1.getId());

        System.out.println("change.getUsername() = " + change.getUsername());
        
    }

    @Test
    public void bulk2(){
        queryFactory
                .update(member)
                .set(member.age,member.age.add(1))
                .execute();
    }
    
    @Test
    public void bulkdel(){
        QMember memberSub = new QMember("memberSub");
        long execute = queryFactory
                .delete(member)
                .where(member.id.in(
                        select(member.id)
                                .from(memberSub)
                ))
                .execute();
    }
    
    @Test
    public void sqlFUn(){
        List<String> fetch = queryFactory
                .select(Expressions.stringTemplate
                        ("function('replace',{0},{1},{2})", member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sql(){
        queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(
                      member.username.lower()
                ))
                .fetch();
    }
}
