package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.Order;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeablePersonQueryDslRepositoryImp implements LikeablePersonQueryDslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LikeablePerson> findAllfromInstaMemberGender (Long instamemberId, String gender, int attractiveTypeCode, Sort sort) {
        JPAQuery<LikeablePerson> contentQuery = jpaQueryFactory
                .select(QLikeablePerson.likeablePerson)
                .from(QLikeablePerson.likeablePerson)
                .where(QLikeablePerson.likeablePerson.toInstaMember.id.eq(instamemberId).and(
                        eqGender(gender)
                ).and(eqAttractiveTypeCode(attractiveTypeCode))
                ).orderBy(getOrderSpecifiers(sort));

        List<LikeablePerson> likeablePersonList = contentQuery.fetch();
        return likeablePersonList;
    }
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {

        OrderSpecifier[] orderSpecifiers = sort.stream()
                .map(this::createOrderSpecifier)
                .toArray(OrderSpecifier[]::new);
        return orderSpecifiers;
    }


        private OrderSpecifier<?> createOrderSpecifier(Sort.Order o) {
            Order order = o.getDirection().isDescending() ? Order.DESC : Order.ASC;
            Expression<?> expression =
            switch (o.getProperty()) {
                case "date" -> QLikeablePerson.likeablePerson.fromInstaMember.createDate;
                case "popularity" -> QLikeablePerson.likeablePerson.fromInstaMember.fromLikeablePeople.size();
                case "gender" -> QLikeablePerson.likeablePerson.fromInstaMember.gender;
                case "attractionReason" -> QLikeablePerson.likeablePerson.attractiveTypeCode;
                default-> QLikeablePerson.likeablePerson.fromInstaMember.createDate;

            };
            return new OrderSpecifier(order, expression);
    }

    private static BooleanExpression eqAttractiveTypeCode (int attractiveTypeCode) {
        if(attractiveTypeCode == 1 || attractiveTypeCode == 2 || attractiveTypeCode == 3 ){
            return QLikeablePerson.likeablePerson.attractiveTypeCode.eq(attractiveTypeCode);
        }
        return null;
    }

        private static BooleanExpression eqGender (String gender) {
        if(gender != null && (gender.equals("M")||gender.equals("W"))){
            return QLikeablePerson.likeablePerson.fromInstaMember.gender.eq(gender);
        }
        return null;
    }



}
