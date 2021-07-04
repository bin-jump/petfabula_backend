package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerCreated;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.QuestionImage;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.common.image.ImageDimension;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionIdGenerator idGenerator;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Question create(Long participatorId, String title,
                           String content, List<ImageFile> images) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long questionId = idGenerator.nextId();
        Question question = new Question(questionId, participator, title, content);

        participator.setQuestionCount(participator.getQuestionCount() + 1);
        participatorRepository.save(participator);

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            QuestionImage postImage =
                    new QuestionImage(idGenerator.nextId(), path, questionId, dimension.getWidth(), dimension.getHeight());
            question.addImage(postImage);
        }

        Question saveQuestion = questionRepository.save(question);

        domainEventPublisher.publish(new QuestionAnswerCreated(saveQuestion));
        return saveQuestion;
    }

    public Question update(Long participatorId, Long questionId,
                           String title, String content) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (!question.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        question.setTitle(title);
        question.setContent(content);

        return questionRepository.save(question);
    }

    public Question remove(Long participatorId, Long questionId) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            return null;
        }

        Participator participator = question.getParticipator();
        if (!participator.getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        participator.setQuestionCount(participator.getQuestionCount() - 1);
        participatorRepository.save(participator);

        questionRepository.remove(question);
        return question;
    }

}
