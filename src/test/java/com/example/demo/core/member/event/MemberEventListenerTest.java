package com.example.demo.core.member.event;

import com.example.demo.TestFixtures;
import com.example.demo.TestTransactionEventSupport;
import com.example.demo.annotation.TransactionalEventListenerTest;
import com.example.demo.core.member.param.SendMemberEmailParam;
import com.example.demo.core.member.service.SendMemberEmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@TransactionalEventListenerTest
@DisplayName("MemberEventListener")
class MemberEventListenerTest {
    @Autowired
    private TestTransactionEventSupport support;
    @InjectMocks
    private MemberEventListener memberEventListener;
    @MockitoBean
    private SendMemberEmailService sendMemberEmailService;

    @Test
    @DisplayName("createMember()는 회원가입 이벤트가 발생했을 때 이메일을 전송한다.")
    void test1() {
        final CreateMemberEvent event = TestFixtures.get()
            .giveMeOne(CreateMemberEvent.class);
        doNothing().when(sendMemberEmailService).createMemberEmail(any(SendMemberEmailParam.class));

        support.publish(event);

        verify(sendMemberEmailService, timeout(2000))
            .createMemberEmail(any(SendMemberEmailParam.class));
    }
}
