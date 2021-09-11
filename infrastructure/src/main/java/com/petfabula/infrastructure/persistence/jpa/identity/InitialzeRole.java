package com.petfabula.infrastructure.persistence.jpa.identity;

import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.repository.RoleRepository;
import com.petfabula.domain.aggregate.identity.service.IdentityIdGenerator;
import com.petfabula.infrastructure.tool.CsvHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
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
@Order(2)
public class InitialzeRole implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IdentityIdGenerator idGenerator;

    @Value("classpath:data/user_roles.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        Set<String> nameSet = new HashSet<>();
        int i = -1;
        for (Object l : arr) {
            i += 1;
            if (i == 0) {
                continue;
            }
            String[] parts = ((String)l).split(",");
            String name = CsvHelper.cleanCell(parts[0]);
            nameSet.add(name);
        }

        for (String name : nameSet) {
            Role role = roleRepository.findByName(name);
            if (role == null) {
                role = new Role(idGenerator.nextId(), name);
                roleRepository.save(role);
            }
        }
    }
}
