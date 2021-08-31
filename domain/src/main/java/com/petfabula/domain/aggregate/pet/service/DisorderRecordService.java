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
import java.util.ArrayList;
import java.util.List;

@Service
public class DisorderRecordService {

    static int IMAGE_LIMIT = 6;

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

        if (images.size() > IMAGE_LIMIT) {
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

    public DisorderRecord update(Long feederId, Long petId, Long recordId, Instant dateTime, String disorderType,
                                 String content, List<ImageFile> images, List<Long> imageIds) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        DisorderRecord record = disorderRecordRepository.findById(recordId);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!record.getPetId().equals(petId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        record.setDisorderType(disorderType);
        record.setContent(content);
        record.setDateTime(dateTime);

        List<Long> removeImageIds = new ArrayList<>();
        for (DisorderRecordImage image : record.getImages()) {
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
            DisorderRecordImage disorderRecordImage =
                    new DisorderRecordImage(idGenerator.nextId(), recordId, path, dimension.getWidth(), dimension.getHeight());
            record.addImage(disorderRecordImage);
        }

        return disorderRecordRepository.save(record);
    }

    public DisorderRecord remove(Long feederId, Long recordId) {
        DisorderRecord record = disorderRecordRepository.findById(recordId);
        if (record == null) {
            return null;
        }

        Pet pet = petRepository.findById(record.getPetId());
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        pet.setDisorderRecordCount(pet.getDisorderRecordCount() - 1);
        petRepository.save(pet);
        disorderRecordRepository.remove(record);
        return record;
    }

}
