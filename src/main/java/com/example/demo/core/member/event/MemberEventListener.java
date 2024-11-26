package com.example.demo.core.member.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class MemberEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmail(final CreateMemberEvent event) {
        log.info("{} email 발송!!!", event.memberId());
    }
}
