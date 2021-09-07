package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class PostRecommendationService {

    private Random random = new Random();

    @Autowired
    private PostRecommendationRepository postRecommendationRepository;

    @Autowired
    private RecommendationIdGenerator recommendationIdGenerator;

    public RecommendationResult<Post> randomRecommendation(int page, int size, Integer seed, Long cursor) {
        if (page > 1 && (seed == null || cursor == null) || page < 1) {
            throw new InvalidOperationException("");
        }
        if (page == 1) {
            seed = random.nextInt();
        }

        RecommendationResult<Post> recommendations = postRecommendationRepository
                .findRandomRecommend(page, size, seed, cursor);

        return recommendations;
    }

    public void addNewPost(Post post) {
        PostRecommendation postRecommendation = postRecommendationRepository
                .findByPostId(post.getId());
        if (postRecommendation != null) {
            return;
        }

        Long id = recommendationIdGenerator.nextId();
        postRecommendation = new PostRecommendation(id, post.getId());
        postRecommendationRepository.save(postRecommendation);
    }

    public void remove(Long postId) {
        PostRecommendation postRecommendation = postRecommendationRepository.findByPostId(postId);
        if (postRecommendation != null) {
            postRecommendationRepository.remove(postRecommendation);
        }

    }

}
