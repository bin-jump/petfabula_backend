package com.petfabula.presentation.web.controller;

import com.petfabula.domain.aggregate.community.participator.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.CollectPostRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.dto.community.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participator")
@Validated
public class ParticipatorController {

    static final int DEAULT_PAGE_SIZE = 5;

    @Autowired
    private ParticiptorPetAssembler participtorPetAssembler;

    @Autowired
    private ParticiptorAssembler participtorAssembler;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private FollowParticipatorRepository followParticipatorRepository;

    @Autowired
    private CollectPostRepository collectPostRepository;

    @GetMapping("profile")
    public Response<ParticipatorDto> getMyProfile() {
        Long userId = LoginUtils.currentUserId();
        Participator participator = participatorRepository.findById(userId);
        ParticipatorDto participatorDto = participtorAssembler.convertToDto(participator);
        return Response.ok(participatorDto);
    }

    @GetMapping("{userId}/profile")
    public Response<ParticipatorDto> getUserProfile(@PathVariable("userId") Long userId) {
        Participator participator = participatorRepository.findById(userId);
        Long myId = LoginUtils.currentUserId();
        ParticipatorDto participatorDto = participtorAssembler.convertToDto(participator);
        if (myId != null && !userId.equals(myId)) {
            FollowParticipator followParticipator =
                    followParticipatorRepository.find(myId, userId);
            participatorDto.setFollowed(followParticipator != null);
        }
        return Response.ok(participatorDto);
    }

    @GetMapping("{userId}/posts")
    public Response<CursorPageData<PostDto>> getUserPosts(@PathVariable("userId") Long userId,
                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = postRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("{userId}/questions")
    public Response<CursorPageData<QuestionDto>> getUserQuestions(@PathVariable("userId") Long userId,
                                                                  @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Question> questions = questionRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<QuestionDto> res = CursorPageData
                .of(questionAssembler.convertToDtos(questions.getResult()), questions.isHasMore(),
                        questions.getPageSize(), questions.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("{userId}/answers")
    public Response<CursorPageData<AnswerWithQuestionDto>> getUserAnswers(@PathVariable("userId") Long userId,
                                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Answer> answers = answerRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        List<Long> questionIds = answers.getResult().stream()
                .map(Answer::getQuestionId).collect(Collectors.toList());
        Map<Long, Question> questionMap = questionRepository.findByIds(questionIds)
                .stream().collect(Collectors.toMap(Question::getId, item -> item));
        List<AnswerWithQuestionDto> answerWithQuestionDtos = new ArrayList<>();
        answers.getResult().forEach(item -> {
            answerWithQuestionDtos.add(answerAssembler
                    .convertToDto(item, questionMap.get(item.getQuestionId())));
        });

        CursorPageData<AnswerWithQuestionDto> res = CursorPageData
                .of(answerWithQuestionDtos, answers.isHasMore(),
                        answers.getPageSize(), answers.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("{userId}/collected-posts")
    public Response<CursorPageData<PostDto>> getUserCollectedPosts(@PathVariable("userId") Long userId,
                                                                  @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = collectPostRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

//    @GetMapping("{userId}/pets")
//    public Response<List<ParticipatorPetDto>> getUserPets(@PathVariable("userId") Long userId) {
//        List<ParticipatorPet> pets = participatorPetRepository.findByParticipatorId(userId);
//        List<ParticipatorPetDto> res = participtorPetAssembler.convertToDtos(pets);
//        return Response.ok(res);
//    }
}
