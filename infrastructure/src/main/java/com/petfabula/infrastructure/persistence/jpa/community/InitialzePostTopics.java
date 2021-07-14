package com.petfabula.infrastructure.persistence.jpa.community;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.domain.aggregate.community.post.service.PostIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitialzePostTopics implements ApplicationRunner {

    @Autowired
    private PostTopicRepository postTopicRepository;

    @Autowired
    private PostIdGenerator postIdGenerator;

    @Override
    public void run(ApplicationArguments args) {
        Long categoryId = postIdGenerator.nextId();
        PostTopicCategory postTopicCategory1 = new PostTopicCategory(categoryId, "Category1");
        postTopicCategory1.addTopic(new PostTopic(postIdGenerator.nextId(), "cate1-topic1", categoryId));
        postTopicCategory1.addTopic(new PostTopic(postIdGenerator.nextId(), "cate1-topic2", categoryId));

        categoryId = postIdGenerator.nextId();
        PostTopicCategory postTopicCategory2 = new PostTopicCategory(categoryId, "Category2");
        postTopicCategory2.addTopic(new PostTopic(postIdGenerator.nextId(), "cate2-topic1", categoryId));
        postTopicCategory2.addTopic(new PostTopic(postIdGenerator.nextId(), "cate2-topic2", categoryId));
        postTopicCategory2.addTopic(new PostTopic(postIdGenerator.nextId(), "cate2-topic3", categoryId));

        addSingle(postTopicCategory1);
        addSingle(postTopicCategory2);
    }


    private void addSingle(PostTopicCategory postTopicCategory) {
        PostTopicCategory sameTitle = postTopicRepository
                .findByTitle(postTopicCategory.getTitle());
        if (sameTitle == null) {
            postTopicRepository.save(postTopicCategory);
        }
    }
}
