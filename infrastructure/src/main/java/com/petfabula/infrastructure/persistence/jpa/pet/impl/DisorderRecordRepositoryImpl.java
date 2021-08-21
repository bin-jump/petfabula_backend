package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.aggregate.pet.respository.DisorderRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.DisorderRecordJpaRepository;
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
public class DisorderRecordRepositoryImpl implements DisorderRecordRepository {

    @Autowired
    private DisorderRecordJpaRepository disorderRecordJpaRepository;

    @Override
    public DisorderRecord save(DisorderRecord disorderRecord) {
        return disorderRecordJpaRepository.save(disorderRecord);
    }

    @Override
    public DisorderRecord findById(Long id) {
        return disorderRecordJpaRepository.findById(id).orElse(null);
    }

    @Override
    public CursorPage<DisorderRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<DisorderRecord> spec = new Specification<DisorderRecord>() {
            @Override
            public Predicate toPredicate(Root<DisorderRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("dateTime")), cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("petId"), petId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("dateTime"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<DisorderRecord> res = disorderRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(DisorderRecord disorderRecord) {
        disorderRecordJpaRepository.delete(disorderRecord);
    }
}
