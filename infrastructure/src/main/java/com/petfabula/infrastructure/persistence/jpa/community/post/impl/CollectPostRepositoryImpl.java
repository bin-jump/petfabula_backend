package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.repository.CollectPostRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.CollectPostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CollectPostRepositoryImpl implements CollectPostRepository {

    @Autowired
    private CollectPostJpaRepository collectPostJpaRepository;

    @Autowired
    private PostRepository postRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size) {
        Specification<CollectPost> spec = new Specification<CollectPost>() {
            @Override
            public Predicate toPredicate(Root<CollectPost> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("postId")));
                Predicate aPred = cb.equal(root.get("participatorId"), participatorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("postId"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<CollectPost> collectPosts = collectPostJpaRepository.findAll(spec, limit);

        List<Long> postIds = collectPosts.getContent().stream()
                .map(CollectPost::getPostId).collect(Collectors.toList());

        List<Post> posts = postRepository.findByIds(postIds);

        return CursorPage.of(posts, collectPosts.hasNext(), size);
    }

    @Override
    public CollectPost save(CollectPost collectPost) {
        return collectPostJpaRepository.save(collectPost);
    }

    @Override
    public CollectPost find(Long participatorId, Long postId) {
        return collectPostJpaRepository.findByParticipatorIdAndPostId(participatorId, postId);
    }

    @Override
    public void remove(CollectPost likePost) {
        collectPostJpaRepository.delete(likePost);
    }
}
