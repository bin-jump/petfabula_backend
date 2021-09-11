package com.petfabula.presentation.data;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class InitialzeAdmin implements ApplicationRunner {


    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Value("classpath:data/initial_admins.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        int i = -1;
        for (Object l : arr) {
            i += 1;
            if (i == 0) {
                continue;
            }
            String[] parts = ((String)l).split(",");
            String name = CsvHelper.cleanCell(parts[0]);
            String email = CsvHelper.cleanCell(parts[1]);
            String pass = CsvHelper.cleanCell(parts[2]);

            UserAccount account = userAccountRepository.findByName(name);
            if (account != null) {
                return;
            }

            List<String> roleNames =
                    new ArrayList<>(Arrays. asList("ADMIN"));

            identityApplicationService
                    .createByEmailPasswordUntrustedEmailWithRoles(name, email, pass, roleNames);
        }
    }
}
