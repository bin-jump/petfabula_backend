package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerCreated;
import com.petfabula.domain.aggregate.community.question.QuestionMessageKeys;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerImage;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.QuestionImage;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private QuestionIdGenerator idGenerator;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Answer create(Long participatorId, Long questionId, String content,
                         List<ImageFile> images) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new NotFoundException(QuestionMessageKeys.QUESTION_NOT_FOUND);
        }

        Long answerId = idGenerator.nextId();
        Answer answer = new Answer(answerId, participator, question.getId(), content);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            AnswerImage answerImage =
                    new AnswerImage(idGenerator.nextId(), path, answerId, dimension.getWidth(), dimension.getHeight());
            answer.addImage(answerImage);
        }

        question.setAnswerCount(question.getAnswerCount() + 1);
        participator.setAnswerCount(participator.getAnswerCount() + 1);
        questionRepository.save(question);
        participatorRepository.save(participator);

        domainEventPublisher.publish(new QuestionAnswerCreated(answer, question));
        return answerRepository.save(answer);
    }

    public Answer update(Long participatorId, Long answerId, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            throw new NotFoundException(CommonMessageKeys.CANNOT_PROCEED);
        }

        answer.setContent(content);
        return answerRepository.save(answer);
    }

    public Answer remove(Long participatorId, Long answerId) {
        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            return null;
        }

        Question question = questionRepository.findById(answer.getQuestionId());
        if (question == null) {
            throw new NotFoundException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (!answer.getParticipator().getId().equals(participatorId)) {
            throw new NotFoundException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        question.setAnswerCount(question.getAnswerCount() - 1);
        participator.setAnswerCount(participator.getAnswerCount() - 1);

        questionRepository.save(question);
        participatorRepository.save(participator);

        answerRepository.remove(answer);
        return answer;
    }
}
