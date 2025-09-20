package study.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition) {
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

//    @Override
//    public List<MemberTeamDto> searchPageSimple(MemberSearchCondition memberSearchCondition, Pageable pageable) {
//        List<MemberTeamDto> fetch = queryFactory
//                .select(new QMemberTeamDto(
//                        member.id.as("memberId")
//                        , member.username
//                        , member.age
//                        , team.id.as("team_id")
//                        , team.name.as("teamName")
//                ))
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(
//                        usernameEq(memberSearchCondition.getUsername())
//                        , teamNameEq(memberSearchCondition.getTeamName())
//                        , ageGoe(memberSearchCondition.getAgeGoe())
//                        , ageLoe(memberSearchCondition.getAgeLoe())
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        List<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId")
                        , member.username
                        , member.age
                        , team.id.as("team_id")
                        , team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(memberSearchCondition.getUsername())
                        , teamNameEq(memberSearchCondition.getTeamName())
                        , ageGoe(memberSearchCondition.getAgeGoe())
                        , ageLoe(memberSearchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(memberSearchCondition.getUsername())
                        , teamNameEq(memberSearchCondition.getTeamName())
                        , ageGoe(memberSearchCondition.getAgeGoe())
                        , ageLoe(memberSearchCondition.getAgeLoe())
                );

        return PageableExecutionUtils.getPage(results,pageable, countQuery::fetchOne);

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
