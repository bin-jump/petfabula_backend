package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.RecommandPostRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostJpaRepository;
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

@Repository
public class RecommandPostRepositoryImpl implements RecommandPostRepository {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Override
    public CursorPage<Post> findRecentRecommand(Long cursor, int size) {
        Specification<Post> spec = new Specification<Post>() {

            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));

                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cPred;
                }
                return null;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Post> res = postJpaRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }
}
