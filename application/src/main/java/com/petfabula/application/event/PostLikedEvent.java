package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PostLikedEvent implements Serializable {

    private Long postId;

    private Long participatorId;
}
