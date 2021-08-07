package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import com.petfabula.domain.aggregate.pet.respository.PetEventRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.PetEventRecordJpaRepository;
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
public class PetEventRecordRepositoryImpl implements PetEventRecordRepository {

    @Autowired
    private PetEventRecordJpaRepository petEventRecordJpaRepository;

    @Override
    public PetEventRecord save(PetEventRecord petEventRecord) {
        return petEventRecordJpaRepository.save(petEventRecord);
    }

    @Override
    public PetEventRecord findById(Long id) {
        return petEventRecordJpaRepository.findById(id).orElse(null);
    }

    @Override
    public CursorPage<PetEventRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<PetEventRecord> spec = new Specification<PetEventRecord>() {
            @Override
            public Predicate toPredicate(Root<PetEventRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.asc(root.get("id")));
                Predicate aPred = cb.equal(root.get("petId"), petId);
                if (cursor != null) {
                    Predicate cPred = cb.greaterThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<PetEventRecord> res = petEventRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(PetEventRecord petEventRecord) {
        petEventRecordJpaRepository.delete(petEventRecord);
    }
}
