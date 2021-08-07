package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.GenericPetRecord;
import com.petfabula.domain.common.paging.CursorPage;

public interface GenericPetRecordRepository {

    GenericPetRecord save(GenericPetRecord disorderRecord);

    GenericPetRecord findById(Long id);

    CursorPage<GenericPetRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(GenericPetRecord disorderRecord);
}
