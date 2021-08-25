package com.petfabula.infrastructure.persistence.jpa.community.participator.impl;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.participator.repository.ParticipatorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ParticipatorRepositoryImpl implements ParticipatorRepository {

    @Autowired
    private ParticipatorJpaRepository participatorJpaRepository;

    @Override
    public Participator save(Participator participator) {
        return participatorJpaRepository.save(participator);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public Participator findById(Long id) {
        return participatorJpaRepository.findById(id).orElse(null);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public List<Participator> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return participatorJpaRepository.findByIdInOrderByIdDesc(ids);
    }
}
