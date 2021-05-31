package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.aggregate.community.entity.PostComment;
import com.petfabula.domain.aggregate.community.entity.PostTopicRelation;
import com.petfabula.domain.aggregate.community.repository.PostTopicRelationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.community.repository.PostTopicRelationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostTopicRelationRepositoryImpl implements PostTopicRelationRepository {

    @Autowired
    private PostTopicRelationJpaRepository postTopicRelationJpaRepository;

    @Override
    public PostTopicRelation save(PostTopicRelation postTopicRelation) {
        return postTopicRelationJpaRepository.save(postTopicRelation);
    }

    @Override
    public PostTopicRelation findByPostId(Long postId) {
        return postTopicRelationJpaRepository.findById(postId).orElse(null);
    }

    @Override
    public CursorPage<Post> findPostsByTopic(Long topicId, Long cursor, int size) {
        Specification<PostTopicRelation> spec = new Specification<PostTopicRelation>() {
            @Override
            public Predicate toPredicate(Root<PostTopicRelation> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("post").get("id")));

                Predicate pPred = cb.equal(root.get("postTopic").get("id"), topicId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("post").get("id"), cursor);
                    return cb.and(pPred, cPred);
                }
                return pPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<PostTopicRelation> res = postTopicRelationJpaRepository.findAll(spec, limit);
        List<Post> posts = res.getContent().stream()
                .map(PostTopicRelation::getPost).collect(Collectors.toList());

        return CursorPage.of(posts, res.hasNext(), size);
    }

}
