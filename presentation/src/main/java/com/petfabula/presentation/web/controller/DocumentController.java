package com.petfabula.presentation.web.controller;

import com.petfabula.application.document.DocumentApplicationService;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.presentation.facade.assembler.document.ApplicationDocumentAssembler;
import com.petfabula.presentation.facade.dto.administration.ReportDto;
import com.petfabula.presentation.facade.dto.administration.UpdateReportStatusRequest;
import com.petfabula.presentation.facade.dto.document.ApplicationDocumentDto;
import com.petfabula.presentation.web.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/document")
@Validated
public class DocumentController {

    @Autowired
    private ApplicationDocumentAssembler documentAssembler;

    @Autowired
    private DocumentApplicationService documentApplicationService;

    @GetMapping("user-agreement")
    public Response<ApplicationDocumentDto> getUserAgreement() {
        ApplicationDocument document = documentApplicationService.getUserAgreementDocument();

        ApplicationDocumentDto documentDto = documentAssembler.convertToDto(document);
        return Response.ok(documentDto);
    }

    @GetMapping("privacy-agreement")
    public Response<ApplicationDocumentDto> gePrivacyAgreement() {
        ApplicationDocument document = documentApplicationService.getPrivacyAgreementDocument();

        ApplicationDocumentDto documentDto = documentAssembler.convertToDto(document);
        return Response.ok(documentDto);
    }
}
