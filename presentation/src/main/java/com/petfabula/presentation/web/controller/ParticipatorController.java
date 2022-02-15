package com.petfabula.presentation.web.controller;

import com.petfabula.application.community.PostApplicationService;
import com.petfabula.domain.aggregate.community.block.entity.BlockRecord;
import com.petfabula.domain.aggregate.community.block.repository.BlockRecordRepository;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.aggregate.community.post.repository.CollectPostRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostImageRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.post.repository.TimelineRepository;
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
import org.springframework.security.access.annotation.Secured;
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

    static final int DEAULT_PAGE_SIZE = 10;

    @Autowired
    private ParticiptorPetAssembler participtorPetAssembler;

    @Autowired
    private PostApplicationService postApplicationService;

    @Autowired
    private ParticiptorAssembler participtorAssembler;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private TimelineRepository timeLineRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private BlockRecordRepository blockRecordRepository;

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

            BlockRecord blockRecord = blockRecordRepository.find(myId, userId);
            participatorDto.setBlocked(blockRecord != null);
        }
        return Response.ok(participatorDto);
    }

    @GetMapping("my-posts")
    public Response<CursorPageData<PostDto>> getMyPosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        return getUserPosts(userId, cursor);
    }

    @GetMapping("my-questions")
    public Response<CursorPageData<QuestionDto>> getMyQuestions(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        return getUserQuestions(userId, cursor);
    }

    @GetMapping("my-answers")
    public Response<CursorPageData<AnswerWithQuestionDto>> getMyAnswers(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        return getUserAnswers(userId, cursor);
    }

    @GetMapping("my-favorite-posts")
    public Response<CursorPageData<PostDto>> getMyFavoritePosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        return getUserFavoritePosts(userId, cursor);
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
            if (questionMap.containsKey(item.getQuestionId())) {
                answerWithQuestionDtos.add(answerAssembler
                        .convertToDto(item, questionMap.get(item.getQuestionId())));
            }
        });

        CursorPageData<AnswerWithQuestionDto> res = CursorPageData
                .of(answerWithQuestionDtos, answers.isHasMore(),
                        answers.getPageSize(), answers.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("{userId}/favorite-posts")
    public Response<CursorPageData<PostDto>> getUserFavoritePosts(@PathVariable("userId") Long userId,
                                                                  @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = collectPostRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("followed-posts")
    public Response<CursorPageData<PostDto>> geTimelinePosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<Post> posts = timeLineRepository.findFollowedByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/posts")
    public Response<CursorPageData<PostDto>> getPetPosts(@PathVariable("petId") Long petId,
                                                         @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = postRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/images")
    public Response<CursorPageData<PostImageDto>> getPetPostImages(@PathVariable("petId") Long petId,
                                                                   @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<PostImage> images = postImageRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE * 3);
        CursorPageData<PostImageDto> res = CursorPageData
                .of(postAssembler.convertPostImageDtos(images.getResult()), images.isHasMore(),
                        images.getPageSize(), images.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("participators/{participatorId}/followers")
    public Response<CursorPageData<ParticipatorDto>> getParticipatorFollowers(@PathVariable("participatorId") Long participatorId,
                                                                      @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Participator> participators = followParticipatorRepository
                .findFollower(participatorId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<ParticipatorDto> res = CursorPageData
                .of(participtorAssembler.convertToDtos(participators.getResult()), participators.isHasMore(),
                        participators.getPageSize(), participators.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("blockeds")
    public Response<CursorPageData<ParticipatorDto>> getMyBlockedUsers(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<Participator> participators = blockRecordRepository
                .findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<ParticipatorDto> res = CursorPageData
                .of(participtorAssembler.convertToDtos(participators.getResult()), participators.isHasMore(),
                        participators.getPageSize(), participators.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("participators/{participatorId}/followeds")
    public Response<CursorPageData<ParticipatorDto>> getParticipatorFolloweds(@PathVariable("participatorId") Long participatorId,
                                                                              @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Participator> participators = followParticipatorRepository
                .findFollowed(participatorId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<ParticipatorDto> res = CursorPageData
                .of(participtorAssembler.convertToDtos(participators.getResult()), participators.isHasMore(),
                        participators.getPageSize(), participators.getNextCursor());
        return Response.ok(res);
    }


    @PostMapping("participators/{participatorId}/block")
    public Response<BlockResult> blockUser(@PathVariable("participatorId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        BlockRecord blockRecord = postApplicationService.block(userId, participatorId);
        BlockResult res = BlockResult.builder()
                .targetId(participatorId)
                .blocked(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("participators/{participatorId}/block")
    public Response<BlockResult> unblockUser(@PathVariable("participatorId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        BlockRecord blockRecord = postApplicationService.removeBlock(userId, participatorId);
        BlockResult res = BlockResult.builder()
                .targetId(participatorId)
                .blocked(false)
                .build();
        return Response.ok(res);
    }
}
