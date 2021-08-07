package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.GenericPetRecord;
import com.petfabula.domain.aggregate.pet.respository.GenericPetRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.GenericPetRecordJpaRepository;
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
public class GenericPetRecordRepositoryImpl implements GenericPetRecordRepository {

    @Autowired
    private GenericPetRecordJpaRepository genericPetRecordJpaRepository;

    @Override
    public GenericPetRecord save(GenericPetRecord disorderRecord) {
        return genericPetRecordJpaRepository.save(disorderRecord);
    }

    @Override
    public GenericPetRecord findById(Long id) {
        return genericPetRecordJpaRepository.findById(id).orElse(null);
    }

    @Override
    public CursorPage<GenericPetRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<GenericPetRecord> spec = new Specification<GenericPetRecord>() {
            @Override
            public Predicate toPredicate(Root<GenericPetRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        Page<GenericPetRecord> res = genericPetRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(GenericPetRecord disorderRecord) {
        genericPetRecordJpaRepository.delete(disorderRecord);
    }
}
