package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.aggregate.community.post.repository.PostImageRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostImageJpaRepository;
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
public class PostImageRepositoryImpl implements PostImageRepository {

    @Autowired
    private PostImageJpaRepository postImageJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<PostImage> findByPetId(Long petId, Long cursor, int size) {
        Specification<PostImage> spec = new Specification<PostImage>() {
            @Override
            public Predicate toPredicate(Root<PostImage> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));

                Predicate pPred = cb.equal(root.get("petId"), petId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(pPred, cPred);
                }
                return pPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<PostImage> res = postImageJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(PostImage image) {
//        image.markDelete();
        postImageJpaRepository.save(image);
    }
}
