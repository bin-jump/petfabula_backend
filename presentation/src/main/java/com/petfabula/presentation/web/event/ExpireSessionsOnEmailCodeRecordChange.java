package com.petfabula.presentation.web.event;


import com.petfabula.application.event.EmailCodeRecordChangeEvent;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.presentation.web.authentication.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ExpireSessionsOnEmailCodeRecordChange {

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @EventListener
    public void handle(EmailCodeRecordChangeEvent event) {
        if (event.isActive()) {
            return;
        }

        UserAccount account = userAccountRepository.findById(event.getUserId());
        LoginUser user = LoginUser.newInstance(account);

        List<SessionInformation> sessionInformations = sessionRegistry
                .getAllSessions(user, false);

        for(SessionInformation sessionInformation : sessionInformations) {
            sessionInformation.expireNow();
        }
    }
}
