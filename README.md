# 자바 스프링 부트 연습용 레포

## 프로젝트 설명

- 연습용 애플리케이션으로 단순한 단일 모듈 레이어드 아키텍처로 시작
- 엔티티 복잡도가 낮으므로 JPA 엔티티를 도메인 엔티티로 사용
- 비즈니스 요구사항이 없으므로 인터페이스(추상화)는 선택사항

## 프로젝트 구조

```
practice-java-spring-boot
 ㄴ common: 공통
 ㄴ config: 설정
 ㄴ core: 애플리케이션 레이어
    ㄴ domain: 도메인 엔티티 (JPA 엔티티)
    ㄴ event: 애플리케이션 이벤트
    ㄴ param: in DTO
    ㄴ result: out DTO
    ㄴ service: 비즈니스 구현체
 ㄴ infrastructure: 인프라스트럭처 레이어
    ㄴ kafka: 카프카
    ㄴ persistence: 영속성(JPA, querydsl)
 ㄴ web : 프리젠테이션 레이어
```
