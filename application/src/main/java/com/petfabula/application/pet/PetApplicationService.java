package com.petfabula.application.pet;

import com.petfabula.application.event.IntegratedEventPublisher;
import com.petfabula.application.event.PetCreateEvent;
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
                      LocalDate arrivalDay, Pet.Gender gender, Double weight,
                         Long breedId, String bio) {

        Pet pet = petService.create(feederId, name, birthday,
                arrivalDay, gender, weight,
                breedId, bio);

        eventPublisher.publish(new PetCreateEvent(pet));
        return pet;
    }

    @Transactional
    public DisorderRecord createDisorderRecord(Long feederId, Long petId, Instant dateTime, String disorderType, String content, List<ImageFile> images) {

        return disorderRecordService.create(feederId, petId, dateTime, disorderType, content, images);
    }

    @Transactional
    public FeedRecord createFeedRecord(Long feederId, Long petId, Instant dateTime, String foodContent, Integer amount, String note) {

        return feedRecordService.create(feederId, petId, dateTime, foodContent, amount, note);
    }

    @Transactional
    public PetEventRecord createPetEventRecord(Long feederId, Long petId, Instant dateTime, String eventType, String content, List<ImageFile> images) {

        return petEventRecordService.create(feederId, petId, dateTime, eventType, content, images);
    }

    @Transactional
    public WeightRecord createWeightRecord(Long feederId, Long petId, Instant date, Double weight) {

        return weightRecordService.create(feederId, petId, date, weight);
    }

    @Transactional
    public MedicalRecord createMedicalRecord(Long feederId, Long petId, String hospitalName, String symptom,
                                             String diagnosis, String treatment, Instant date, String note, List<ImageFile> images) {

        return medicalRecordService.create(feederId, petId, hospitalName, symptom,
                diagnosis, treatment, date, note, images);
    }
}
