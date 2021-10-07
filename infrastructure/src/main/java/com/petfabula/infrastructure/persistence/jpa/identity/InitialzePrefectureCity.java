package com.petfabula.infrastructure.persistence.jpa.identity;

import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.entity.Prefecture;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.domain.aggregate.identity.repository.PrefectureRepository;
import com.petfabula.domain.aggregate.identity.service.IdentityIdGenerator;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InitialzePrefectureCity implements ApplicationRunner {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PrefectureRepository prefectureRepository;

    @Autowired
    private IdentityIdGenerator idGenerator;

    @Value("classpath:data/cities.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        Map<String, Set<String>> prefectureMap = new HashMap<>();
        int i = -1;
        for (Object l : arr) {
            i += 1;
            if (i == 0) {
                continue;
            }
            String[] parts = ((String)l).split(",");
            String prefecture = CsvHelper.cleanCell(parts[1]);
            String city = CsvHelper.cleanCell(parts[3]);

            if (!prefectureMap.containsKey(prefecture)) {
                prefectureMap.put(prefecture, new HashSet<>());
            }
            prefectureMap.get(prefecture).add(city);
        }

        for (Map.Entry<String, Set<String>> entry : prefectureMap.entrySet()) {
            String name = entry.getKey();
            Prefecture prefecture = prefectureRepository.findByName(name);
            if (prefecture == null) {
                prefecture = new Prefecture(idGenerator.nextId(), name);
                prefectureRepository.save(prefecture);
                log.info("add prefecture: " + prefecture);
            }

            Long prefectureId = prefecture.getId();
            for (String cityName : entry.getValue()) {
                City city = cityRepository
                        .findByPrefectureIdAndName(prefectureId, cityName);
                if (city == null) {
                    city = new City(idGenerator.nextId(), cityName,
                            prefecture.getName(), prefectureId);
                    cityRepository.save(city);
                }
            }
        }

    }
}
