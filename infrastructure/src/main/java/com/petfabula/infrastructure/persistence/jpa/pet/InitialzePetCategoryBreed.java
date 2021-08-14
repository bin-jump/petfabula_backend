package com.petfabula.infrastructure.persistence.jpa.pet;

import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import com.petfabula.domain.aggregate.pet.respository.PetBreedRepository;
import com.petfabula.domain.aggregate.pet.respository.PetCategoryRepository;
import com.petfabula.domain.aggregate.pet.service.PetIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
@Slf4j
public class InitialzePetCategoryBreed implements ApplicationRunner {

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Value("classpath:data/pets.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        Map<String, Set<String>> categoryBreedMap = new HashMap<>();
        for (Object l : arr) {
            String[] parts = ((String)l).split(",");
            String category = parts[0].trim();
            String breed = parts[1].trim();

            if (!categoryBreedMap.containsKey(category)) {
                categoryBreedMap.put(category, new HashSet<>());
            }
            categoryBreedMap.get(category).add(breed);
        }

        for (Map.Entry<String, Set<String>> entry : categoryBreedMap.entrySet()) {
            String name = entry.getKey();
            PetCategory category = petCategoryRepository.findByName(name);
            if (category == null) {
                category = new PetCategory(idGenerator.nextId(), name);
                petCategoryRepository.save(category);
                log.info("add category: " + category);
            }

            Long categoryId = category.getId();
            for (String breed : entry.getValue()) {
                PetBreed petBreed = petBreedRepository.findByCategoryIdName(categoryId, breed);
                if (petBreed == null) {
                    petBreed = new PetBreed(idGenerator.nextId(), categoryId,
                            category.getName(), breed);
                    petBreedRepository.save(petBreed);
                }
            }

        }
    }
}
