package com.petfabula.domain.common.image;


import com.google.common.io.Files;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class ImageFile {

    // limit the image size for whole application.
    public static int IMAGE_SIZE_MAX = 5 * 1024 * 1024;

    public static int IMAGE_SIDE_LENGTH_MIN = 100;

    public ImageFile(String fileName, InputStream inputStream, long size) throws IOException {
        if (size > IMAGE_SIZE_MAX) {
            throw new InvalidOperationException(String
                    .format("Image size %d must not exceed %d byte",
                            size, IMAGE_SIZE_MAX));
        }
        this.orgFileName = fileName;
        this.imageType = getImageTypeByExtension(Files
                .getFileExtension(fileName).toUpperCase());
        this.data = inputStream;
    }

    private InputStream data;

    private String orgFileName;

    private ImageType imageType;


    public String getOriginalFilename() {
        return orgFileName;
    }

    public InputStream getInputStream() {
        return data;
    }


    private static ImageType getImageTypeByExtension(String extention) {
        if (!isValidImageFormat(extention)) {
            throw new InvalidOperationException(String
                    .format("Image type %s not supported", extention));
        }

        return ImageType.valueOf(extention.toUpperCase());
    }

    private static boolean isValidImageFormat(String formatName) {
        for (ImageType t : ImageType.values()) {
            if (t.name().equals(formatName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }


}

