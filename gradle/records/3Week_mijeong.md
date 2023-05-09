## Title: [3Week] 이미정

### 미션 요구사항 분석 & 체크리스트

---

- [ ] 네이버 클라우드 플랫폼을 통한 배포
- [x] 호감표시/호감사유변경 후, 개별 호감표시건에 대해서, 3시간 동안은 호감취소와 호감사유변경을 할 수 없도록 작업
- [ ] 선택 미션 : 알림기능 구현
---

## 접근방법

### 호감표시 삭제 쿨타임 

- 현재 시간이 쿨타임 시간(3시간)을 지난 시간이어야 삭제 기능 로직이 수행되어야 한다.
- LikeablePersonService의 canCancel에 기능 추가
```java
    LocalDateTime modifyUnlockDate = likeablePerson.getModifyUnlockDate();
        if (LocalDateTime.now().isBefore(modifyUnlockDate)){
        return RsData.of("F-3","삭제 쿨타임이 지나야합니다");
        }
```
- LocalDateTime.now().isBefore(modifyUnlockDate) : 현재 시간이 쿨타임해제 시간보다 전이면 Fail

### 호감표시 변경 쿨타임 
-위의 삭제 쿨타임과 기능 동일하다


### 배포 작업
- 클라우드 작업 완료
- 현재 main의 TEST에서 막혀서 진행작업중이다.