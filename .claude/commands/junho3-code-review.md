---
description: junho3 판단 규칙(P1~P5)으로 변경분을 검토한다 (소프트 권고)
argument-hint: "[커밋 범위, 기본=HEAD]"
allowed-tools: Bash, Read, Grep, Glob
---
주입된 규칙을 diff에 적용해 리뷰한다. 규칙 본문·범위·판정 태그는 주입 파일을 따른다.

## 1. 검토 대상 diff
!`git -C . diff ${ARGUMENTS:-HEAD}`

## 2. 판단 규칙 (전문 — 빠짐없이 대조)
@.claude/rules/junho3-code-rules.md

## 3. (참고) PMD 리포트
!`./gradlew pmdMain -q 2>&1 | tail -25 || true`

## 출력
| file:line | 규칙(P1~P5) | 문제 | 제안 |

확신 없으면 만들지 마라. 0건이면 "판단 이슈 없음". 끝에 "이 리뷰는 권고이며 CI 하드 게이트를 대체하지 않는다" 명시.
