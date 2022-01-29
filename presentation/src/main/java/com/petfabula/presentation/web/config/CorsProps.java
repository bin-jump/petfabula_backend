package com.petfabula.presentation.web.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix="app.cors")
@Data
@NoArgsConstructor
@Validated
public class CorsProps {

//    private String allowedOrigin;
    private List<String> allowedOrigins;
}
