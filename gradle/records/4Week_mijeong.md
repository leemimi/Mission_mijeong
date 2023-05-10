## Title: [4Week] 이미정

### 미션 요구사항 분석 & 체크리스트

---

- [ ] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현 
- [x] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
  - [x] 최신순, 날짜순 정렬
  - [x] 인기도순 정렬
  - [x] 성별 정렬(여자 우선, 최신순)
  - [ ] 호감사유순 정렬
---

## 접근방법

### 필수미션 + 선택미션 구현 설명
 - QueryDsl을 사용하여 구현하고자 함
 - LikeablePersonQueryDslRepository 와 LikeablePersonQueryDslRepositoryImp를 만들었다
```java
public interface LikeablePersonQueryDslRepository {
    List<LikeablePerson> findAllfromInstaMemberGender(Long instaMemberId, String gender,int attractiveTypeCode, Sort sort);

}
```

```java
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
```
- JPAQuery를 사용해서 select, from, where를 통해 성별을 가져온다
- 이후 뒤의 코드는 추가미션의 호감사유 필터링 구현을 .and()로 처리했다
- orderBy는 





### 배포 작업
