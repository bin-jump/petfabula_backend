package com.petfabula.infrastructure.fileserver.image;

import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.ExternalServerException;
import com.petfabula.infrastructure.rest.MultipartInputStreamFileResource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ImageRepositoryImpl implements ImageRepository {

    @Value("${upload.imageUrl}")
    private String uploadUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String save(ImageFile imageFile) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        ResponseItem[] response;

        try {
            map.add("images", new MultipartInputStreamFileResource(imageFile.getInputStream(),
                    imageFile.getOriginalFilename()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(uploadUrl, requestEntity, ResponseItem[].class);

        } catch (HttpStatusCodeException e) {
            throw new ExternalServerException("Failed to save image files");
        }

        return response[0].getPath();
    }

    @Override
    public List<String> save(List<ImageFile> imageFiles) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        ResponseItem[] response;

        try {
            for (ImageFile file : imageFiles) {
                map.add("images", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(uploadUrl, requestEntity, ResponseItem[].class);

        } catch (HttpStatusCodeException e) {
            throw new ExternalServerException("Failed to save image files \n" + e);
        }

        return Arrays.asList(response).stream()
                .map(ResponseItem::getPath).collect(Collectors.toList());

    }

    @Override
    public void remove(String uri) {
        throw new UnsupportedOperationException();
    }

}

