package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import com.petfabula.domain.aggregate.pet.entity.MedicalRecordImage;
import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.respository.MedicalRecordRepository;
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
public class MedicalRecordService {

    static int IMAGE_LIMIT = 6;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private ImageRepository imageRepository;

    public MedicalRecord create(Long feederId, Long petId, String hospitalName, String symptom,
                                String diagnosis, String treatment, Instant dateTime, String note, List<ImageFile> images) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (images.size() > IMAGE_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        dateTime = RecordHelper.removeSecondInfo(dateTime);
        MedicalRecord record = medicalRecordRepository.findByPetIdAndDateTime(petId, dateTime);
        if (record != null) {
            throw new InvalidOperationException(CommonMessageKeys.RECORD_ALREADY_EXISTS);
        }

        Long id = idGenerator.nextId();
        record = new MedicalRecord(id, petId, hospitalName, symptom,
                diagnosis, treatment, dateTime, note);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            MedicalRecordImage disorderRecordImage =
                    new MedicalRecordImage(idGenerator.nextId(), id, path, dimension.getWidth(), dimension.getHeight());
            record.addImage(disorderRecordImage);
        }

        pet.setMedicalRecordCount(pet.getMedicalRecordCount() + 1);
        petRepository.save(pet);

        return medicalRecordRepository.save(record);
    }

    public MedicalRecord update(Long feederId, Long petId, Long recordId, String hospitalName, String symptom,
                                String diagnosis, String treatment, Instant dateTime, String note,
                                List<ImageFile> images, List<Long> imageIds) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        MedicalRecord record = medicalRecordRepository.findById(recordId);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!record.getPetId().equals(petId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        dateTime = RecordHelper.removeSecondInfo(dateTime);
        if (!dateTime.equals(record.getDateTime())) {
            MedicalRecord sameTime = medicalRecordRepository.findByPetIdAndDateTime(petId, dateTime);
            if (sameTime != null) {
                throw new InvalidOperationException(CommonMessageKeys.RECORD_ALREADY_EXISTS);
            }
        }

        record.setHospitalName(hospitalName);
        record.setSymptom(symptom);
        record.setDiagnosis(diagnosis);
        record.setTreatment(treatment);
        record.setDateTime(dateTime);
        record.setNote(note);

        List<Long> removeImageIds = new ArrayList<>();
        for (MedicalRecordImage image : record.getImages()) {
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
            MedicalRecordImage image =
                    new MedicalRecordImage(idGenerator.nextId(), recordId, path, dimension.getWidth(), dimension.getHeight());
            record.addImage(image);
        }

        return medicalRecordRepository.save(record);
    }

    public MedicalRecord remove(Long feederId, Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId);
        if (record == null) {
            return null;
        }

        Pet pet = petRepository.findById(record.getPetId());
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        pet.setMedicalRecordCount(pet.getMedicalRecordCount() - 1);
        petRepository.save(pet);

        medicalRecordRepository.remove(record);
        return record;
    }
}
