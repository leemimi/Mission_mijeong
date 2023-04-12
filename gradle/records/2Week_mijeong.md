## Title: [2Week] 이미정

### 미션 요구사항 분석 & 체크리스트

---

- [x] 케이스 4 : 한명의 인스타회원이 다른 인스타회원에게 중복으로 호감표시를 할 수 없습니다.
- [x] 케이스 5 : 한명의 인스타회원이 11명 이상의 호감상대를 등록 할 수 없습니다.
  - [ ] application.yml로 사이즈 제한하기
- [ ] 케이스 6 : 케이스 4 가 발생했을 때 기존의 사유와 다른 사유로 호감을 표시하는 경우에는 성공으로 처리한다.
- [ ] 추가 미션 : 네이버 연동
---

 ## 접근방법

### 케이스4

  - 호감표시를 "중복"으로 할 수 없기때문에 Service의 add 를 수정
  - if문으로 getInstaMember의 getFromLikeablePeple을 가져온 리스트에 내가 넣고싶은 username이 있으면 rq.historyback을 하도록 했다

### 케이스5
    
 - 10명이하의 사람을 등록하면 등록을 하지 않도록 구현함.

 ```java
    if(member.getInstaMember().getFromLikeablePeople().size()>=10){
        return RsData.of("F-1", "등록한 상대가 10명입니다! 호감상대를 삭제하고 다시시도하세요!");
    }
```
- size()로 10명을 제한 두었다.
### 케이스6

- 케이스4번에서 코드를 추가해서 구현해야하겠다고 생각함.
- 처음에는 likeableRepository의 @Query 를 통해 update를 해야한다고 생각하는 것이 맞다고 생각했다.
 ```java
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update LikeablePerson a set a.attractiveTypeCode=:attractiveTypeCode where a.id =:id")
    @Transactional
    int updateAttractiveTypeCode(@Param("id") Long id, @Param("attractiveTypeCode") int attractiveTypeCode);
```
- 이방법은 적용이 되지 않았다. 강사님께서도 이 방법을 사용하지말라하고 점프투스프링부트 방법을 사용하라고 권하셨다
- 점프투 스프링 부트 방법 적용
```java
    if(member.getInstaMember().getFromLikeablePeople().stream()
        .anyMatch(lp -> lp.getToInstaMember().getUsername().equals(username))){
        likeablePerson.setAttractiveTypeCode(attractiveTypeCode);
        likeablePerson.setModifyDate(LocalDateTime.now());

        return RsData.of("S-2", "호감사유를 변경합니다.");
        }

        likeablePersonRepository.save(likeablePerson);

```
- if문으로 해당되는 likeablePerson 을 set을 통해서 수정하면 된다고 생각했다. (likeablePerson에 @Setter추가함)
- 하지만 되지않았다. 밑에있는 save를 if문 안에 넣으면 DB에 중복이 안되고 추가 현상이 일어난다.

## 특이사항 & 궁금한점

### 케이스 6번의 궁금증과 구현방안 논리 고찰

- 안되는 이유를 곰곰히 생각해보았는데 likeablePerson의 해당하는 id를 리포지터리에서 찾고 그 id의 attrativeTypeCode를 수정한다. 이 논리로 가야하는 것 같다
- 여러 방법을 사용해봤는데 id값을 넣고 그 해당하는 호감상대의 attractiveTypeCode를 수정. 하는 방법을 도저히 모르겠다.
- 한끗차이로 모르는 느낌이 드는데 이건 내 어느 부분이 부족한걸까?

### 케이스5번의 application.yml로의 추가 구현
- 시간이 없어서 못했다.