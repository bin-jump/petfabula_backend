package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.domain.aggregate.pet.respository.WeightRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.WeightRecordJpaRepository;
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
public class WeightRecordRepositoryImpl implements WeightRecordRepository {

    @Autowired
    private WeightRecordJpaRepository weightRecordJpaRepository;

    @Override
    public WeightRecord save(WeightRecord weightRecord) {
        return weightRecordJpaRepository.save(weightRecord);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public WeightRecord findById(Long id) {
        return weightRecordJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public CursorPage<WeightRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<WeightRecord> spec = new Specification<WeightRecord>() {
            @Override
            public Predicate toPredicate(Root<WeightRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        Page<WeightRecord> res = weightRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(WeightRecord weightRecord) {
        weightRecord.markDelete();
        weightRecordJpaRepository.save(weightRecord);
    }
}
