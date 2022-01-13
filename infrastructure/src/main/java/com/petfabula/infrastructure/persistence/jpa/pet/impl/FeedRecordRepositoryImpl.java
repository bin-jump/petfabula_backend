package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.domain.aggregate.pet.respository.FeedRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.FeedRecordJpaRepository;
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
import java.time.Instant;
import java.util.List;

@Repository
public class FeedRecordRepositoryImpl implements FeedRecordRepository {

    @Autowired
    private FeedRecordJpaRepository feedRecordJpaRepository;

    @Override
    public FeedRecord save(FeedRecord disorderRecord) {
        return feedRecordJpaRepository.save(disorderRecord);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public FeedRecord findById(Long id) {
        return feedRecordJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public FeedRecord findByPetIdAndDateTime(Long petId, Instant dateTime) {
        return feedRecordJpaRepository.findByPetIdAndDateTime(petId, dateTime);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public CursorPage<FeedRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<FeedRecord> spec = new Specification<FeedRecord>() {
            @Override
            public Predicate toPredicate(Root<FeedRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("dateTime")), cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("petId"), petId);
                if (cursor != null) {
                    Predicate dateTimePred = cb.lessThan(root.get("dateTime"), Instant.ofEpochMilli(cursor));
                    return cb.and(aPred, dateTimePred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<FeedRecord> res = feedRecordJpaRepository.findAll(spec, limit);
        Long cr = res.hasNext() ? res.getContent().get(res.getContent().size() - 1).getDateTime().toEpochMilli() : null;
        return CursorPage.of(res.getContent(), cr, size);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public List<FeedRecord> findByPetIdAndAfter(Long petId, Instant dateTime, int sizeLimit) {
        Pageable limit = PageRequest.of(0, sizeLimit);
        return feedRecordJpaRepository.findAllByPetIdAndDateTimeGreaterThanEqualOrderByDateTimeDesc(petId, dateTime, limit);
    }

    @Override
    public void remove(FeedRecord disorderRecord) {
        disorderRecord.markDelete();
        feedRecordJpaRepository.save(disorderRecord);
    }
}
