package com.petfabula.infrastructure.fileserver.image;

import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.ExternalServerException;
import com.petfabula.infrastructure.fileserver.ftp.FtpProps;
import com.petfabula.infrastructure.rest.MultipartInputStreamFileResource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ImageRepositoryImpl implements ImageRepository {

    @Value("${image.uploadUrl}")
    private String uploadUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String save(ImageFile imageFile) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        List<ResponseItem> response;

        try {
            map.add("images", new MultipartInputStreamFileResource(imageFile.getInputStream(),
                    imageFile.getOriginalFilename()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(uploadUrl, requestEntity, List.class);

        } catch (HttpStatusCodeException e) {
            throw new ExternalServerException("Failed to save image files");
        }

        return response.get(0).getPath();
    }

    @Override
    public List<String> save(List<ImageFile> imageFiles) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        List<ResponseItem> response;

        try {
            for (ImageFile file : imageFiles) {
                map.add("images", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(uploadUrl, requestEntity, List.class);

        } catch (HttpStatusCodeException e) {
            throw new ExternalServerException("Failed to save image files");
        }

        return response.stream().map(ResponseItem::getPath).collect(Collectors.toList());

    }

    @Override
    public void remove(String uri) {
        throw new UnsupportedOperationException();
    }

    @Data
    class ResponseItem {

        private String name;

        private String path;


    }

}

