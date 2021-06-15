package com.petfabula.domain.common.image;


import com.google.common.io.Files;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

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

        getImageDimension();
    }

    private InputStream data;

    private String orgFileName;

    private ImageType imageType;

    private ImageDimension dimension;

    public String getOriginalFilename() {
        return orgFileName;
    }

    public InputStream getInputStream() {
        return data;
    }

    // extract dimension and reset inputStream
    public ImageDimension getImageDimension() {
        if (dimension != null) {
            return this.dimension;
        }
        try {
            setDimensionThumbnails();
            return this.dimension;
        } catch (IOException e) {
            throw new RuntimeException("failed to get image dimension");
        }
    }

    private void setDimensionThumbnails() throws IOException {

        BufferedImage bm = Thumbnails.of(this.data)
                .scale(1)
                .asBufferedImage();
        this.dimension = new ImageDimension(bm.getWidth(), bm.getHeight());
        // rebuild inputStream
        NoneCopyByteArrayOutputStream os = new NoneCopyByteArrayOutputStream();
        ImageIO.write(bm, "jpg", os);
        this.data = new ByteArrayInputStream(os.toByteArray());

    }

    private void setDimension() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(this.data, baos);
        byte[] bytes = baos.toByteArray();

        try(ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))){
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    this.dimension = new ImageDimension(reader.getWidth(0),
                            reader.getHeight(0));

                    this.data = new ByteArrayInputStream(bytes);
                } finally {
                    reader.dispose();
                }
            }
        }
        throw new RuntimeException("failed to get image dimension");
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

