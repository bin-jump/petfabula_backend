package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.repository.PostJpaRepository;
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

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size) {
        Specification<Post> spec = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("participator").get("id"), participatorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Post> res = postJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public Post findById(Long id) {
        return postJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Post post) {
        post.markDelete();
        postJpaRepository.save(post);
    }
}
