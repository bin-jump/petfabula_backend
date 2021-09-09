package com.petfabula.infrastructure.persistence.jpa.community;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.domain.aggregate.community.post.service.PostIdGenerator;
import com.petfabula.infrastructure.tool.CsvHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InitialzePostTopics implements ApplicationRunner {

    @Autowired
    private PostTopicRepository postTopicRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @Value("classpath:data/post_topics.csv")
    private Resource resource;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        InputStream is = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Object[] arr = reader.lines().toArray();

        Map<String, Set<String>> categoryMap = new HashMap<>();
        int i = -1;
        for (Object l : arr) {
            i += 1;
            if (i == 0) {
                continue;
            }
            String[] parts = ((String)l).split(",");
            String category = CsvHelper.cleanCell(parts[0]);
            String topic = CsvHelper.cleanCell(parts[1]);

            if (!categoryMap.containsKey(category)) {
                categoryMap.put(category, new HashSet<>());
            }
            categoryMap.get(category).add(topic);
        }

        for (Map.Entry<String, Set<String>> entry : categoryMap.entrySet()) {
            String title = entry.getKey();
            PostTopicCategory topicCategory = postTopicRepository.findCategoryByTitle(title);
            if (topicCategory == null) {
                topicCategory = new PostTopicCategory(idGenerator.nextId(), title);
                postTopicRepository.save(topicCategory);
                log.info("add topic: " + title);
            }

            for (String topicTitle : entry.getValue()) {
                PostTopic topic = postTopicRepository.findTopicByTitle(topicTitle);
                if (topic == null) {
                    topic = new PostTopic(idGenerator.nextId(), topicTitle,
                            topicCategory.getId(), topicCategory.getTitle());
                    postTopicRepository.save(topic);
                }
            }
        }
    }
}
