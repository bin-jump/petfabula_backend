package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.Feeder;
import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import com.petfabula.domain.aggregate.pet.respository.FeederRepository;
import com.petfabula.domain.aggregate.pet.respository.PetBreedRepository;
import com.petfabula.domain.aggregate.pet.respository.PetCategoryRepository;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private FeederRepository feederRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    public Pet create(Long feederId, String name, LocalDate birthday,
                      LocalDate arrivalDay, Pet.Gender gender, Integer weight,
                      Long breedId, String bio, ImageFile imageFile){
        Feeder feeder = feederRepository.findById(feederId);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Pet pet = petRepository.findByFeederIdAndName(feederId, name);
        if (pet != null) {
            throw new InvalidValueException("name", CommonMessageKeys.NAME_ALREADY_EXISTS);
        }

        PetBreed petBreed = petBreedRepository.findById(breedId);
        if (petBreed == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        String imageUrl = null;
        if (imageFile != null) {
            imageUrl = imageRepository.save(imageFile);
        }

        Long id = idGenerator.nextId();
        pet = new Pet(id, feederId, name, imageUrl, birthday, arrivalDay, gender,
                weight, petBreed.getCategoryId(), petBreed.getCategory(), breedId, bio);

        feeder.setPetCount(feeder.getPetCount() + 1);
        feederRepository.save(feeder);

        return petRepository.save(pet);
    }

    public Pet update(Long feederId, Long petId, String name, LocalDate birthday,
                      LocalDate arrivalDay, Pet.Gender gender, Integer weight,
                      Long breedId, String bio, ImageFile imageFile){
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PetBreed petBreed = petBreedRepository.findById(breedId);
        if (petBreed == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (!pet.getName().equals(name)) {
            Pet sameName = petRepository.findByFeederIdAndName(feederId, name);
            if (sameName != null) {
                throw new InvalidValueException("name", CommonMessageKeys.NAME_ALREADY_EXISTS);
            }
        }

        String imageUrl = pet.getPhoto();
        if (imageFile != null) {
            imageUrl = imageRepository.save(imageFile);
        }

        pet.update(name, imageUrl, birthday, arrivalDay, gender,
                weight, petBreed.getCategoryId(), petBreed.getCategory(), breedId, bio);

        return petRepository.save(pet);
    }

    public Pet remove(Long feederId, Long petId){
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            return null;
        }

        if (!pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        petRepository.remove(pet);
        return pet;
    }

}
