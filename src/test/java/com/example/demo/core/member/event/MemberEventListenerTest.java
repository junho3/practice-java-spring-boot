package com.example.demo.core.member.event;

import com.example.demo.annotation.TestEnvironment;
import com.example.demo.core.member.param.SendMemberEmailParam;
import com.example.demo.core.member.service.SendMemberEmailService;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestEnvironment
@RecordApplicationEvents
@DisplayName("MemberEventListener")
@SpringBootTest(classes = {ApplicationEventPublisher.class, MemberEventListener.class})
class MemberEventListenerTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private MemberEventListener memberEventListener;
    @MockBean
    private SendMemberEmailService sendMemberEmailService;

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @Test
    @DisplayName("createMember()는 회원가입 이벤트가 발생했을 때 이메일을 전송한다.")
    void createMember_when_publish_CreateMemberEvent_then_send_email() {
        final CreateMemberEvent event = fixtureMonkey.giveMeOne(CreateMemberEvent.class);
        doNothing().when(sendMemberEmailService).createMemberEmail(any(SendMemberEmailParam.class));

        applicationEventPublisher.publishEvent(event);

        verify(sendMemberEmailService, times(1))
            .createMemberEmail(any(SendMemberEmailParam.class));
    }
}
