package com.petfabula.presentation.web.controller;

import com.petfabula.application.administration.AdministrationApplicationService;
import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.presentation.facade.assembler.administration.FeedbackAssembler;
import com.petfabula.presentation.facade.dto.administration.CreateReportRequest;
import com.petfabula.presentation.facade.dto.administration.FeedbackDto;
import com.petfabula.presentation.facade.dto.administration.ReportResult;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/feedback")
@Validated
public class FeedbackController {

    @Autowired
    private FeedbackAssembler feedbackAssembler;

    @Autowired
    private AdministrationApplicationService administrationApplicationService;

    @PostMapping("report")
    public Response<ReportResult> createReport(@RequestBody @Validated CreateReportRequest request) {
        Long userId = LoginUtils.currentUserId();
        Report report = administrationApplicationService
                .createReport(userId, request.getReason(), request.getEntityType(), request.getEntityId());
        ReportResult res = ReportResult.builder()
                .entityId(report.getEntityId())
                .entityType(report.getEntityType())
                .build();

        return Response.ok(res);
    }

    @PostMapping("feedback")
    public Response<FeedbackDto> createFeedback(@RequestBody @Validated FeedbackDto feedbackDto) {
        Long userId = LoginUtils.currentUserId();
        Feedback feedback = administrationApplicationService
                .createFeedback(userId, feedbackDto.getContent());

        feedbackDto = feedbackAssembler.convertToDto(feedback);
        return Response.ok(feedbackDto);
    }

}
