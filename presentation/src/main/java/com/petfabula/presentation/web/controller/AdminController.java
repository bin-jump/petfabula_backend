package com.petfabula.presentation.web.controller;

import com.petfabula.application.administration.AdministrationApplicationService;
import com.petfabula.application.document.DocumentApplicationService;
import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.repository.FeedbackRepository;
import com.petfabula.domain.aggregate.administration.repository.ReportRepository;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicCategoryRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.respository.SystemNotificationRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.administration.FeedbackAssembler;
import com.petfabula.presentation.facade.assembler.administration.ReportAssembler;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.assembler.document.ApplicationDocumentAssembler;
import com.petfabula.presentation.facade.assembler.notification.SystemNotificationAssembler;
import com.petfabula.presentation.facade.dto.AlreadyDeletedResponse;
import com.petfabula.presentation.facade.dto.administration.FeedbackDto;
import com.petfabula.presentation.facade.dto.administration.ReportDto;
import com.petfabula.presentation.facade.dto.administration.UpdateReportStatusRequest;
import com.petfabula.presentation.facade.dto.community.*;
import com.petfabula.presentation.facade.dto.document.ApplicationDocumentDto;
import com.petfabula.presentation.facade.dto.notification.SystemNotificationDto;
import com.petfabula.presentation.web.api.OffsetPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@Validated
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private ReportAssembler reportAssembler;

    @Autowired
    private FeedbackAssembler feedbackAssembler;

    @Autowired
    private SystemNotificationAssembler systemNotificationAssembler;

    @Autowired
    private ParticiptorAssembler participtorAssembler;

    @Autowired
    private PostTopicAssembler postTopicAssembler;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private ApplicationDocumentAssembler documentAssembler;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AdministrationApplicationService administrationApplicationService;

    @Autowired
    private DocumentApplicationService documentApplicationService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SystemNotificationRepository systemNotificationRepository;

    @Autowired
    private PostTopicRepository postTopicRepository;

    @Autowired
    private PostTopicCategoryRepository postTopicCategoryRepository;

    @GetMapping("reports")
    public Response<OffsetPageData<ReportDto>> getReports(@RequestParam(value = "page") Integer page,
                                                          @RequestParam(value = "size") Integer size) {
        JumpableOffsetPage<Report> reportPage = reportRepository.find(page, size);
        List<ReportDto> reportDtoList = reportAssembler.convertToDtos(reportPage.getResult());

        List<Long> userIds = reportDtoList.stream().map(ReportDto::getLastReporterId)
                .collect(Collectors.toList());
        Map<Long, Participator> participatorMap = participatorRepository.findByIds(userIds)
                .stream()
                .collect(Collectors.toMap(Participator::getId, item -> item));

        reportDtoList = reportDtoList.stream()
            .map(item -> {
            Long userId = item.getLastReporterId();
            if (participatorMap.containsKey(userId)) {
                ParticipatorDto participatorDto =
                        participtorAssembler.convertToDto(participatorMap.get(userId));
                item.setLastReporter(participatorDto);
                return item;
            }
            return null;
        })
        .filter(Objects::nonNull)
                .collect(Collectors.toList());

        OffsetPageData<ReportDto> res = OffsetPageData.of(reportDtoList, reportPage.getPageIndex(),
                reportPage.getPageSize(), reportPage.getTotal());

        return Response.ok(res);
    }

    @PutMapping("report/status")
    public Response<ReportDto> updateReportStatus(@RequestBody @Validated UpdateReportStatusRequest request) {
        Report report = administrationApplicationService.updateState(request.getId(), request.getStatus());
        Participator participator = participatorRepository.findById(report.getLastReporterId());
        ReportDto res = reportAssembler.convertToDto(report);
        res.setLastReporter(participtorAssembler.convertToDto(participator));
        return Response.ok(res);
    }

    @GetMapping("feedbacks")
    public Response<OffsetPageData<FeedbackDto>> getFeedbacks(@RequestParam(value = "page") Integer page,
                                                              @RequestParam(value = "size") Integer size) {
        JumpableOffsetPage<Feedback> feedbackPage = feedbackRepository.findAll(page, size);
        List<FeedbackDto> reportDtoList = feedbackAssembler.convertToDtos(feedbackPage.getResult());

        List<Long> userIds = reportDtoList.stream().map(FeedbackDto::getReporterId)
                .collect(Collectors.toList());
        Map<Long, Participator> participatorMap = participatorRepository.findByIds(userIds)
                .stream()
                .collect(Collectors.toMap(Participator::getId, item -> item));

        reportDtoList = reportDtoList.stream()
                .map(item -> {
                    Long userId = item.getReporterId();
                    if (participatorMap.containsKey(userId)) {
                        ParticipatorDto participatorDto =
                                participtorAssembler.convertToDto(participatorMap.get(userId));
                        item.setReporter(participatorDto);
                        return item;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        OffsetPageData<FeedbackDto> res = OffsetPageData.of(reportDtoList, feedbackPage.getPageIndex(),
                feedbackPage.getPageSize(), feedbackPage.getTotal());

        return Response.ok(res);
    }

    @GetMapping("reports/{reportId}")
    public Response<ReportDto> getReportDetail(@PathVariable Long reportId) {
        Report report = reportRepository.findById(reportId);
        if (report == null) {
            throw new NotFoundException(reportId, "not found");
        }
        Participator participator = participatorRepository.findById(report.getLastReporterId());
        if (participator == null) {
            throw new NotFoundException(reportId, "not found");
        }

        ReportDto res = reportAssembler.convertToDto(report);
        res.setLastReporter(participtorAssembler.convertToDto(participator));

        Object entity = null;
        if (report.getEntityType().equals(Report.ReportType.POST.toString())) {
            Post post = postRepository.findById(report.getEntityId());
            if (post == null) {
                throw new NotFoundException(reportId, "not found");
            }
            entity = postAssembler.convertToDto(post);
        } else if (report.getEntityType().equals(Report.ReportType.QUESTION.toString())) {
            Question question = questionRepository.findById(report.getEntityId());
            if (question == null) {
                throw new NotFoundException(reportId, "not found");
            }
            entity = questionAssembler.convertToDto(question);
        } else if (report.getEntityType().equals(Report.ReportType.ANSWER.toString())) {
            Answer answer = answerRepository.findById(report.getEntityId());
            if (answer == null) {
                throw new NotFoundException(reportId, "not found");
            }
            entity = answerAssembler.convertToDto(answer);
        }

        res.setEntity(entity);
        return Response.ok(res);
    }

    @DeleteMapping("posts/{postId}")
    public Response<Object> removePost(@PathVariable Long postId) {
        Post post = administrationApplicationService.removePost(postId);
        if (post == null) {
            return Response.ok(AlreadyDeletedResponse.of(postId));
        }
        PostDto res = postAssembler.convertToDto(post);
        return Response.ok(res);
    }

    @DeleteMapping("questions/{questionId}")
    public Response<Object> removeQuestion(@PathVariable Long questionId) {
        Question question = administrationApplicationService.removeQuestion(questionId);
        if (question == null) {
            return Response.ok(AlreadyDeletedResponse.of(questionId));
        }
        QuestionDto res = questionAssembler.convertToDto(question);
        return Response.ok(res);
    }

    @DeleteMapping("answers/{answerId}")
    public Response<Object> removeAnswer(@PathVariable Long answerId) {
        Answer answer = administrationApplicationService.removeAnswer(answerId);
        if (answer == null) {
            return Response.ok(AlreadyDeletedResponse.of(answerId));
        }
        AnswerDto res = answerAssembler.convertToDto(answer);
        return Response.ok(res);
    }

    @GetMapping("system-notifications")
    public Response<OffsetPageData<SystemNotificationDto>> getSystemNotifications(@RequestParam(value = "page") Integer page,
                                                                    @RequestParam(value = "size") Integer size) {
        JumpableOffsetPage<SystemNotification> notificationPage = systemNotificationRepository
                .findAll(page, size);
        List<SystemNotificationDto> notificationDtos = systemNotificationAssembler
                .convertToDtos(notificationPage.getResult());
        OffsetPageData<SystemNotificationDto> res = OffsetPageData
                .of(notificationDtos, notificationPage.getPageIndex(),
                        notificationPage.getPageSize(), notificationPage.getTotal());
        return Response.ok(res);
    }

    @PostMapping("system-notifications")
    public Response<SystemNotificationDto> createSystemNotification(@RequestBody SystemNotificationDto notificationDto) {
        Long userId = LoginUtils.currentUserId();
        SystemNotification notification = administrationApplicationService
                .createSystemNotification(userId, notificationDto.getTitle(), notificationDto.getContent());

        notificationDto = systemNotificationAssembler.convertToDto(notification);
        return Response.ok(notificationDto);
    }

    @PutMapping("system-notifications")
    public Response<SystemNotificationDto> updateSystemNotification(@RequestBody SystemNotificationDto notificationDto) {
        SystemNotification notification = administrationApplicationService
                .updateSystemNotification(notificationDto.getId(), notificationDto.getTitle(), notificationDto.getContent());
        notificationDto = systemNotificationAssembler.convertToDto(notification);
        return Response.ok(notificationDto);
    }

    @DeleteMapping("system-notifications/{notificationId}")
    public Response<Object> removeNotification(@PathVariable("notificationId") Long notificationId) {
        SystemNotification notification = administrationApplicationService
                .removeSystemNotification(notificationId);

        if (notification == null) {
            return Response.ok(AlreadyDeletedResponse.of(notificationId));
        }
        return Response.ok(systemNotificationAssembler.convertToDto(notification));
    }

    @PutMapping("document")
    public Response<ApplicationDocumentDto> updateDocument(@RequestBody @Validated ApplicationDocumentDto documentDto) {
        ApplicationDocument document = documentApplicationService
                .updateDocument(documentDto.getDocumentKey(), documentDto.getContent());

        documentDto = documentAssembler.convertToDto(document);
        return Response.ok(documentDto);
    }

    @GetMapping("topic-categories")
    public Response<List<PostTopicCategoryDto>> getTopics() {
        List<PostTopic> topics = postTopicRepository.findAll();
        List<PostTopicCategory> categories = postTopicCategoryRepository.findAll();

        List<PostTopicDto> topicDtos = postTopicAssembler.convertToTopicDtos(topics);
        List<PostTopicCategoryDto> categoryDtos = postTopicAssembler
                .convertToTopicCategoryDtos(categories);

        for(PostTopicDto topicDto : topicDtos) {
            for(PostTopicCategoryDto categoryDto : categoryDtos) {
                if (categoryDto.getId().equals(topicDto.getTopicCategoryId())) {
                    categoryDto.getTopics().add(topicDto);
                    continue;
                }
            }
        }

        return Response.ok(categoryDtos);
    }

    @PostMapping("topics")
    public Response<PostTopicDto> createTopic(@RequestBody @Validated PostTopicDto topicDto) {
        PostTopic topic = administrationApplicationService
                .createTopic(topicDto.getTopicCategoryId(), topicDto.getTitle());
        topicDto = postTopicAssembler.convertToDto(topic);
        return Response.ok(topicDto);
    }

    @PostMapping("topic-categories")
    public Response<PostTopicCategoryDto> createTopicCategory(@RequestBody @Validated PostTopicCategoryDto categoryDto) {
        PostTopicCategory category = administrationApplicationService
                .createCategory(categoryDto.getTitle());
        categoryDto = postTopicAssembler.convertToDto(category);
        return Response.ok(categoryDto);
    }

    @PutMapping("topics")
    public Response<PostTopicDto> updateTopic(@RequestBody @Validated PostTopicDto topicDto) {
        PostTopic topic = administrationApplicationService
                .updateTopic(topicDto.getId(), topicDto.getTitle());
        topicDto = postTopicAssembler.convertToDto(topic);
        return Response.ok(topicDto);
    }

    @PutMapping("topic-categories")
    public Response<PostTopicCategoryDto> updateTopicCategory(@RequestBody @Validated PostTopicCategoryDto categoryDto) {
        PostTopicCategory category = administrationApplicationService
                .updateTopicCategory(categoryDto.getId(), categoryDto.getTitle());
        categoryDto = postTopicAssembler.convertToDto(category);
        return Response.ok(categoryDto);
    }

    @DeleteMapping("topics/{topicId}")
    public Response<Object> removeTopic(@PathVariable("topicId") Long topicId) {
        PostTopic topic = administrationApplicationService
                .removeTopic(topicId);
        if (topic == null) {
            return Response.ok(AlreadyDeletedResponse.of(topicId));
        }

        PostTopicDto topicDto = postTopicAssembler.convertToDto(topic);
        return Response.ok(topicDto);
    }

    @DeleteMapping("topic-categories/{categoryId}")
    public Response<Object> removeTopicCategory(@PathVariable("categoryId") Long categoryId) {
        PostTopicCategory category = administrationApplicationService
                .removeTopicCategory(categoryId);
        if (category == null) {
            return Response.ok(AlreadyDeletedResponse.of(categoryId));
        }

        PostTopicCategoryDto topicDto = postTopicAssembler.convertToDto(category);
        return Response.ok(topicDto);
    }
}
