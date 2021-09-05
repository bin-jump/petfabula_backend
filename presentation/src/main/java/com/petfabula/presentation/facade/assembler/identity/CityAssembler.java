package com.petfabula.presentation.facade.assembler.identity;

import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.presentation.facade.dto.identity.CityDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CityDto convertToDto(City city) {
        CityDto cityDto = modelMapper.map(city, CityDto.class);
        return cityDto;
    }

    public List<CityDto> convertToDtos(List<City> cities) {
        return cities.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
