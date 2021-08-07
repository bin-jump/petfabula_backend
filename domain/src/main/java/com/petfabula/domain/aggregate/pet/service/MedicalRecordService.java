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
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private ImageRepository imageRepository;

    public MedicalRecord create(Long petId, String hospitalName, String symptom,
                                String diagnosis, String treatment, Instant date, String note, List<ImageFile> images) {
        Pet pet = petRepository.findById(petId);
        if (pet != null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (images.size() > 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        MedicalRecord record = new MedicalRecord(id, petId, hospitalName, symptom,
                diagnosis, treatment, date, note);

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
}
