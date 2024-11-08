package com.example.demo.core.member.domain;

import com.example.demo.config.persistence.AuditEntity;
import com.example.demo.infrastructure.persistence.member.MemberNickName1;
import com.example.demo.infrastructure.persistence.member.MemberNickName2;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends AuditEntity {
    @Id
    @Column(name = "member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(name = "member_id", nullable = false, updatable = false, unique = true)
    private String memberId;

    @Column(name = "member_name", nullable = false, updatable = false)
    private String memberName;

    @Column(name = "member_nickname_1", columnDefinition = "VARCHAR(64)")
    private MemberNickName1 memberNickName1;

    @Column(name = "member_nickname_2", columnDefinition = "VARCHAR(64)")
    private MemberNickName2 memberNickName2;

    public Member(
        String memberId,
        String memberName
    ) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberNickName1 = new MemberNickName1(memberName);
        this.memberNickName2 = new MemberNickName2(memberName);
    }
}
