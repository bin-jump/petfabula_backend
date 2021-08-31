package com.petfabula.application.pet;

import com.petfabula.application.event.IntegratedEventPublisher;
import com.petfabula.application.event.PetCreateEvent;
import com.petfabula.application.event.PetRemoveEvent;
import com.petfabula.application.event.PetUpdateEvent;
import com.petfabula.domain.aggregate.pet.entity.*;
import com.petfabula.domain.aggregate.pet.service.*;
import com.petfabula.domain.common.image.ImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class PetApplicationService {

    @Autowired
    private FeederService feederService;

    @Autowired
    private PetService petService;

    @Autowired
    private IntegratedEventPublisher eventPublisher;

    @Autowired
    private DisorderRecordService disorderRecordService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FeedRecordService feedRecordService;

    @Autowired
    private PetEventRecordService petEventRecordService;

    @Autowired
    private WeightRecordService weightRecordService;

    @Transactional
    public Pet createPet(Long feederId, String name, LocalDate birthday,
                      LocalDate arrivalDay, Pet.Gender gender, Integer weight,
                         Long breedId, String bio, ImageFile imageFile) {

        Pet pet = petService.create(feederId, name, birthday,
                arrivalDay, gender, weight,
                breedId, bio, imageFile);

        eventPublisher.publish(new PetCreateEvent(pet));
        return pet;
    }

    @Transactional
    public Pet updatePet(Long feederId, Long petId, String name, LocalDate birthday,
                         LocalDate arrivalDay, Pet.Gender gender, Integer weight,
                         Long breedId, String bio, ImageFile imageFile) {

        Pet pet = petService.update(feederId, petId, name, birthday,
                arrivalDay, gender, weight,
                breedId, bio, imageFile);

        eventPublisher.publish(new PetUpdateEvent(pet));
        return pet;
    }

    @Transactional
    public Pet removePet(Long feederId, Long petId) {

        Pet pet = petService.remove(feederId, petId);
        eventPublisher.publish(new PetRemoveEvent(petId));
        return pet;
    }

    @Transactional
    public DisorderRecord createDisorderRecord(Long feederId, Long petId, Instant dateTime, String disorderType, String content, List<ImageFile> images) {

        return disorderRecordService.create(feederId, petId, dateTime, disorderType, content, images);
    }

    @Transactional
    public FeedRecord createFeedRecord(Long feederId, Long petId, Instant dateTime, String foodContent,
                                       Integer amount, String note) {

        return feedRecordService.create(feederId, petId, dateTime, foodContent, amount, note);
    }

    @Transactional
    public PetEventRecord createPetEventRecord(Long feederId, Long petId, Instant dateTime, String eventType,
                                               String content, List<ImageFile> images) {

        return petEventRecordService.create(feederId, petId, dateTime, eventType, content, images);
    }

    @Transactional
    public WeightRecord createWeightRecord(Long feederId, Long petId, Instant dateTime, Integer weight) {

        return weightRecordService.create(feederId, petId, dateTime, weight);
    }

    @Transactional
    public MedicalRecord createMedicalRecord(Long feederId, Long petId, String hospitalName, String symptom,
                                             String diagnosis, String treatment, Instant date, String note, List<ImageFile> images) {

        return medicalRecordService.create(feederId, petId, hospitalName, symptom,
                diagnosis, treatment, date, note, images);
    }

    @Transactional
    public DisorderRecord updateDisorderRecord(Long feederId, Long petId, Long recordId, Instant dateTime, String disorderType,
                                              String content, List<ImageFile> images, List<Long> imageIds) {
        return disorderRecordService.update(feederId, petId, recordId, dateTime, disorderType, content, images, imageIds);
    }

    @Transactional
    public FeedRecord updateFeedRecord(Long feederId, Long petId, Long recordId, Instant dateTime, String foodContent,
                                           Integer amount, String note) {
        return feedRecordService.update(feederId, petId, recordId, dateTime, foodContent, amount, note);
    }

    @Transactional
    public MedicalRecord updateMedicalRecord(Long feederId, Long petId, Long recordId, String hospitalName, String symptom,
                                             String diagnosis, String treatment, Instant date, String note,
                                             List<ImageFile> images, List<Long> imageIds) {

        return medicalRecordService.update(feederId, petId, recordId, hospitalName, symptom,
                diagnosis, treatment, date, note, images, imageIds);
    }

    @Transactional
    public PetEventRecord updatePetEventRecord(Long feederId, Long petId, Long recordId, Instant dateTime, String eventType,
                                              String content, List<ImageFile> images, List<Long> imageIds) {

        return petEventRecordService.update(feederId, petId, recordId, dateTime, eventType, content, images, imageIds);
    }

    @Transactional
    public WeightRecord updateWeightRecord(Long feederId, Long petId, Long recordId, Instant dateTime, Integer weight) {

        return weightRecordService.update(feederId, petId, recordId, dateTime, weight);
    }

    @Transactional
    public DisorderRecord removeDisorderRecord(Long feederId, Long recordId) {
        return disorderRecordService.remove(feederId, recordId);
    }

    @Transactional
    public FeedRecord removeFeedRecord(Long feederId, Long recordId) {
        return feedRecordService.remove(feederId, recordId);
    }

    @Transactional
    public MedicalRecord removeMedicalRecord(Long feederId, Long recordId) {
        return medicalRecordService.remove(feederId, recordId);
    }

    @Transactional
    public PetEventRecord removePetEventRecord(Long feederId, Long recordId) {
        return petEventRecordService.remove(feederId, recordId);
    }

    @Transactional
    public WeightRecord removeWeightRecord(Long feederId, Long recordId) {
        return weightRecordService.remove(feederId, recordId);
    }
}

