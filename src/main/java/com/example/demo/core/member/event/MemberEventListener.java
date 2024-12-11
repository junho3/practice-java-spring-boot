package com.example.demo.core.member.event;

import com.example.demo.core.member.param.SendMemberEmailParam;
import com.example.demo.core.member.service.SendMemberEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final SendMemberEmailService sendMemberEmailService;

    @Async("domainEventAsyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createMember(final CreateMemberEvent event) {
        log.info("회원생성 이벤트 리스너 수신: {}", event);
        sendMemberEmailService.createMemberEmail(SendMemberEmailParam.from(event));
    }
}
