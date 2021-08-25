package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostCommentJpaRepository;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostCommentRepositoryImpl implements PostCommentRepository {

    @Autowired
    private PostCommentJpaRepository postCommentJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<PostComment> findByPostId(Long postId, Long cursor, int size) {
        Specification<PostComment> spec = new Specification<PostComment>() {
            @Override
            public Predicate toPredicate(Root<PostComment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));

                Predicate pPred = cb.equal(root.get("postId"), postId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(pPred, cPred);
                }
                return pPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<PostComment> res = postCommentJpaRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public PostComment findById(Long id) {
        return postCommentJpaRepository.findById(id).orElse(null);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public List<PostComment> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return postCommentJpaRepository.findByIdIn(ids);
    }

    @Override
    public PostComment save(PostComment postComment) {
        return postCommentJpaRepository.save(postComment);
    }

    @Override
    public void remove(PostComment postComment) {
        postComment.markDelete();
        postCommentJpaRepository.save(postComment);
    }
}
