package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.aggregate.community.annotation.RestrictedAction;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.question.QuestionMessageKeys;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.entity.AnswerCommentReply;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentReplyRepository;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentRepository;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerCommentService {

    @Autowired
    private QuestionIdGenerator idGenerator;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private AnswerCommentRepository answerCommentRepository;

//    @Autowired
//    private AnswerCommentReplyRepository answerCommentReplyRepository;

    @RestrictedAction
    public AnswerComment createAnswerComment(Long participatorId, Long answerId, Long replyTo, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            throw new InvalidOperationException(QuestionMessageKeys.ANSWER_NOT_FOUND);
        }

        AnswerComment answerComment =
                new AnswerComment(idGenerator.nextId(), participator, answerId,
                        answer.getQuestionId(), replyTo, content);
        answer.setCommentCount(answer.getCommentCount() + 1);
        answerRepository.save(answer);

        return answerCommentRepository.save(answerComment);
    }

    public AnswerComment removeAnswerComment(Long participatorId, Long commentId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        AnswerComment answerComment = answerCommentRepository.findById(commentId);
        if (answerComment == null) {
            return null;
        }

        if (!answerComment.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Answer answer = answerRepository.findById(answerComment.getAnswerId());
        if (answer == null) {
            throw new InvalidOperationException(QuestionMessageKeys.ANSWER_NOT_FOUND);
        }

        answer.setCommentCount(answer.getCommentCount() - 1);
        answerRepository.save(answer);

        answerCommentRepository.remove(answerComment);
        return answerComment;
    }

//    public AnswerCommentReply createCommentReply(Long participatorId, Long commentId, Long replyToId, String content) {
//        AnswerComment answerComment = answerCommentRepository.findById(commentId);
//        if (answerComment == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        Participator participator = participatorRepository.findById(participatorId);
//        if (participator == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        Answer answer = answerRepository.findById(answerComment.getAnswerId());
//        if (answer == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        if (replyToId != null) {
//            AnswerCommentReply replyTo = answerCommentReplyRepository.findById(replyToId);
//            if (replyTo != null) {
//                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//            }
//        }
//
//        answerComment.setReplyCount(answerComment.getReplyCount() + 1);
//        answer.setCommentCount(answer.getCommentCount() + 1);
//        answerCommentRepository.save(answerComment);
//        answerRepository.save(answer);
//
//        AnswerCommentReply commentReply =
//                new AnswerCommentReply(idGenerator.nextId(), participator, answer.getId(),
//                        commentId, replyToId, content);
//        return answerCommentReplyRepository.save(commentReply);
//    }
//
//    public AnswerCommentReply removeReply(Long participatorId, Long commentReplyId) {
//        AnswerCommentReply commentReply = answerCommentReplyRepository.findById(commentReplyId);
//        if (commentReply == null) {
//            return null;
//        }
//        Participator participator = participatorRepository.findById(participatorId);
//        if (participator == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        Answer answer = answerRepository.findById(commentReply.getAnswerId());
//        if (answer == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        AnswerComment answerComment = answerCommentRepository.findById(commentReply.getCommentId());
//        if (answerComment == null) {
//            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
//        }
//
//        answer.setCommentCount(answer.getCommentCount() - 1);
//        answerComment.setReplyCount(answerComment.getReplyCount() - 1);
//
//        answerRepository.save(answer);
//        answerCommentRepository.save(answerComment);
//        answerCommentReplyRepository.remove(commentReply);
//
//        return commentReply;
//    }

}
