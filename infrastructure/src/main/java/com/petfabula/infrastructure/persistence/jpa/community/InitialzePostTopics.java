package com.petfabula.infrastructure.persistence.jpa.community;

import com.petfabula.domain.aggregate.community.entity.PostTopic;
import com.petfabula.domain.aggregate.community.repository.PostTopicRepository;
import com.petfabula.domain.aggregate.community.service.PostIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitialzePostTopics implements ApplicationRunner {

    @Autowired
    private PostTopicRepository postTopicRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        addSingle("Topic1", "Topic1 intro");
        addSingle("Topic2", "Topic2 intro");
    }

    private void addSingle(String title, String intro) {
        PostTopic postTopic = postTopicRepository.findByTitle(title);
        if (postTopic == null) {
            postTopic = new PostTopic(title, intro);
            postTopicRepository.save(postTopic);
        }
    }
}
