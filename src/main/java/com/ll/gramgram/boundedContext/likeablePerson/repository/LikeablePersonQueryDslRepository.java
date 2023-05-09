package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LikeablePersonQueryDslRepository {
    List<LikeablePerson> findAllfromInstaMemberGender(Long instaMemberId, String gender,int attractiveTypeCode);

}
