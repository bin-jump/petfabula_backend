package com.petfabula.infrastructure.persistence.jpa.community;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class InitialzePostTopics implements ApplicationRunner {

    @Autowired
    private PostTopicRepository postTopicRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        PostTopicCategory postTopicCategory1 = new PostTopicCategory("Category1");
        postTopicCategory1.addTopic(new PostTopic("cate1-topic1", postTopicCategory1));
        postTopicCategory1.addTopic(new PostTopic("cate1-topic2", postTopicCategory1));

        PostTopicCategory postTopicCategory2 = new PostTopicCategory("Category2");
        postTopicCategory1.addTopic(new PostTopic("cate2-topic1", postTopicCategory2));
        postTopicCategory1.addTopic(new PostTopic("cate2-topic2", postTopicCategory2));
        postTopicCategory1.addTopic(new PostTopic("cate2-topic3", postTopicCategory2));

        addSingle(postTopicCategory1);
        addSingle(postTopicCategory1);
    }


    private void addSingle(PostTopicCategory postTopicCategory) {
        PostTopicCategory sameTitle = postTopicRepository
                .findByTitle(postTopicCategory.getTitle());
        if (sameTitle == null) {
            postTopicRepository.save(postTopicCategory);
        }
    }
}
