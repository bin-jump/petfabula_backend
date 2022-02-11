package com.petfabula.presentation.web.controller;

import com.petfabula.application.document.DocumentApplicationService;
import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class HtmlDocumentController {

    @Autowired
    private DocumentApplicationService documentApplicationService;

    @GetMapping("privacy-policy.html")
    public String getPrivacyPolicy(Model model) {
        ApplicationDocument document = documentApplicationService.getPrivacyAgreementDocument();
        String content = document.getContent();

        List<String> res = Arrays.asList(content.split("\n"));
        model.addAttribute("content", res);
        model.addAttribute("title", "Privacy Policy");
        return "document";
    }
}
