package com.petfabula.application.service;

import com.petfabula.domain.aggregate.community.service.ParticipatorService;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SynchronizeUser {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ParticipatorService participatorService;

    @Transactional
    public UserAccount registerByEmailCodeAndSync(String name, String email, String code) {
        UserAccount userAccount =
                registerService.registerByEmailCode(name, email, code);
        onCreate(userAccount.getId(), userAccount.getName(), userAccount.getPhoto());

        return userAccount;
    }

    @Transactional
    public void onCreate(Long id, String name, String photo){
        participatorService.createParticipator(id, name, photo);
    }

    @Transactional
    public void onUpdate(Long id, String name, String photo){

    }
}
