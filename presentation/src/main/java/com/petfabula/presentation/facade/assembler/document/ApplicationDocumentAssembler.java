package com.petfabula.presentation.facade.assembler.document;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.presentation.facade.dto.document.ApplicationDocumentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationDocumentAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public ApplicationDocumentDto convertToDto(ApplicationDocument document) {
        ApplicationDocumentDto documentDto = modelMapper.map(document, ApplicationDocumentDto.class);
        return documentDto;
    }
}
