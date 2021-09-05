package com.petfabula.infrastructure.persistence.jpa.pet;

import com.petfabula.domain.aggregate.pet.entity.PetEventType;
import com.petfabula.domain.aggregate.pet.respository.PetEventTypeRepository;
import com.petfabula.domain.aggregate.pet.service.PetIdGenerator;
import com.petfabula.infrastructure.tool.CsvHelper;
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
import java.util.HashSet;

@Component
@Slf4j
public class InitialzePetEventType implements ApplicationRunner {

    @Autowired
    private PetEventTypeRepository petEventTypeRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Value("classpath:data/pet_event_types.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        for (Object l : arr) {
            String[] parts = ((String)l).split(",");
            String category = CsvHelper.cleanCell(parts[0]);
            String eventType = CsvHelper.cleanCell(parts[1]);
            if (category.length() == 0) {
                category = null;
            }
            inserSingle(category, eventType);
        }
    }

    private void inserSingle(String category, String eventType) {
        PetEventType petEventType = petEventTypeRepository.findByEventType(eventType);
        if (petEventType == null) {
            petEventType = new PetEventType(idGenerator.nextId(), category, eventType);
            petEventTypeRepository.save(petEventType);
        }
    }
}
