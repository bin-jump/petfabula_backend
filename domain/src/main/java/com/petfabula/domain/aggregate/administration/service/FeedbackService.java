package com.petfabula.domain.aggregate.administration.service;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.repository.FeedbackRepository;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private FeedbackRepository feedBackRepository;

    @Autowired
    private AdminIdGenerator idGenerator;

    public Feedback create(Long reporterId, String content) {
        UserAccount account = userAccountRepository.findById(reporterId);
        if (account == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        Feedback feedback = new Feedback(id, reporterId, content);

        return feedBackRepository.save(feedback);
    }
}
