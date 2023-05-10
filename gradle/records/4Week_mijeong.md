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
- orderBy는 정렬을 위한 코드이다


```java
        private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {

        OrderSpecifier[] orderSpecifiers = sort.stream()
        .map(this::createOrderSpecifier)
        .toArray(OrderSpecifier[]::new);
        return orderSpecifiers;
        }

```
- 정렬하기위한 코드
```java
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

```
- 선택미션을 수행하기 위해 switch문으로 구현했다
- 잘 안되는 부분이 마지막 호감사유순인데, 호감사유개수를 count하는 과정이 잘 되지 않았다

```java
      private static BooleanExpression eqAttractiveTypeCode (int attractiveTypeCode) {
      if(attractiveTypeCode == 1 || attractiveTypeCode == 2 || attractiveTypeCode == 3 ){
      return QLikeablePerson.likeablePerson.attractiveTypeCode.eq(attractiveTypeCode);
      }
      return null;
      }
```
- attractiveTypeCode를 구별하는 코드이다
```java
        private static BooleanExpression eqGender (String gender) {
        if(gender != null && (gender.equals("M")||gender.equals("W"))){
            return QLikeablePerson.likeablePerson.fromInstaMember.gender.eq(gender);
        }
        return null;
    }
```
- 여성, 남성 구별을 가능하게 하는 코드이다

###  LikeablePersonService
```java
    public List<LikeablePerson> findAll (Long id, String gender, int attractiveTypeCode, int sortCode) {
        Sort sort;
        switch (sortCode) {
            case 1:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            case 2:
                sort = Sort.by(Sort.Direction.ASC, "date");
                break;
            case 3:
                sort = Sort.by(Sort.Direction.DESC, "popularity");
                break;
            case 4:
                sort = Sort.by(Sort.Direction.ASC, "popularity");
                break;
            case 5:
                sort = Sort.by(Sort.Direction.DESC,"gender").and(Sort.by("W")).and(Sort.by("M"));
                break;
            case 6:
                sort = Sort.by(Sort.Direction.DESC, "attractionReason").and(Sort.by(String.valueOf(attractiveTypeCode)));
                break;
            default:
                sort = Sort.unsorted();
                break;
        }

        return likeablePersonQueryDslRepository.findAllfromInstaMemberGender(id,gender,attractiveTypeCode, sort);
    }
```
- case 1~5, default까지는 잘 구현이 된다
- case 6이 호감 사유 순 인데 이 부분 구현 동작이 잘 되지 않는다.


### 배포 작업
- 배포는 계속 실행 부분에서 막힌다
- java -jar -Dspring.profiles.active=prod build/libs/gramgram-0.0.1-SNAPSHOT.jar
- DB랑 연동이 잘 안된것 같다!