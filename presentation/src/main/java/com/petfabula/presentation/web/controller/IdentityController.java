package com.petfabula.presentation.web.controller;

import com.petfabula.application.service.IdentityApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class IdentityController {

    @Autowired
    IdentityApplicationService identityApplicationService;

    @GetMapping
    public String hi() {
        return identityApplicationService.hi();
    }
}
