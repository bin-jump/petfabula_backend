package com.petfabula.infrastructure.fileserver.image;

import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.infrastructure.fileserver.ftp.FtpProps;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository
public class ImageRepositoryFtpImpl implements ImageRepository {

    @Autowired
    private FtpProps ftpProps;

    @Autowired
    FtpRemoteFileTemplate ftpRemoteFileTemplate;

    @Override
    public String save(ImageFile imageFile) {
        String filePath = filePath(randFileName(imageFile.fileNameWithoutExtension(),
                imageFile.fileExtension()));

        ftpRemoteFileTemplate.execute(session -> {
            session.write(imageFile.getImageStream(), filePath);
            return null;
        });

        return filePath;
    }

    @Override
    public List<String> save(List<ImageFile> imageFiles) {
        return null;
    }

    @Override
    public void remove(String uri) {
        ftpRemoteFileTemplate.execute(session -> {
            if (session.exists(uri)) {
                session.remove(uri);
            }
            return null;
        });
    }

    private String filePath(String name) {
        return String.format("%s/%s", ftpProps.getImageFilePath(), name);
    }

    private String randFileName(String fileName, String ext) {

        String rd = RandomStringUtils.randomAlphanumeric(32);
        return String.format("%s-%s.%s", fileName, rd, ext);
    }
}

