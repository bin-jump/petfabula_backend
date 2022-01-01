package com.petfabula.application.administration;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.service.FeedbackService;
import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.domain.aggregate.community.guardian.repository.RestrictionRepository;
import com.petfabula.domain.aggregate.community.guardian.service.RestrictionService;
import com.petfabula.domain.aggregate.community.post.service.PostTopicService;
import com.petfabula.domain.aggregate.administration.service.ReportService;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.post.service.PostService;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.community.question.service.AnswerService;
import com.petfabula.domain.aggregate.community.question.service.QuestionService;
import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.service.CityService;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.service.SystemNotificationService;
import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.service.PetBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdministrationApplicationService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private SystemNotificationService systemNotificationService;

    @Autowired
    private PostTopicService postTopicService;

    @Autowired
    private PetBreedService petBreedService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RestrictionService restrictionService;

    @Transactional
    public Feedback createFeedback(Long reporterId, String content) {

        return feedbackService.create(reporterId, content);
    }

    @Transactional
    public Report createReport(Long reporterId, String reason, String entityType, Long entityId) {

        return reportService.create(reporterId, reason, entityType, entityId);
    }

    @Transactional
    public Report updateState(Long reportId, Report.ReportStatus status) {

        return reportService.updateState(reportId, status);
    }

    @Transactional
    public Report updateMemo(Long reportId, String memo) {

        return reportService.updateMemo(reportId, memo);
    }

    @Transactional
    public Post removePost(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            return null;
        }
        return postService.remove(post.getParticipator().getId(), postId);
    }

    @Transactional
    public Question removeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            return null;
        }
        return questionService.remove(question.getParticipator().getId(), questionId);
    }

    @Transactional
    public Answer removeAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId);
        if (answer == null) {
            return null;
        }
        return answerService.remove(answer.getParticipator().getId(), answerId);
    }

    @Transactional
    public SystemNotification createSystemNotification(Long senderId, String title, String content) {
        return systemNotificationService.create(senderId, title, content);
    }

    @Transactional
    public SystemNotification updateSystemNotification(Long notificationId, String title, String content) {
        return systemNotificationService.update(notificationId, title, content);
    }

    @Transactional
    public SystemNotification removeSystemNotification(Long notificationId) {
        return systemNotificationService.remove(notificationId);
    }

    @Transactional
    public PostTopic createTopic(Long topicCategoryId, String title) {
        return postTopicService.createTopic(topicCategoryId, title);
    }

    @Transactional
    public PostTopicCategory createCategory(String title) {
        return postTopicService.createCategory(title);
    }

    @Transactional
    public PostTopic updateTopic(Long topicId, String title) {
        return postTopicService.updateTopic(topicId, title);
    }

    @Transactional
    public PostTopicCategory updateTopicCategory(Long topicCategoryId, String title) {
        return postTopicService.updateTopicCategory(topicCategoryId, title);
    }

    @Transactional
    public PostTopic removeTopic(Long topicId) {
        return postTopicService.removeTopic(topicId);
    }

    @Transactional
    public PostTopicCategory removeTopicCategory(Long topicCategoryId) {
        return postTopicService.removeTopicCategory(topicCategoryId);
    }

    @Transactional
    public PetBreed createPetBreed(Long categoryId, String name) {
        return petBreedService.create(categoryId, name);
    }

    @Transactional
    public PetBreed updatePetBreed(Long breedId, String name) {
        return petBreedService.update(breedId, name);
    }

    @Transactional
    public City createCity(Long prefectureId, String name) {
        return cityService.createCity(prefectureId, name);
    }

    @Transactional
    public City updateCity(Long cityId, String name) {
        return cityService.updateCity(cityId, name);
    }

    @Transactional
    public Restriction createRestriction(Long userId, String reason) {
        return restrictionService.createPermanent(userId, reason);
    }

    @Transactional
    public Restriction removeRestrictionByParticipatorId(Long userId) {
        return restrictionService.removeByParticipatorId(userId);
    }
}
