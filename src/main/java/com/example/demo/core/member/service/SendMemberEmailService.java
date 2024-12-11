package com.example.demo.core.member.service;

import com.example.demo.core.member.param.SendMemberEmailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendMemberEmailService {

    public void createMemberEmail(final SendMemberEmailParam param) {
        log.info("{} email 발송!!!", param.memberId());
    }
}
