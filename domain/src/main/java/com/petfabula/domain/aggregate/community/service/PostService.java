package com.petfabula.domain.aggregate.community.service;

import com.petfabula.domain.aggregate.community.entity.*;
import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.aggregate.community.repository.*;
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

    public Post create(Long participatorId, String content, Long relatePetId, Long topicId, List<ImageFile> images) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST);
        }
        if (relatePetId != null) {
            ParticipatorPet participatorPet = participatorPetRepository
                    .findByParticipatorId(participatorId).stream()
                    .filter(p -> p.getId().equals(relatePetId))
                    .findAny().orElse(null);
            if (participatorPet == null) {
                throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST);
            }
        }

        Long postId = idGenerator.nextId();
        Post post = new Post(postId, participator, content, relatePetId);

        for (ImageFile image : images) {
            image.fixedRatioResizeInplace(600, 600);
            String p = imageRepository.save(image);
            PostImage postImage = new PostImage(p);
            post.addImage(postImage);
        }

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

        return postRepository.save(post);
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
