package com.petfabula.domain.common.image;

import java.util.List;

public interface ImageRepository {

    String save(ImageFile imageFile);

    List<String> save(List<ImageFile> imageFiles);

    void remove(String uri);

}
