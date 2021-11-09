package com.petfabula.presentation.web.controller;

import com.petfabula.application.pet.PetApplicationService;
import com.petfabula.domain.aggregate.pet.entity.*;
import com.petfabula.domain.aggregate.pet.respository.*;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.assembler.pet.*;
import com.petfabula.presentation.facade.dto.AlreadyDeletedResponse;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.pet.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

        res.stream().forEach(item -> item.setBreed(petAssembler
                .convertToPetBreedDto(breedMap.get(item.getBreedId()))));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}")
    public Response<PetDetailDto> getPetDetail(@PathVariable("petId") Long petId) {
        Pet pet = petRepository.findById(petId);
        PetBreed breed = petBreedRepository.findById(pet.getBreedId());
        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        PetBreedDto breedDto = petAssembler.convertToPetBreedDto(breed);
        res.setBreed(breedDto);
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
        List<PetBreedDto> res = petAssembler.convertToPetBreedDtos(petBreeds);
        return Response.ok(res);
    }

    @PostMapping("pets")
    public Response<PetDetailDto> createPet(@RequestPart(name = "pet") @Validated PetDetailDto petDto,
                                            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Long userId = LoginUtils.currentUserId();
        ImageFile imageFile = null;
        if (image != null) {
            imageFile = new ImageFile(image.getOriginalFilename(),
                    image.getInputStream(), image.getSize());
        }
        Pet pet = petApplicationService.createPet(userId,
                petDto.getName(), AssemblerHelper.toLocalDate(petDto.getBirthday()),
                AssemblerHelper.toLocalDate(petDto.getArrivalDay()),
                petDto.getGender(), petDto.getWeight(), petDto.getBreedId(), petDto.getBio(), imageFile);
        PetBreed breed = petBreedRepository.findById(pet.getBreedId());
        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        res.setBreed(petAssembler.convertToPetBreedDto(breed));
        return Response.ok(res);
    }

    @PutMapping("pets")
    public Response<PetDetailDto> updatePet(@RequestPart(name = "pet") @Validated PetDetailDto petDto,
                                            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Long userId = LoginUtils.currentUserId();
        ImageFile imageFile = null;
        if (image != null) {
            imageFile = new ImageFile(image.getOriginalFilename(),
                    image.getInputStream(), image.getSize());
        }
        Pet pet = petApplicationService.updatePet(userId, petDto.getId(),
                petDto.getName(), AssemblerHelper.toLocalDate(petDto.getBirthday()),
                AssemblerHelper.toLocalDate(petDto.getArrivalDay()),
                petDto.getGender(), petDto.getWeight(), petDto.getBreedId(), petDto.getBio(), imageFile);
        PetBreed breed = petBreedRepository.findById(pet.getBreedId());
        PetDetailDto res = petAssembler.convertToDetailDto(pet);
        res.setBreed(petAssembler.convertToPetBreedDto(breed));
        return Response.ok(res);
    }

    @DeleteMapping("pets/{petId}")
    public Response<Object> removePet(@PathVariable("petId")Long petId) {
        Long userId = LoginUtils.currentUserId();
        Pet pet = petApplicationService.removePet(userId, petId);
        if (pet == null) {
            return Response.ok(AlreadyDeletedResponse.of(petId));
        }
        PetDto petDto = petAssembler.convertToDto(pet);
        return Response.ok(petDto);
    }

    @PostMapping("feedrecords")
    public Response<FeedRecordDto> createFeedRecord(@Validated @RequestBody FeedRecordDto feedRecordDto) {
        Long userId = LoginUtils.currentUserId();
        FeedRecord record = petApplicationService.createFeedRecord(userId, feedRecordDto.getPetId(),
                AssemblerHelper.toInstant(feedRecordDto.getDateTime()),
                feedRecordDto.getFoodContent(), feedRecordDto.getAmount(), feedRecordDto.getNote());
        feedRecordDto = feedRecordAssember.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        feedRecordDto.setPet(petDto);
        return Response.ok(feedRecordDto);
    }

    @PostMapping("weightrecords")
    public Response<WeightRecordDto> createWeightRecord(@Validated @RequestBody WeightRecordDto weightRecordDto) {
        Long userId = LoginUtils.currentUserId();
        WeightRecord record = petApplicationService.createWeightRecord(userId, weightRecordDto.getPetId(),
                AssemblerHelper.toInstant(weightRecordDto.getDateTime()), weightRecordDto.getWeight());
        weightRecordDto = weightRecordAssembler.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        weightRecordDto.setPet(petDto);
        return Response.ok(weightRecordDto);
    }

    @PostMapping("disorderrecords")
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
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(disorderRecordDto.getPetId()));
        disorderRecordDto.setPet(petDto);
        return Response.ok(disorderRecordDto);
    }

    @PostMapping("peteventrecords")
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
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        recordDto.setPet(petDto);
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
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        recordDto.setPet(petDto);
        return Response.ok(recordDto);
    }

    @GetMapping("pets/{petId}/feedrecords")
    public Response<CursorPageData<FeedRecordDto>> getFeedRecords(@PathVariable("petId") Long petId,
                                                                      @RequestParam(value = "cursor", required = false) Long cursor) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }
        CursorPage<FeedRecord> records = feedRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<FeedRecordDto> res = CursorPageData
                .of(feedRecordAssember.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());

        res.getResult().stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/recent-feed-records")
    public Response<List<FeedRecordDto>> getRecentFeedRecords(@PathVariable("petId") Long petId) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }

        Date d = new Date();
        Instant today = DateUtils.truncate(d, Calendar.DATE).toInstant();
        Instant start = today.minus(7, ChronoUnit.DAYS);

        List<FeedRecord> records = feedRecordRepository.findByPetIdAndAfter(petId, start, 200);
        List<FeedRecordDto> res = feedRecordAssember.convertToDtos(records);

        res.stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/weightrecords")
    public Response<CursorPageData<WeightRecordDto>> getWeightRecords(@PathVariable("petId") Long petId,
                                                    @RequestParam(value = "cursor", required = false) Long cursor) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }
        CursorPage<WeightRecord> records = weightRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<WeightRecordDto> res = CursorPageData
                .of(weightRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());

        res.getResult().stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/disorderrecords")
    public Response<CursorPageData<DisorderRecordDto>> getDisorderRecords(@PathVariable("petId") Long petId,
                                                                      @RequestParam(value = "cursor", required = false) Long cursor) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }
        CursorPage<DisorderRecord> records = disorderRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<DisorderRecordDto> res = CursorPageData
                .of(disorderRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());

        res.getResult().stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/peteventrecords")
    public Response<CursorPageData<PetEventRecordDto>> getPetEventRecords(@PathVariable("petId") Long petId,
                                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }
        CursorPage<PetEventRecord> records = petEventRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PetEventRecordDto> res = CursorPageData
                .of(petEventRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());

        res.getResult().stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @GetMapping("pets/{petId}/medicalrecords")
    public Response<CursorPageData<MedicalRecordDto>> getMedicalRecords(@PathVariable("petId") Long petId,
                                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new NotFoundException(petId, CommonMessageKeys.PET_NOT_FOUND);
        }
        CursorPage<MedicalRecord> records = medicalRecordRepository.findByPetId(petId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<MedicalRecordDto> res = CursorPageData
                .of(medicalRecordAssembler.convertToDtos(records.getResult()), records.isHasMore(),
                        records.getPageSize(), records.getNextCursor());

        res.getResult().stream().forEach(item -> item.setPet(petAssembler.convertToDto(pet)));
        return Response.ok(res);
    }

    @PutMapping("disorderrecords")
    public Response<DisorderRecordDto> updateDisorderRecord(@RequestPart(name = "record") @Validated DisorderRecordDto disorderRecordDto,
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

        List<Long> imageIds = disorderRecordDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        DisorderRecord disorderRecord = petApplicationService
                .updateDisorderRecord(userId, disorderRecordDto.getPetId(),  disorderRecordDto.getId(),
                        AssemblerHelper.toInstant(disorderRecordDto.getDateTime()),
                        disorderRecordDto.getDisorderType(), disorderRecordDto.getContent(), imageFiles, imageIds);
        disorderRecordDto = disorderRecordAssembler.convertToDto(disorderRecord);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(disorderRecordDto.getPetId()));
        disorderRecordDto.setPet(petDto);
        return Response.ok(disorderRecordDto);
    }

    @PutMapping("feedrecords")
    public Response<FeedRecordDto> updateFeedRecord(@RequestBody @Validated FeedRecordDto recordDto) {
        Long userId = LoginUtils.currentUserId();
        FeedRecord record = petApplicationService
                .updateFeedRecord(userId, recordDto.getPetId(),  recordDto.getId(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()),
                        recordDto.getFoodContent(), recordDto.getAmount(), recordDto.getNote());
        recordDto = feedRecordAssember.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(recordDto.getPetId()));
        recordDto.setPet(petDto);
        return Response.ok(recordDto);
    }

    @PutMapping("/medicalrecords")
    public Response<MedicalRecordDto> updateMedicalRecord(@RequestPart(name = "record") @Validated MedicalRecordDto recordDto,
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

        List<Long> imageIds = recordDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        MedicalRecord record = petApplicationService
                .updateMedicalRecord(userId, recordDto.getPetId(), recordDto.getId(), recordDto.getHospitalName(),
                        recordDto.getSymptom(), recordDto.getDiagnosis(), recordDto.getTreatment(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()),
                        recordDto.getNote(), imageFiles, imageIds);
        recordDto = medicalRecordAssembler.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        recordDto.setPet(petDto);
        return Response.ok(recordDto);
    }

    @PutMapping("peteventrecords")
    public Response<PetEventRecordDto> updatePetEventRecord(@RequestPart(name = "record") @Validated PetEventRecordDto recordDto,
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

        List<Long> imageIds = recordDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        PetEventRecord record = petApplicationService
                .updatePetEventRecord(userId, recordDto.getPetId(), recordDto.getId(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()), recordDto.getEventType(),
                        recordDto.getContent(), imageFiles, imageIds);
        recordDto = petEventRecordAssembler.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        recordDto.setPet(petDto);
        return Response.ok(recordDto);
    }

    @PutMapping("weightrecords")
    public Response<WeightRecordDto> updateWeightRecord(@Validated @RequestBody WeightRecordDto recordDto) {
        Long userId = LoginUtils.currentUserId();

        WeightRecord record = petApplicationService
                .updateWeightRecord(userId, recordDto.getPetId(), recordDto.getId(),
                        AssemblerHelper.toInstant(recordDto.getDateTime()), recordDto.getWeight());
        recordDto = weightRecordAssembler.convertToDto(record);
        PetDto petDto = petAssembler.convertToDto(petRepository.findById(record.getPetId()));
        recordDto.setPet(petDto);
        return Response.ok(recordDto);
    }

    @DeleteMapping("disorderrecords/{recordId}")
    public Response<Object> removeDisorderRecord(@PathVariable("recordId") Long recordId) {
        Long userId = LoginUtils.currentUserId();
        DisorderRecord record = petApplicationService.removeDisorderRecord(userId, recordId);
        if (record == null) {
            return Response.ok(AlreadyDeletedResponse.of(recordId));
        }
        DisorderRecordDto res = disorderRecordAssembler.convertToDto(record);
        return Response.ok(res);
    }

    @DeleteMapping("feedrecords/{recordId}")
    public Response<Object> removeFeedRecord(@PathVariable("recordId") Long recordId) {
        Long userId = LoginUtils.currentUserId();
        FeedRecord record = petApplicationService.removeFeedRecord(userId, recordId);
        if (record == null) {
            return Response.ok(AlreadyDeletedResponse.of(recordId));
        }
        FeedRecordDto res = feedRecordAssember.convertToDto(record);
        return Response.ok(res);
    }

    @DeleteMapping("medicalrecords/{recordId}")
    public Response<Object> removeMedicalRecord(@PathVariable("recordId") Long recordId) {
        Long userId = LoginUtils.currentUserId();
        MedicalRecord record = petApplicationService.removeMedicalRecord(userId, recordId);
        if (record == null) {
            return Response.ok(AlreadyDeletedResponse.of(recordId));
        }
        MedicalRecordDto res = medicalRecordAssembler.convertToDto(record);
        return Response.ok(res);
    }

    @DeleteMapping("peteventrecords/{recordId}")
    public Response<Object> removePetEventRecord(@PathVariable("recordId") Long recordId) {
        Long userId = LoginUtils.currentUserId();
        PetEventRecord record = petApplicationService.removePetEventRecord(userId, recordId);
        if (record == null) {
            return Response.ok(AlreadyDeletedResponse.of(recordId));
        }
        PetEventRecordDto res = petEventRecordAssembler.convertToDto(record);
        return Response.ok(res);
    }

    @DeleteMapping("weightrecords/{recordId}")
    public Response<Object> removeWeightRecord(@PathVariable("recordId") Long recordId) {
        Long userId = LoginUtils.currentUserId();
        WeightRecord record = petApplicationService.removeWeightRecord(userId, recordId);
        if (record == null) {
            return Response.ok(AlreadyDeletedResponse.of(recordId));
        }
        WeightRecordDto res = weightRecordAssembler.convertToDto(record);
        return Response.ok(res);
    }
}
