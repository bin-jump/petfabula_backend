package com.petfabula.presentation.web.controller;

import com.petfabula.application.administration.AdministrationApplicationService;
import com.petfabula.application.document.DocumentApplicationService;
import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.repository.FeedbackRepository;
import com.petfabula.domain.aggregate.administration.repository.ReportRepository;
import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.domain.aggregate.community.guardian.repository.RestrictionRepository;
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
import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.entity.EmailCodeRecord;
import com.petfabula.domain.aggregate.identity.entity.Prefecture;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeRecordRepository;
import com.petfabula.domain.aggregate.identity.repository.PrefectureRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.aggregate.identity.service.RegisterService;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.respository.SystemNotificationRepository;
import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import com.petfabula.domain.aggregate.pet.respository.PetBreedRepository;
import com.petfabula.domain.aggregate.pet.respository.PetCategoryRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.administration.FeedbackAssembler;
import com.petfabula.presentation.facade.assembler.administration.ReportAssembler;
import com.petfabula.presentation.facade.assembler.administration.RestrictionAssembler;
import com.petfabula.presentation.facade.assembler.administration.UserAssembler;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.assembler.document.ApplicationDocumentAssembler;
import com.petfabula.presentation.facade.assembler.identity.CityAssembler;
import com.petfabula.presentation.facade.assembler.identity.UserAccountAssembler;
import com.petfabula.presentation.facade.assembler.notification.SystemNotificationAssembler;
import com.petfabula.presentation.facade.assembler.pet.PetAssembler;
import com.petfabula.presentation.facade.dto.AlreadyDeletedResponse;
import com.petfabula.presentation.facade.dto.administration.*;
import com.petfabula.presentation.facade.dto.community.*;
import com.petfabula.presentation.facade.dto.document.ApplicationDocumentDto;
import com.petfabula.presentation.facade.dto.identity.CityDto;
import com.petfabula.presentation.facade.dto.identity.PrefectureDto;
import com.petfabula.presentation.facade.dto.identity.UserAccountDto;
import com.petfabula.presentation.facade.dto.notification.SystemNotificationDto;
import com.petfabula.presentation.facade.dto.pet.PetBreedDto;
import com.petfabula.presentation.facade.dto.pet.PetCategoryDto;
import com.petfabula.presentation.web.api.OffsetPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private PetAssembler petAssembler;

    @Autowired
    private CityAssembler cityAssembler;

    @Autowired
    private UserAccountAssembler userAccountAssembler;

    @Autowired
    private UserAssembler userAssembler;

    @Autowired
    private RestrictionAssembler restrictionAssembler;

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
    private IdentityApplicationService identityApplicationService;

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

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PrefectureRepository prefectureRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private EmailCodeRecordRepository emailCodeRecordRepository;

    @GetMapping("test-users")
    public Response<List<StaticEmailCodeAccountDto>> getTestAllAccouts() {
        List<EmailCodeRecord> records = emailCodeRecordRepository.findAll();
        List<UserAccount> userAccounts = userAccountRepository
                .findByIds(records.stream().map(EmailCodeRecord::getId).collect(Collectors.toList()));
        Map<Long, UserAccount> userAccountMap = userAccounts
                .stream().collect(Collectors.toMap(UserAccount::getId, o -> o));

        List<StaticEmailCodeAccountDto> res = new ArrayList<>();
        records.forEach(o -> {
            if (userAccountMap.containsKey(o.getId())) {
                UserAccount account = userAccountMap.get(o.getId());
                StaticEmailCodeAccountDto dto = userAssembler.convertToDto(account, o);
                res.add(dto);
            }
        });

        return Response.ok(res);
    }

    @PostMapping("test-user")
    public Response<StaticEmailCodeAccountDto> createTestuser(@RequestBody @Validated StaticEmailCodeAccountDto request) {
        UserAccount account = identityApplicationService
                .registerByStaticEmailCode(request.getName(), request.getEmail(), request.getCode());
        EmailCodeRecord record = emailCodeRecordRepository.findByEmail(account.getEmail());

        StaticEmailCodeAccountDto res = userAssembler.convertToDto(account, record);
        return Response.ok(res);
    }

    @PutMapping("test-user-auth-info")
    public Response<Object> updateTestAuthentication(@RequestBody @Validated StaticEmailCodeAccountDto request) {
        EmailCodeRecord record = identityApplicationService
                .updateStaticEmailCodeRecord(request.getEmail(), request.getCode(), request.isActive());
        UserAccount account = userAccountRepository.findById(record.getId());
        if (account == null) {
            return Response.ok(AlreadyDeletedResponse.of(record.getId()));
        }

        StaticEmailCodeAccountDto res = userAssembler.convertToDto(account, record);
        return Response.ok(res);
    }

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

    @GetMapping("pet-categories")
    public Response<List<PetCategoryDto>> getPetCategories() {
        List<PetCategory> categories = petCategoryRepository.findAll();
        List<PetBreed> breeds = petBreedRepository.findAll();

        List<PetCategoryDto> categoryDtos = petAssembler.convertToPetCategoryDtos(categories);
        List<PetBreedDto> breedDtos = petAssembler
                .convertToPetBreedDtos(breeds);

        for(PetCategoryDto categoryDto : categoryDtos) {
            for(PetBreedDto breedDto : breedDtos) {
                if (categoryDto.getId().equals(breedDto.getCategoryId())) {
                    categoryDto.getPetBreeds().add(breedDto);
                    continue;
                }
            }
        }

        return Response.ok(categoryDtos);
    }

    @PostMapping("pet-breeds")
    public Response<PetBreedDto> createPetBreed(@RequestBody @Validated PetBreedDto breedDto) {
        PetBreed petBreed = administrationApplicationService
                .createPetBreed(breedDto.getCategoryId(), breedDto.getName());
        breedDto = petAssembler.convertToPetBreedDto(petBreed);
        return Response.ok(breedDto);
    }

    @PutMapping("pet-breeds")
    public Response<PetBreedDto> updatePetBreed(@RequestBody @Validated PetBreedDto breedDto) {
        PetBreed petBreed = administrationApplicationService
                .updatePetBreed(breedDto.getId(), breedDto.getName());
        breedDto = petAssembler.convertToPetBreedDto(petBreed);
        return Response.ok(breedDto);
    }

    @GetMapping("prefectures")
    public Response<List<PrefectureDto>> getPrefectures() {
        List<Prefecture> prefectures = prefectureRepository.findAll();
        List<City> cities = cityRepository.findAll();

        List<PrefectureDto> prefectureDtos = cityAssembler.convertToPrefectureDtos(prefectures);
        List<CityDto> cityDtos = cityAssembler
                .convertToDtos(cities);

        for(CityDto cityDto : cityDtos) {
            for(PrefectureDto prefectureDto : prefectureDtos) {
                if (prefectureDto.getId().equals(cityDto.getPrefectureId())) {
                    prefectureDto.getCities().add(cityDto);
                    continue;
                }
            }
        }

        return Response.ok(prefectureDtos);
    }

    @PostMapping("cities")
    public Response<CityDto> createCity(@RequestBody @Validated CityDto cityDto) {
        City city = administrationApplicationService
                .createCity(cityDto.getPrefectureId(), cityDto.getName());
        cityDto = cityAssembler.convertToDto(city);
        return Response.ok(cityDto);
    }

    @PutMapping("cities")
    public Response<CityDto> updateCity(@RequestBody @Validated CityDto cityDto) {
        City city = administrationApplicationService
                .updateCity(cityDto.getId(), cityDto.getName());
        cityDto = cityAssembler.convertToDto(city);
        return Response.ok(cityDto);
    }

    @GetMapping("users")
    public Response<OffsetPageData<UserAccountDto>> getUsers(@RequestParam(value = "page") Integer page,
                                                               @RequestParam(value = "size") Integer size) {
        JumpableOffsetPage<UserAccount> userPage = userAccountRepository.findAll(page, size);
        List<UserAccountDto> userDtoList = userAccountAssembler.convertToDtos(userPage.getResult());

        OffsetPageData<UserAccountDto> res = OffsetPageData.of(userDtoList, userPage.getPageIndex(),
                userPage.getPageSize(), userPage.getTotal());

        return Response.ok(res);
    }

    @GetMapping("users/{userId}")
    public Response<Object> getUserDetail(@PathVariable("userId") Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId);
        if (userAccount == null) {
            return Response.ok(AlreadyDeletedResponse.of(userId));
        }

        Restriction restriction = restrictionRepository.findByParticipatorId(userId);
        UserWithStatusDto res = userAssembler.convertToDto(userAccount, restriction);

        return Response.ok(res);
    }

    @PostMapping("users/restricts")
    public Response<RestrictionDto> createRestrict(@RequestBody @Validated RestrictionDto restrictionDto) {
        Restriction restriction = administrationApplicationService
                .createRestriction(restrictionDto.getParticipatorId(), restrictionDto.getReason());
        RestrictionDto res = restrictionAssembler.convertToDto(restriction);
        return Response.ok(res);
    }

    @DeleteMapping("users/{userId}/restricts")
    public Response<Object> removeRestrict(@PathVariable("userId") Long userId) {
        Restriction restriction = administrationApplicationService
                .removeRestrictionByParticipatorId(userId);
        if (restriction == null) {
            return Response.ok(AlreadyDeletedResponse.of(userId));
        }
        RestrictionDto res = restrictionAssembler.convertToDto(restriction);
        return Response.ok(res);
    }
}
