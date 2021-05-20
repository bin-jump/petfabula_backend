package com.petfabula.presentation.web.config;

import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;

@Configuration
public class ToolConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelmapper = new ModelMapper();
        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };

        Converter<Long, LocalDate> longToLocalDate = new AbstractConverter<Long, LocalDate>() {
            @Override
            protected LocalDate convert(Long source) {
                return AssemblerHelper.toLocalDate(source);
            }
        };

        Converter<LocalDate, Long> localDateToLong = new AbstractConverter<LocalDate, Long>() {
            @Override
            protected Long convert(LocalDate source) {
                return AssemblerHelper.toLong(source);
            }
        };

        modelmapper.createTypeMap(Long.class, LocalDate.class);
        modelmapper.addConverter(longToLocalDate);
        modelmapper.getTypeMap(Long.class, LocalDate.class).setProvider(localDateProvider);

        modelmapper.createTypeMap(LocalDate.class, Long.class);
        modelmapper.addConverter(localDateToLong);

        modelmapper.typeMap(Instant.class, Long.class)
                .setConverter(ctx -> ctx.getSource().toEpochMilli());

        return modelmapper;
    }

}
