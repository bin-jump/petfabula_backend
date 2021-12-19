package com.petfabula.domain.aggregate.identity.service.email;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateManager {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String getFilledVerificationEmail(String code, int min) {
        Context thymeleafContext = new Context();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("code", code);
        templateModel.put("minutes", min);
        thymeleafContext.setVariables(templateModel);
        String body = templateEngine.process("verification_code_email.html", thymeleafContext);

        return body;
    }

    public String getTitleFromHtml(String content) {
        String title = Jsoup.parse(content).title();
        return title;
    }

}
