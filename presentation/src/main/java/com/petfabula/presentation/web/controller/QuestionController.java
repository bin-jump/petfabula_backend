package com.petfabula.presentation.web.controller;

import com.petfabula.application.community.QuestionApplicationService;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.question.QuestionMessageKeys;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import com.petfabula.domain.aggregate.community.question.repository.*;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.community.AnswerAssembler;
import com.petfabula.presentation.facade.assembler.community.AnswerCommentAssembler;
import com.petfabula.presentation.facade.assembler.community.ParticiptorPetAssembler;
import com.petfabula.presentation.facade.assembler.community.QuestionAssembler;
import com.petfabula.presentation.facade.dto.community.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question")
@Validated
public class QuestionController {

    static final int DEAULT_PAGE_SIZE = 5;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private AnswerCommentAssembler answerCommentAssembler;

    @Autowired
    private ParticiptorPetAssembler participtorPetAssembler;

    @Autowired
    private QuestionApplicationService questionApplicationService;

    @Autowired
    private FollowParticipatorRepository followParticipatorRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionVoteRepository questionVoteRepository;

    @Autowired
    private AnswerCommentRepository answerCommentRepository;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    @PostMapping("questions")
    public Response<QuestionDto> createQuestion(@RequestPart(name = "question") @Validated QuestionDto questionDto,
                                                @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        Question question = questionApplicationService
                .createQuestion(userId, questionDto.getRelatePetId(), questionDto.getTitle(), questionDto.getContent(), imageFiles);

        questionDto = questionAssembler.convertToDto(question);
        return Response.ok(questionDto);
    }

    @PostMapping("answers")
    public Response<AnswerDto> createAnswer(@RequestPart(name = "answer") @Validated AnswerDto answerDto,
                                                @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        Answer answer = questionApplicationService
                .createAnswer(userId, answerDto.getQuestionId(), answerDto.getContent(), imageFiles);

        answerDto = answerAssembler.convertToDto(answer);
        return Response.ok(answerDto);
    }

    @GetMapping("recommend/questions")
    public Response<CursorPageData<QuestionDto>> getRecommandQuestions(@RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Question> questions = questionRepository.findRecent(cursor, DEAULT_PAGE_SIZE);
        CursorPageData<QuestionDto> res = CursorPageData
                .of(questionAssembler.convertToDtos(questions.getResult()), questions.isHasMore(),
                        questions.getPageSize(), questions.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("unanswered-questions")
    public Response<CursorPageData<QuestionDto>> getUnansweredQuestions(@RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Question> questions = questionRepository.findUnanswered(cursor, DEAULT_PAGE_SIZE);
        CursorPageData<QuestionDto> res = CursorPageData
                .of(questionAssembler.convertToDtos(questions.getResult()), questions.isHasMore(),
                        questions.getPageSize(), questions.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("questions/{questionId}")
    public Response<QuestionDto> getQuestionDetail(@PathVariable("questionId") Long questionId) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new NotFoundException(QuestionMessageKeys.QUESTION_NOT_FOUND);
        }
        QuestionDto res = questionAssembler.convertToDto(question);
        Long userId = LoginUtils.currentUserId();
        if (userId != null) {
            UpvoteQuestion upvoteQuestion = questionVoteRepository.find(userId, questionId);
            res.setUpvoted(upvoteQuestion != null);
            if (!userId.equals(question.getParticipator().getId())) {
                FollowParticipator followParticipator = followParticipatorRepository
                        .find(userId, question.getParticipator().getId());
                res.getParticipator().setFollowed(followParticipator != null);
            }
        }

        if (question.getRelatePetId() != null) {
            res.setRelatePet(participtorPetAssembler
                    .convertToDto(participatorPetRepository.findById(question.getRelatePetId())));
        }

        return Response.ok(res);
    }

    @GetMapping("questions/{questionId}/answers")
    public Response<CursorPageData<AnswerDto>> getQuestionAnswers(@PathVariable("questionId") Long questionId,
                                                                  @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Answer> answers = answerRepository.findByQuestionId(questionId, cursor, DEAULT_PAGE_SIZE);
        List<AnswerDto> answerDtos = answerAssembler.convertToDtos(answers.getResult());
        Long userId = LoginUtils.currentUserId();
        if (userId != null) {
            List<Long> answerIds = answers.getResult()
                    .stream().map(Answer::getId)
                    .collect(Collectors.toList());
            List<UpvoteAnswer> upvoteAnswers = answerVoteRepository
                    .findByParticipatorIdVoted(userId, answerIds);
            Set<Long> upvotedIds = upvoteAnswers.stream()
                    .map(UpvoteAnswer::getAnswerId)
                    .collect(Collectors.toSet());

            answerDtos.stream().forEach(item -> {
                item.setUpvoted(upvotedIds.contains(item.getId()));
            });
        }
        CursorPageData<AnswerDto> res = CursorPageData
                .of(answerDtos, answers.isHasMore(),
                        answers.getPageSize(), answers.getNextCursor());
        return Response.ok(res);
    }

    @PostMapping("answers/comments")
    public Response<AnswerCommentDto> createAnswerComment(@RequestBody @Validated AnswerCommentDto answerCommentDto) {
        Long userId = LoginUtils.currentUserId();
        AnswerComment answerComment = questionApplicationService
                .createAnswerComment(userId, answerCommentDto.getAnswerId(),
                        answerCommentDto.getReplyTo(), answerCommentDto.getContent());
        answerCommentDto = answerCommentAssembler.convertToDto(answerComment);
        return Response.ok(answerCommentDto);
    }

    @GetMapping("answers/{answerId}/comments")
    public Response<CursorPageData<AnswerCommentDto>> getAnswerComments(@PathVariable("answerId") Long answerId,
                                                        @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<AnswerComment> answerComments =
                answerCommentRepository.findByAnswerId(answerId, cursor,DEAULT_PAGE_SIZE);
        CursorPageData<AnswerCommentDto> res = CursorPageData
                .of(answerCommentAssembler.convertToDtos(answerComments.getResult()), answerComments.isHasMore(),
                        answerComments.getPageSize(), answerComments.getNextCursor());
        return Response.ok(res);
    }

    @PostMapping("questions/{questionId}/vote")
    public Response<VoteQuestionResult> voteQuestion(@PathVariable("questionId") Long questionId) {
        Long userId = LoginUtils.currentUserId();
        questionApplicationService.upvoteQuestion(userId, questionId);
        VoteQuestionResult res = VoteQuestionResult.builder()
                .questionId(questionId)
                .upvoted(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("questions/{questionId}/vote")
    public Response<VoteQuestionResult> removeVoteQuestion(@PathVariable("questionId") Long questionId) {
        Long userId = LoginUtils.currentUserId();
        questionApplicationService.removeUpvoteQuestion(userId, questionId);
        VoteQuestionResult res = VoteQuestionResult.builder()
                .questionId(questionId)
                .upvoted(false)
                .build();
        return Response.ok(res);
    }

    @PostMapping("answers/{answerId}/vote")
    public Response<VoteAnswerResult> voteAnswer(@PathVariable("answerId") Long answerId) {
        Long userId = LoginUtils.currentUserId();
        questionApplicationService.upvoteAnswer(userId, answerId);
        VoteAnswerResult res = VoteAnswerResult.builder()
                .answerId(answerId)
                .upvoted(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("answers/{answerId}/vote")
    public Response<VoteAnswerResult> unvoteAnswer(@PathVariable("answerId") Long answerId) {
        Long userId = LoginUtils.currentUserId();
        questionApplicationService.removeUpvoteAnswer(userId, answerId);
        VoteAnswerResult res = VoteAnswerResult.builder()
                .answerId(answerId)
                .upvoted(false)
                .build();
        return Response.ok(res);
    }

}
