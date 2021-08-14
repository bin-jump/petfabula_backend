package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import com.petfabula.domain.aggregate.pet.entity.PetEventRecordImage;
import com.petfabula.domain.aggregate.pet.entity.PetEventType;
import com.petfabula.domain.aggregate.pet.respository.PetEventRecordRepository;
import com.petfabula.domain.aggregate.pet.respository.PetEventTypeRepository;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PetEventRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private PetEventRecordRepository petEventRecordRepository;

    @Autowired
    private PetEventTypeRepository petEventTypeRepository;

    @Autowired
    private ImageRepository imageRepository;

    public PetEventRecord create(Long feederId, Long petId, Instant dateTime, String eventType, String content, List<ImageFile> images) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        PetEventType petEventType = petEventTypeRepository.findByEventType(eventType);
        if (petEventType == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (images.size() > 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        PetEventRecord record = new PetEventRecord(id, petId, dateTime, eventType, content);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            PetEventRecordImage petEventRecordImage =
                    new PetEventRecordImage(idGenerator.nextId(), id, path, dimension.getWidth(), dimension.getHeight());
            record.addImage(petEventRecordImage);
        }

        pet.setEventRecordCount(pet.getEventRecordCount() + 1);
        petRepository.save(pet);

        return petEventRecordRepository.save(record);
    }
}
