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
import java.util.ArrayList;
import java.util.List;

@Service
public class PetEventRecordService {

    static int IMAGE_LIMIT = 6;

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

    public PetEventRecord update(Long feederId, Long petId, Long recordId, Instant dateTime, String eventType,
                                 String content, List<ImageFile> images, List<Long> imageIds) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PetEventRecord record = petEventRecordRepository.findById(recordId);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }
        if (!record.getPetId().equals(petId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PetEventType petEventType = petEventTypeRepository.findByEventType(eventType);
        if (petEventType == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        record.setContent(content);
        record.setEventType(eventType);
        record.setDateTime(dateTime);

        List<Long> removeImageIds = new ArrayList<>();
        for (PetEventRecordImage image : record.getImages()) {
            if (!imageIds.contains(image.getId())) {
                removeImageIds.add(image.getId());
            }
        }
        for (Long id : removeImageIds) {
            record.removeImage(id);
        }

        if ((images.size() + record.getImages().size()) > IMAGE_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            PetEventRecordImage image =
                    new PetEventRecordImage(idGenerator.nextId(), recordId, path, dimension.getWidth(), dimension.getHeight());
            record.addImage(image);
        }

        return petEventRecordRepository.save(record);
    }

    public PetEventRecord remove(Long feederId, Long recordId) {
        PetEventRecord record = petEventRecordRepository.findById(recordId);
        if (record == null) {
            return null;
        }

        Pet pet = petRepository.findById(record.getPetId());
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        pet.setEventRecordCount(pet.getEventRecordCount() - 1);
        petRepository.save(pet);
        petEventRecordRepository.remove(record);
        return record;
    }

}
