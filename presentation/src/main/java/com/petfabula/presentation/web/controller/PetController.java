package com.petfabula.presentation.web.controller;

import com.petfabula.application.pet.PetApplicationService;
import com.petfabula.domain.aggregate.pet.entity.*;
import com.petfabula.domain.aggregate.pet.respository.*;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.assembler.pet.*;
import com.petfabula.presentation.facade.dto.pet.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pet")
@Validated
public class PetController {

    static final int DEAULT_PAGE_SIZE = 5;

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

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    @Autowired
    private FeedRecordRepository feedRecordRepository;

    @Autowired
    private DisorderRecordRepository disorderRecordRepository;

    @Autowired
    private PetEventRecordRepository petEventRecordRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping("pets")
    public Response<List<PetDetailDto>> getMyPets() {
        Long userId = LoginUtils.currentUserId();
        List<Pet> pets = petRepository.findByFeederId(userId);
        List<PetDetailDto> res = petAssembler.convertToDetailDtos(pets);
        List<Long> breedIds = pets.stream().map(Pet::getBreedId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, PetBreed> breedMap = petBreedRepository.findByIds(breedIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        res.stream().forEach(item -> item.setBreed(breedMap.get(item.getBreedId()).getName()));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}")
    public Response<PetDetailDto> getPet(@PathVariable("petId") Long petId) {
        Pet pet = petRepository.findById(petId);
        PetBreed breed = petBreedRepository.findById(pet.getBreedId());
        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        res.setBreed(breed.getName());
        return Response.ok(res);
    }

    @GetMapping("feeders/{feederId}/pets")
    public Response<List<PetDto>> getFeederPets(@PathVariable("feederId") Long feederId) {
        List<Pet> pets = petRepository.findByFeederId(feederId);
        List<PetDto> res = petAssembler.convertToDtos(pets);
        List<Long> breedIds = pets.stream().map(Pet::getBreedId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, PetBreed> breedMap = petBreedRepository.findByIds(breedIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        res.stream().forEach(item -> item.setBreed(breedMap.get(item.getBreedId()).getName()));
        return Response.ok(res);
    }

    @GetMapping("breeds")
    public Response<List<PetBreedDto>> getPetBreeds() {
        List<PetBreed> petBreeds = petBreedRepository.findAll();
        List<PetBreedDto> res = new ArrayList<>();
        petBreeds.forEach(item -> {
            PetBreedDto breedDto = PetBreedDto.builder()
                    .id(item.getId())
                    .category(item.getCategory())
                    .categoryId(item.getCategoryId())
                    .name(item.getName())
                    .build();
            res.add(breedDto);
        });
        return Response.ok(res);
    }

    @PostMapping("pets")
    public Response<PetDetailDto> createPet(@RequestPart(name = "pet") @Validated PetDetailDto petDto,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long userId = LoginUtils.currentUserId();
        Pet pet = petApplicationService.createPet(userId,
                petDto.getName(), AssemblerHelper.toLocalDate(petDto.getBirthday()),
                AssemblerHelper.toLocalDate(petDto.getArrivalDay()),
                petDto.getGender(), petDto.getWeight(), petDto.getBreedId(), petDto.getBio());
        PetBreed breed = petBreedRepository.findById(pet.getBreedId());
        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        res.setBreed(breed.getName());
        return Response.ok(res);
    }

    @PostMapping("feedrecords")
    public Response<FeedRecordDto> createFeedRecord(@Validated @RequestBody FeedRecordDto feedRecordDto) {
        Long userId = LoginUtils.currentUserId();
        FeedRecord record = petApplicationService.createFeedRecord(userId, feedRecordDto.getPetId(),
                AssemblerHelper.toInstant(feedRecordDto.getDateTime()),
                feedRecordDto.getFoodContent(), feedRecordDto.getAmount(), feedRecordDto.getNote());
        feedRecordDto = feedRecordAssember.convertToDto(record);
        return Response.ok(feedRecordDto);
    }

    @PostMapping("weightrecords")
    public Response<WeightRecordDto> createWeightRecord(@Validated @RequestBody WeightRecordDto weightRecordDto) {
        Long userId = LoginUtils.currentUserId();
        WeightRecord record = petApplicationService.createWeightRecord(userId, weightRecordDto.getPetId(),
                AssemblerHelper.toInstant(weightRecordDto.getDateTime()), weightRecordDto.getWeight());
        weightRecordDto = weightRecordAssembler.convertToDto(record);
        return Response.ok(weightRecordDto);
    }

    @PostMapping("/disorderrecords")
    public Response<DisorderRecordDto> createDisorderRecord(@RequestPart(name = "record") @Validated DisorderRecordDto disorderRecordDto,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        DisorderRecord disorderRecord = petApplicationService
                .createDisorderRecord(userId, disorderRecordDto.getPetId(),
                        AssemblerHelper.toInstant(disorderRecordDto.getDateTime()),
                        disorderRecordDto.getDisorderType(), disorderRecordDto.getContent(), imageFiles);
        disorderRecordDto = disorderRecordAssembler.convertToDto(disorderRecord);
        return Response.ok(disorderRecordDto);
    }

    @PostMapping("/peteventrecords")
    public Response<PetEventRecordDto> createPetEventRecord(@RequestPart(name = "record") @Validated PetEventRecordDto recordDto,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        PetEventRecord record = petApplicationService
                .createPetEventRecord(userId, recordDto.getPetId(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()),
                        recordDto.getEventType(), recordDto.getContent(), imageFiles);
        recordDto = petEventRecordAssembler.convertToDto(record);
        return Response.ok(recordDto);
    }

    @PostMapping("/medicalrecords")
    public Response<MedicalRecordDto> createMedicalRecord(@RequestPart(name = "record") @Validated MedicalRecordDto recordDto,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        MedicalRecord record = petApplicationService
                .createMedicalRecord(userId, recordDto.getPetId(), recordDto.getHospitalName(),
                        recordDto.getSymptom(), recordDto.getDiagnosis(), recordDto.getTreatment(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()),
                        recordDto.getNote(), imageFiles);
        recordDto = medicalRecordAssembler.convertToDto(record);
        return Response.ok(recordDto);
    }

    @GetMapping("pets/{petId}/feedrecords")
    public Response<CursorPageData<FeedRecordDto>> getFeedRecords(@PathVariable("petId") Long petId,
                                                                      @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<FeedRecord> records = feedRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<FeedRecordDto> res = CursorPageData
                .of(feedRecordAssember.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/weightrecords")
    public Response<CursorPageData<WeightRecordDto>> getWeightRecords(@PathVariable("petId") Long petId,
                                                    @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<WeightRecord> records = weightRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<WeightRecordDto> res = CursorPageData
                .of(weightRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/disorderrecords")
    public Response<CursorPageData<DisorderRecordDto>> getDisorderRecords(@PathVariable("petId") Long petId,
                                                                      @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<DisorderRecord> records = disorderRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<DisorderRecordDto> res = CursorPageData
                .of(disorderRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/peteventrecords")
    public Response<CursorPageData<PetEventRecordDto>> getPetEventRecords(@PathVariable("petId") Long petId,
                                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<PetEventRecord> records = petEventRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PetEventRecordDto> res = CursorPageData
                .of(petEventRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/medicalrecords")
    public Response<CursorPageData<MedicalRecordDto>> getMedicalRecords(@PathVariable("petId") Long petId,
                                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<MedicalRecord> records = medicalRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<MedicalRecordDto> res = CursorPageData
                .of(medicalRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());
        return Response.ok(res);
    }
}
