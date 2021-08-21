package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import com.petfabula.domain.aggregate.pet.respository.MedicalRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.MedicalRecordJpaRepository;
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
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    @Autowired
    private MedicalRecordJpaRepository medicalRecordJpaRepository;

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordJpaRepository.save(medicalRecord);
    }

    @Override
    public MedicalRecord findById(Long id) {
        return medicalRecordJpaRepository.findById(id).orElse(null);
    }

    @Override
    public CursorPage<MedicalRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<MedicalRecord> spec = new Specification<MedicalRecord>() {
            @Override
            public Predicate toPredicate(Root<MedicalRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        Page<MedicalRecord> res = medicalRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(MedicalRecord medicalRecord) {
        medicalRecordJpaRepository.delete(medicalRecord);
    }
}
