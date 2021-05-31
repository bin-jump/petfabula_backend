package com.petfabula.infrastructure.fileserver.ftp;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="ftp")
@Data
@NoArgsConstructor
public class FtpProps {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String imageFilePath;
}