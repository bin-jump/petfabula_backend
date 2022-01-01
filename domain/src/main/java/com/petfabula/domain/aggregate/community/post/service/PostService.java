package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.annotation.RestrictedAction;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.PostRemoved;
import com.petfabula.domain.aggregate.community.post.PostUpdated;
import com.petfabula.domain.aggregate.community.post.PostCreated;
import com.petfabula.domain.aggregate.community.post.entity.*;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.aggregate.community.post.repository.*;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    static int IMAGE_LIMIT = 6;

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
    private PostImageRepository postImageRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @RestrictedAction
    public Post create(Long participatorId, String content, Long relatePetId, Long topicId, List<ImageFile> images) {
        if (images.size() > IMAGE_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        ParticipatorPet participatorPet = null;
        if (relatePetId != null) {
            participatorPet = participatorPetRepository.findById(relatePetId);
            if (participatorPet == null || !participatorPet.getParticipatorId().equals(participatorId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        }

        Long postId = idGenerator.nextId();
        Post post = new Post(postId, participator, content, false, participatorPet);
//        for (ImageFile image : images) {
//            String p = imageRepository.save(image);
//            PostImage postImage = new PostImage(p);
//            post.addImage(postImage);
//        }

        participator.setPostCount(participator.getPostCount() + 1);
        participatorRepository.save(participator);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            PostImage postImage =
                    new PostImage(idGenerator.nextId(), path, postId, relatePetId, dimension.getWidth(), dimension.getHeight());
            post.addImage(postImage);

        }
        Post savedPost = postRepository.save(post);

        if (topicId != null) {
            PostTopic postTopic = postTopicRepository.findById(topicId);
            if (postTopic == null) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
            PostTopicRelation postTopicRelation =
                    new PostTopicRelation(idGenerator.nextId(), post.getId(), topicId, postTopic.getTopicCategoryId());
            postTopicRelationRepository.save(postTopicRelation);
        }

        domainEventPublisher.publish(new PostCreated(savedPost));
        return savedPost;
    }

    @RestrictedAction
    public Post update(Long participatorId, Long postId, String content, Long relatePetId, Long topicId,
                       List<ImageFile> images, List<Long> imageIds) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!post.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (relatePetId != null) {
            ParticipatorPet participatorPet = participatorPetRepository
                    .findByParticipatorId(post.getParticipator().getId()).stream()
                    .filter(p -> p.getId().equals(relatePetId))
                    .findAny().orElse(null);
            if (participatorPet == null) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
            post.setRelatePet(participatorPet);
        } else {
            post.setRelatePet(null);
        }

        PostTopicRelation topicRelation = postTopicRelationRepository.findByPostId(postId);
        if (topicId == null) {
            if (topicRelation != null) {
                postTopicRelationRepository.remove(topicRelation);
            }
        } else {
            PostTopic postTopic = postTopicRepository.findById(topicId);
            if (postTopic == null) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }

//            if (topicRelation != null && !topicRelation.getTopicCategoryId().equals(topicId)) {
//                postTopicRelationRepository.remove(topicRelation);
//            }

            if (topicRelation == null) {
                topicRelation = new PostTopicRelation(idGenerator.nextId(), post.getId(),
                        topicId, postTopic.getTopicCategoryId());
                postTopicRelationRepository.save(topicRelation);
            } else if (!topicRelation.getTopicCategoryId().equals(topicId)) {
                topicRelation.setTopicId(topicId);
                postTopicRelationRepository.save(topicRelation);
            }

        }
        post.setContent(content);
        List<Long> removeImageIds = new ArrayList<>();
        for (PostImage image : post.getImages()) {
            if (!imageIds.contains(image.getId())) {
                removeImageIds.add(image.getId());
            }
        }
        for (Long id : removeImageIds) {
            post.removeImage(id);
        }
        for (PostImage image : post.getImages()) {
            image.setPetId(relatePetId);
        }

        if ((images.size() + post.getImages().size()) > IMAGE_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            PostImage postImage =
                    new PostImage(idGenerator.nextId(), path, postId, relatePetId, dimension.getWidth(), dimension.getHeight());
            post.addImage(postImage);
        }

        domainEventPublisher.publish(new PostUpdated(post));
        return postRepository.save(post);
    }

    public Post remove(Long participatorId, Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            return null;
        }

        Participator participator = post.getParticipator();
        if (!participator.getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        for (PostImage img : post.getImages()) {
            postImageRepository.remove(img);
        }

        PostTopicRelation topicRelation = postTopicRelationRepository.findByPostId(post.getId());
        if (topicRelation != null) {
            postTopicRelationRepository.remove(topicRelation);
        }

        postRepository.remove(post);
        participator.setPostCount(participator.getPostCount() - 1);
        participatorRepository.save(participator);

        domainEventPublisher.publish(new PostRemoved(post));
        return post;
    }
}
