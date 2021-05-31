package com.petfabula.domain.common.image;

public interface ImageRepository {

    String save(ImageFile imageFile);

    void remove(String uri);

}
