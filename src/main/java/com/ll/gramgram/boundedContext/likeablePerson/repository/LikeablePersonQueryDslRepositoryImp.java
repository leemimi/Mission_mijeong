package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeablePersonQueryDslRepositoryImp implements LikeablePersonQueryDslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LikeablePerson> findAllfromInstaMemberGender (Long instamemberId, String gender) {
        JPAQuery<LikeablePerson> contentQuery = jpaQueryFactory
                .select(QLikeablePerson.likeablePerson)
                .from(QLikeablePerson.likeablePerson)
                .where(QLikeablePerson.likeablePerson.toInstaMember.id.eq(instamemberId).and(
                        eqGender(gender)
                ));

        List<LikeablePerson> likeablePersonList = contentQuery.fetch();
        return likeablePersonList;
    }

    private static BooleanExpression eqGender (String gender) {
        if(gender != null && (gender.equals("M")||gender.equals("W"))){
            return QLikeablePerson.likeablePerson.fromInstaMember.gender.eq(gender);
        }
        return null;
    }


}
