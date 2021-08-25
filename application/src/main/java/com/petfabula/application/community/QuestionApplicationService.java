package com.petfabula.application.community;

import com.petfabula.application.event.AnswerCommentCreateEvent;
import com.petfabula.application.event.AnswerCreateEvent;
import com.petfabula.application.event.IntegratedEventPublisher;
import com.petfabula.application.event.QuestionLikedEvent;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.entity.AnswerCommentReply;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import com.petfabula.domain.aggregate.community.question.service.AnswerCommentService;
import com.petfabula.domain.aggregate.community.question.service.AnswerService;
import com.petfabula.domain.aggregate.community.question.service.QuestionService;
import com.petfabula.domain.aggregate.community.question.service.VoteService;
import com.petfabula.domain.common.image.ImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionApplicationService {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerCommentService answerCommentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private IntegratedEventPublisher eventPublisher;

    @Transactional
    public Question createQuestion(Long participatorId, Long relatedPetId, String title,
                                   String content, List<ImageFile> images) {
        return questionService.create(participatorId, relatedPetId, title, content, images);
    }

    @Transactional
    public Question updateQuestion(Long participatorId, Long questionId, String title, String content, Long relatePetId,
                                   List<ImageFile> images, List<Long> imageIds) {
        return questionService.update(participatorId, questionId, title, content, relatePetId, images, imageIds);
    }

    @Transactional
    public Question removeQuestion(Long participatorId, Long questionId) {
        return questionService.remove(participatorId, questionId);
    }

    @Transactional
    public Answer createAnswer(Long participatorId, Long questionId, String content, List<ImageFile> images) {
        Answer answer =
                answerService.create(participatorId, questionId, content, images);
        eventPublisher.publish(new AnswerCreateEvent(answer));
        return answer;
    }

    @Transactional
    public Answer updateAnswer(Long participatorId, Long answerId, String content,
                               List<ImageFile> images, List<Long> imageIds) {
        Answer answer = answerService.update(participatorId, answerId, content, images, imageIds);
        eventPublisher.publish(new AnswerCreateEvent(answer));
        return answer;
    }

    @Transactional
    public Answer removeAnswer(Long participatorId, Long answerId) {
        return answerService.remove(participatorId, answerId);
    }

    @Transactional
    public AnswerComment createAnswerComment(Long participatorId, Long answerId, Long replyTo, String content) {
        AnswerComment answerComment =
                answerCommentService.createAnswerComment(participatorId, answerId, replyTo, content);
        eventPublisher.publish(new AnswerCommentCreateEvent(answerComment));
        return answerComment;
    }

    @Transactional
    public AnswerComment removeAnswerComment(Long participatorId, Long commentId) {
        return answerCommentService.removeAnswerComment(participatorId, commentId);
    }

//    @Transactional
//    public AnswerCommentReply createAnswerCommentReply(Long participatorId, Long commentId, Long replyToId, String content) {
//        return answerCommentService.createCommentReply(participatorId, commentId, replyToId, content);
//    }

//    @Transactional
//    public AnswerCommentReply removeAnswerCommentReply(Long participatorId, Long commentReplyId) {
//        return answerCommentService.removeReply(participatorId, commentReplyId);
//    }

    @Transactional
    public UpvoteQuestion upvoteQuestion(Long participatorId, Long questionId) {
        UpvoteQuestion upvoteQuestion =  voteService.upvoteQuestion(participatorId, questionId);
        eventPublisher.publish(new QuestionLikedEvent(questionId, participatorId));
        return upvoteQuestion;
    }

    @Transactional
    public UpvoteQuestion removeUpvoteQuestion(Long participatorId, Long questionId) {
        return voteService.removeUpvoteQuestion(participatorId, questionId);
    }

    @Transactional
    public UpvoteAnswer upvoteAnswer(Long participatorId, Long answerId) {
        return voteService.upvoteAnswer(participatorId, answerId);
    }

    @Transactional
    public UpvoteAnswer removeUpvoteAnswer(Long participatorId, Long answerId) {
        return voteService.removeUpvoteAnswer(participatorId, answerId);
    }
}
