package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.question.QuestionMessageKeys;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.AnswerVoteRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionVoteRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionVoteRepository questionVoteRepository;

    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Autowired
    private QuestionIdGenerator idGenerator;

    public UpvoteQuestion upvoteQuestion(Long participatorId, Long questionId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new InvalidOperationException(QuestionMessageKeys.QUESTION_NOT_FOUND);
        }

        UpvoteQuestion upvoteQuestion = questionVoteRepository
                .find(participatorId, questionId);
        if (upvoteQuestion == null) {
            upvoteQuestion = new UpvoteQuestion(idGenerator.nextId(), participator, question);
            question.setUpvoteCount(question.getUpvoteCount() + 1);
            questionRepository.save(question);
            questionVoteRepository.save(upvoteQuestion);
        }

        return upvoteQuestion;
    }

    public UpvoteQuestion removeUpvoteQuestion(Long participatorId, Long questionId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new InvalidOperationException(QuestionMessageKeys.QUESTION_NOT_FOUND);
        }

        UpvoteQuestion upvoteQuestion = questionVoteRepository
                .find(participatorId, questionId);
        if (upvoteQuestion != null) {
            question.setUpvoteCount(question.getUpvoteCount() - 1);
            questionRepository.save(question);
            questionVoteRepository.remove(upvoteQuestion);
        }

        return upvoteQuestion;
    }

    public UpvoteAnswer upvoteAnswer(Long participatorId, Long answerId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            throw new InvalidOperationException(QuestionMessageKeys.ANSWER_NOT_FOUND);
        }

        UpvoteAnswer upvoteAnswer = answerVoteRepository
                .find(participatorId, answerId);
        if (upvoteAnswer == null) {
            upvoteAnswer = new UpvoteAnswer(idGenerator.nextId(), participator, answer);
            answer.setUpvoteCount(answer.getUpvoteCount() + 1);
            answerRepository.save(answer);
            answerVoteRepository.save(upvoteAnswer);
        }

        return upvoteAnswer;
    }

    public UpvoteAnswer removeUpvoteAnswer(Long participatorId, Long answerId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            throw new InvalidOperationException(QuestionMessageKeys.ANSWER_NOT_FOUND);
        }

        UpvoteAnswer upvoteAnswer = answerVoteRepository
                .find(participatorId, answerId);
        if (upvoteAnswer != null) {
            answer.setUpvoteCount(answer.getUpvoteCount() - 1);
            answerRepository.save(answer);
            answerVoteRepository.remove(upvoteAnswer);
        }

        return upvoteAnswer;
    }
}
