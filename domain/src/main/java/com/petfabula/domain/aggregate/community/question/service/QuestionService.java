package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerCreated;
import com.petfabula.domain.aggregate.community.question.QuestionCreated;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    static int IMAGE_LIMIT = 6;

    @Autowired
    private QuestionIdGenerator idGenerator;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Question create(Long participatorId, Long relatePetId, String title,
                           String content, List<ImageFile> images) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        ParticipatorPet participatorPet = null;
        if (relatePetId != null) {
            participatorPet = participatorPetRepository.findById(relatePetId);
            if (participatorPet == null || !participatorPet.getParticipatorId().equals(participatorId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        }

        Long questionId = idGenerator.nextId();
        Question question = new Question(questionId, participator, title, content, participatorPet);

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
        domainEventPublisher.publish(new QuestionCreated(saveQuestion));
        return saveQuestion;
    }

    public Question update(Long participatorId, Long questionId, String title, String content,
                           Long relatePetId, List<ImageFile> images, List<Long> imageIds) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!question.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (relatePetId != null) {
            ParticipatorPet participatorPet = participatorPetRepository.findById(relatePetId);
            if (participatorPet == null || !participatorPet.getParticipatorId().equals(participatorId)) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
            question.setRelatePet(participatorPet);
        }

        question.setTitle(title);
        question.setContent(content);

        List<Long> removeImageIds = new ArrayList<>();
        for (QuestionImage image : question.getImages()) {
            if (!imageIds.contains(image.getId())) {
                removeImageIds.add(image.getId());
            }
        }
        for (Long id : removeImageIds) {
            question.removeImage(id);
        }

        if ((images.size() + question.getImages().size()) > IMAGE_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        List<String> imagePathes = imageRepository.save(images);
        for (int i = 0; i < imagePathes.size(); i++) {
            String path = imagePathes.get(i);
            ImageDimension dimension = images.get(i).getDimension();
            QuestionImage postImage =
                    new QuestionImage(idGenerator.nextId(), path, questionId, dimension.getWidth(), dimension.getHeight());
            question.addImage(postImage);
        }

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
