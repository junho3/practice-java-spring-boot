# 코드 리뷰 판단 규칙 (P1~P5)

`/junho3-code-review`가 diff에 적용하는 소프트 권고 체크리스트. **차단이 아니라 표시다.**
CI 하드 게이트(레이어·네이밍 ArchUnit, PMD)가 잡는 건 여기서 보지 않는다.
설계 배경은 `docs/QUALITY-GATES.md`.

## P1. 시간 주입 (testability)
서비스 로직이 현재 시각을 **직접 조회**하면 그 분기를 테스트에서 고정 못 한다.
- BAD:  `LocalDateTime at = LocalDateTime.now();  // 반환/저장되는 비즈니스 값`
- GOOD: `Clock`/파라미터/DTO로 주입 → `now(clock)`
- 검증 비교(`isAfter(LocalDate.now())`)·로깅·메타데이터는 **"확인 필요"로만 표시**, 주입 강요 금지.

## P2. 단일 사용 alias 인라인 (local reasoning)
필드를 지역변수에 담아 **한 번만** 쓰면 값 추적 비용만 는다.
- BAD:  `String name = dto.name(); foo(name);`
- GOOD: `foo(dto.name());`
- 이름이 의미를 부여하면(매직값·복잡식 명명) 유지.

## P3. 얕은 래퍼 / 불필요한 간접
정보 은닉 없이 한 줄 위임만 하는 패스스루.
- BAD:  `String getName(Dto d) { return d.name(); }`
- GOOD: 호출부에서 `d.name()` 직접

## P4. 네이밍 의미 정밀도
이름이 역할·단위를 담는가. (접미사 컨벤션은 ArchUnit이 강제.)
- BAD:  `List<Result> list`, `boolean flag`, `int cnt`
- GOOD: `List<Result> top10Sales`, `boolean isFutureDate`, `int retryCount`

## P5. 추상화 존재 정당성
"이 추상화가 애초에 필요한가?" 과설계(YAGNI)를 지적.
- 구현체 하나뿐인 인터페이스, 안 쓰는 확장점, 한 곳만 쓰는 제네릭.

## 범위
- 컴파일·ArchUnit·PMD로 잡히는 것 재보고 금지.
- P1~P5 밖 일반 리뷰·리스크 0 취향(공백 등) 금지.
- 전부 [ADVISORY] — 차단으로 승격하지 마라.
