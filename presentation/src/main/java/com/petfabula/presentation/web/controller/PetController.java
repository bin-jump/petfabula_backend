package com.petfabula.presentation.web.controller;

import com.petfabula.application.pet.PetApplicationService;
import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.assembler.pet.*;
import com.petfabula.presentation.facade.dto.pet.PetDetailDto;
import com.petfabula.presentation.facade.dto.pet.PetDto;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
@Validated
public class PetController {

    @Autowired
    private AssemblerHelper assemblerHelper;

    @Autowired
    private PetAssembler petAssembler;

    @Autowired
    private DisorderRecordAssembler disorderRecordAssembler;

    @Autowired
    private FeedRecordAssembler feedRecordAssember;

    @Autowired
    private MedicalRecordAssembler medicalRecordAssembler;

    @Autowired
    private PetEventRecordAssembler petEventRecordAssembler;

    @Autowired
    private WeightRecordAssembler weightRecordAssembler;

    @Autowired
    private PetApplicationService petApplicationService;

    @Autowired
    private PetRepository petRepository;

    @GetMapping("pets")
    public Response<List<PetDetailDto>> getMyPets() {
        Long userId = LoginUtils.currentUserId();
        List<Pet> pets = petRepository.findByFeederId(userId);
        List<PetDetailDto> res = petAssembler.convertToDetailDtos(pets);
        return Response.ok(res);
    }

    @GetMapping("feeders/{feederId}/pets")
    public Response<List<PetDto>> getFeederPets(@PathVariable("feederId") Long feederId) {
        Long userId = LoginUtils.currentUserId();
        List<Pet> pets = petRepository.findByFeederId(userId);
        List<PetDto> res = petAssembler.convertToDtos(pets);
        return Response.ok(res);
    }

    @PostMapping("pets")
    public Response<PetDetailDto> createPet(@RequestPart(name = "pet") @Validated PetDetailDto petDto,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long userId = LoginUtils.currentUserId();
        Pet pet = petApplicationService.createPet(userId,
                petDto.getName(), AssemblerHelper.toLocalDate(petDto.getBirthday()),
                AssemblerHelper.toLocalDate(petDto.getArrivalDay()),
                petDto.getGender(), petDto.getWeight(), petDto.getCategory(), petDto.getBreed());

        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        return Response.ok(res);
    }
}
