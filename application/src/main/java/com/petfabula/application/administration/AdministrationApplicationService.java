package com.petfabula.application.administration;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.service.FeedbackService;
import com.petfabula.domain.aggregate.administration.service.ReportService;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.post.service.PostService;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.community.question.service.AnswerService;
import com.petfabula.domain.aggregate.community.question.service.QuestionService;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.service.SystemNotificationService;
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
}
