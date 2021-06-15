package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.PostCreated;
import com.petfabula.domain.aggregate.community.post.entity.*;
import com.petfabula.domain.aggregate.community.post.PostMessageKeys;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.aggregate.community.post.repository.*;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    @Autowired
    private PostTopicRepository postTopicRepository;

    @Autowired
    private PostTopicRelationRepository postTopicRelationRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Post create(Long participatorId, String content, Long relatePetId, Long topicId, List<ImageFile> images) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST);
        }

        ParticipatorPet participatorPet = null;
        if (relatePetId != null) {
            participatorPet = participatorPetRepository
                    .findByParticipatorId(participatorId).stream()
                    .filter(p -> p.getId().equals(relatePetId))
                    .findAny().orElse(null);
            if (participatorPet == null) {
                throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST);
            }
        }

        Long postId = idGenerator.nextId();
        Post post = new Post(postId, participator, content, participatorPet);
//        for (ImageFile image : images) {
//            String p = imageRepository.save(image);
//            PostImage postImage = new PostImage(p);
//            post.addImage(postImage);
//        }

        participator.setPostCount(participator.getPostCount() + 1);
        participatorRepository.save(participator);

        if (topicId != null) {
            PostTopic postTopic = postTopicRepository.findById(topicId);
            if (postTopic == null) {
                throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST);
            }
            PostTopicRelation postTopicRelation = new PostTopicRelation(postTopic, post);
            postTopicRelationRepository.save(postTopicRelation);
        }

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            PostImage postImage =
                    new PostImage(idGenerator.nextId(), path, post, dimension.getWidth(), dimension.getHeight());
            post.addImage(postImage);

        }
        Post savedPost = postRepository.save(post);

        domainEventPublisher.publish(new PostCreated(savedPost));
        return savedPost;
    }

    public Post update(Long postId, String content, Long relatePetId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException(PostMessageKeys.POST_NOT_FOUND);
        }

        if (relatePetId != null) {
            ParticipatorPet participatorPet = participatorPetRepository
                    .findByParticipatorId(post.getParticipator().getId()).stream()
                    .filter(p -> p.getId().equals(relatePetId))
                    .findAny().orElse(null);
            if (participatorPet == null) {
                throw new InvalidOperationException(PostMessageKeys.CANNOT_UPDATE_POST);
            }
        }
        post.setContent(content);
        post.setRelatePetId(relatePetId);

        return postRepository.save(post);
    }

    public Post remove(Long participatorId, Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException(PostMessageKeys.POST_NOT_FOUND);
        }

        Participator participator = post.getParticipator();
        if (!participator.getId().equals(participatorId)) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_REMOVE_POST);
        }

        postRepository.remove(post);
        participator.setPostCount(participator.getPostCount() - 1);
        participatorRepository.save(participator);
        return post;
    }
}
