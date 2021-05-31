package com.petfabula.infrastructure.fileserver.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@Configuration
public class FtpTemplateConfiguration {

    @Autowired
    private FtpProps ftpProps;

    @Bean
    DefaultFtpSessionFactory defaultFtpSessionFactory() {
        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
        defaultFtpSessionFactory.setPassword(ftpProps.getPassword());
        defaultFtpSessionFactory.setUsername(ftpProps.getUsername());
        defaultFtpSessionFactory.setHost(ftpProps.getHost());
        defaultFtpSessionFactory.setPort(ftpProps.getPort());
        // set passive mode
        defaultFtpSessionFactory.setClientMode(2);
        // set binary trans type
        defaultFtpSessionFactory.setFileType(2);

        return defaultFtpSessionFactory;
    }

    @Bean
    FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
        return new FtpRemoteFileTemplate(dsf);
    }
}