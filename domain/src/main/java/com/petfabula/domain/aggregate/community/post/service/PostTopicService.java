package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.event.TopicCategoryRemovedEvent;
import com.petfabula.domain.aggregate.community.post.event.TopicRemovedEvent;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicCategoryRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTopicService {

    @Autowired
    private PostTopicRepository topicRepository;

    @Autowired
    private PostTopicCategoryRepository postTopicCategoryRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public PostTopic createTopic(Long topicCategoryId, String title) {
        PostTopicCategory category = postTopicCategoryRepository.findById(topicCategoryId);
        if (category == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        PostTopic topic = topicRepository.findByTitle(title);
        if (topic != null) {
            throw new InvalidOperationException(CommonMessageKeys.NAME_ALREADY_EXISTS);
        }

        topic = new PostTopic(idGenerator.nextId(), title,
                category.getId(), category.getTitle());

        return topicRepository.save(topic);
    }

    public PostTopicCategory createCategory(String title) {
        PostTopicCategory category = postTopicCategoryRepository.findByTitle(title);
        if (category != null) {
            throw new InvalidOperationException(CommonMessageKeys.NAME_ALREADY_EXISTS);
        }

        category = new PostTopicCategory(idGenerator.nextId(), title);
        return postTopicCategoryRepository.save(category);
    }

    public PostTopic updateTopic(Long topicId, String title) {
        PostTopic topic = topicRepository.findById(topicId);
        if (topic == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        topic.setTitle(title);
        return topicRepository.save(topic);
    }

    public PostTopicCategory updateTopicCategory(Long topicCategoryId, String title) {
        PostTopicCategory category = postTopicCategoryRepository.findById(topicCategoryId);
        if (category == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        category.setTitle(title);
        topicRepository.updateCategoryTitle(topicCategoryId, title);
        return postTopicCategoryRepository.save(category);
    }

    public PostTopic removeTopic(Long topicId) {
        PostTopic topic = topicRepository.findById(topicId);
        if (topic == null) {
            return null;
        }

        topicRepository.remove(topic);

        domainEventPublisher.publish(new TopicRemovedEvent(topic));
        return topic;
    }

    public PostTopicCategory removeTopicCategory(Long topicCategoryId) {
        PostTopicCategory category = postTopicCategoryRepository.findById(topicCategoryId);
        if (category == null) {
            return null;
        }

        postTopicCategoryRepository.remove(category);
        topicRepository.removeByCategoryId(topicCategoryId);

        domainEventPublisher.publish(new TopicCategoryRemovedEvent(category));
        return category;
    }
}
