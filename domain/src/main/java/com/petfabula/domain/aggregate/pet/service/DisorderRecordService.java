package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.aggregate.pet.entity.DisorderRecordImage;
import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.respository.DisorderRecordRepository;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DisorderRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private DisorderRecordRepository disorderRecordRepository;

    @Autowired
    private ImageRepository imageRepository;

    public DisorderRecord create(Long feederId, Long petId, Instant dateTime, String disorderType, String content, List<ImageFile> images) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (images.size() > 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        DisorderRecord disorderRecord = new DisorderRecord(id, petId, dateTime, disorderType, content);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            DisorderRecordImage disorderRecordImage =
                    new DisorderRecordImage(idGenerator.nextId(), id, path, dimension.getWidth(), dimension.getHeight());
            disorderRecord.addImage(disorderRecordImage);
        }

        pet.setDisorderRecordCount(pet.getDisorderRecordCount() + 1);

        petRepository.save(pet);
        return disorderRecordRepository.save(disorderRecord);
    }
}
