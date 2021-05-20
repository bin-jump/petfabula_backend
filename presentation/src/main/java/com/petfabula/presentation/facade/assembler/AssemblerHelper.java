package com.petfabula.presentation.facade.assembler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class AssemblerHelper {

    @Value("${image.host}")
    private String imageServerHost;

    @Value("${image.protocol}")
    private String imageServerProtocol;

    public String completeImageUrl(String url) {
        if (url == null) {
            return null;
        }
        return String.format("%s%s", imagePrefix(), url);
    }

    private String imagePrefix() {
        return String.format("%s://%s", imageServerProtocol, imageServerHost);
    }


    public static LocalDate toLocalDate(Long val) {
        if (val == null) {
            return null;
        }
        // TODO: Use UTC?
        LocalDate date =
                Instant.ofEpochMilli(val)
                        .atZone(ZoneOffset.UTC).toLocalDate();
        return date;

    }

    public static Long toLong(LocalDate val) {
        if (val == null) {
            return null;
        }

        return val.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

    }
}
