package com.example.demo.core.member.domain;

import com.example.demo.config.persistence.AuditEntity;
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

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_name")
    private String memberName;

    public Member(
        String memberId,
        String memberName
    ) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
