package com.citizenv.app.service.impl;


import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.*;
import com.citizenv.app.payload.custom.CustomAddress;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import com.citizenv.app.payload.excel.ExcelCitizen;
import com.citizenv.app.repository.*;
import com.citizenv.app.service.CitizenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CitizenServiceImpl implements CitizenService {
    @Autowired
    private ModelMapper mapper;

    Logger logger = LogManager.getRootLogger();

//    @Autowired
    private final CitizenRepository repo;
//    @Autowired
    private final DistrictRepository districtRepo;
//    @Autowired
    private final ProvinceRepository provinceRepo;
//    @Autowired
    private final WardRepository wardRepo;
//    @Autowired
    private final HamletRepository hamletRepo;
//    @Autowired
    private final AddressRepository addressRepo;
//    @Autowired
    private final EthnicityRepository ethnicityRepo;
//    @Autowired
    private final ReligionRepository religionRepo;
//    @Autowired
    private final AssociationRepository associationRepo;
//    @Autowired
    private final AddressTypeRepository addressTypeRepo;

    private final AssociationTypeRepository associationTypeRepo;

    public CitizenServiceImpl(CitizenRepository repo, DistrictRepository districtRepo, ProvinceRepository provinceRepo
            , WardRepository wardRepo, HamletRepository hamletRepo, AddressRepository addressRepo
            , EthnicityRepository ethnicityRepo, ReligionRepository religionRepo, AssociationRepository associationRepo
            , AddressTypeRepository addressTypeRepo, AssociationTypeRepository associationTypeRepo) {
        this.repo = repo;
        this.districtRepo = districtRepo;
        this.provinceRepo = provinceRepo;
        this.wardRepo = wardRepo;
        this.hamletRepo = hamletRepo;
        this.addressRepo = addressRepo;
        this.ethnicityRepo = ethnicityRepo;
        this.religionRepo = religionRepo;
        this.associationRepo = associationRepo;
        this.addressTypeRepo = addressTypeRepo;
        this.associationTypeRepo = associationTypeRepo;
    }

    public List<CitizenDto> getAll() {
        List<Citizen> entities = repo.findAll();
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAll(int page) {
        Page<Citizen> citizenEntities = repo.findAll(PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizenEntities);
    }
//    public List<CitizenCustom> getAll() {
//        List<Citizen> entities = repo.findAll();
//        List<CitizenCustom> list = new ArrayList<>();
//        for (Citizen c: entities) {
//            list.add(mapping(c));
//        }
//        return list;
//    }

    public CitizenDto getById(String citizenId) {
        Citizen citizen = repo.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerID", citizenId));
        return mapper.map(citizen, CitizenDto.class);
    }

    @Override
    public CitizenDto getByNationalId(String nationalId) {
        Citizen citizen = repo.findByNationalId(nationalId).orElseThrow(
                () -> new ResourceNotFoundException("Citizen", "NationalId", nationalId));
        return mapper.map(citizen, CitizenDto.class);
    }

    @Override
    public List<CitizenDto> getAllByHamletCode(String hamletCode) {
        Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );
//        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, "1");
        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAllByHamletCode(String hamletCode, int page) {
        Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );
        Page<Citizen> citizensPage = repo.findAllByHamletCode(hamletCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Override
    public Map<String, Object> getAllByDistrictCode(String districtCode, int page) {
        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        Page<Citizen> citizensPage = repo.findAllByDistrictCode(districtCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Override
    public Map<String, Object> getAllByProvinceCode(String provinceCode, int page) {
        Province foundProvince = provinceRepo.findByCode(provinceCode).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );
        Page<Citizen> citizensPage = repo.findAllByProvinceCode(provinceCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Transactional
    @Override
    public CitizenDto createCitizen(CustomCitizenRequest citizen) {
        String nId = citizen.getNationalId();
        repo.findByNationalId(nId).ifPresent(
                citizen1 -> {
                    throw new ResourceFoundException("Citizen", "NationalId", nId);
                }
        );

        Citizen c = validate(citizen);
        Citizen newCitizen = mapper.map(c, Citizen.class);
        List<Address> addresses = new ArrayList<>();
        Address hometown = new Address();
        Address permanentAddress = new Address();
        Address temporaryAddress = new Address();
        for (CustomAddress ad : citizen.getAddresses()) {
            Integer adTypeId = ad.getAddressType();
            String hamletCode = ad.getHamletCode();
            AddressType foundAddressType = addressTypeRepo.findById(adTypeId).orElseThrow(
                    () -> new ResourceNotFoundException("AddressType", "AddressTypeId","" + adTypeId)
            );
            Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                    () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
            );
            if (adTypeId == 1) {
                hometown.setParam(c, foundHamlet, foundAddressType);
            } else if (adTypeId == 2) {
                permanentAddress.setParam(c, foundHamlet, foundAddressType);
            } else if (adTypeId == 3) {
                temporaryAddress.setParam(c, foundHamlet, foundAddressType);
            }
        }
        if (citizen.getAddresses().size() == 2) {
            AddressType foundAddressType = addressTypeRepo.findById(3).orElseThrow(
                    () -> new ResourceNotFoundException("AddressType", "AddressTypeId","" + 3)
            );
            temporaryAddress.setCitizen(c);
            temporaryAddress.setAddressType(foundAddressType);
        }
        addresses.add(hometown);
        addresses.add(permanentAddress);
        addresses.add(temporaryAddress);
        newCitizen.setAddresses(addresses);

        List<Association> associations = new ArrayList<>();
        if (citizen.getAssociations() != null) {
            for (AssociationDto as : citizen.getAssociations()) {
                Association association = mapper.map(as, Association.class);
                association.setCitizen(newCitizen);
                associations.add(association);
            }
            newCitizen.setAssociations(associations);
        }
        Citizen createCitizen = repo.save(newCitizen);
        return mapper.map(createCitizen, CitizenDto.class);
    }

    @Transactional
    @Override
    public CitizenDto updateCitizen(String nationalId, CustomCitizenRequest citizen) {
        Citizen foundCitizen = repo.findByNationalId(nationalId).orElseThrow(
                () -> new ResourceNotFoundException("Citizen", "NationalId", nationalId)
        );
        if (!nationalId.equals(citizen.getNationalId())) {
            repo.findByNationalId(citizen.getNationalId()).ifPresent(citizen1 -> {
                throw new ResourceFoundException("Citizen", "NationalId", citizen.getNationalId());
            });
        }
        Citizen c = validate(citizen);
        c.setId(foundCitizen.getId());

        //update address
//        List<Address> addresses = new ArrayList<>();
        for(Address ad: foundCitizen.getAddresses()) {
            for (CustomAddress cad: citizen.getAddresses()) {
                if (ad.getAddressType().getId().equals(cad.getAddressType())) {
                    if ((ad.getAddressType().getId() != 3) && (!ad.getHamlet().getCode().equals(cad.getHamletCode()))) {
                        Hamlet foundHamlet = hamletRepo.findByCode(cad.getHamletCode()).orElseThrow(
                                () -> new ResourceNotFoundException("Hamlet", "HamletCode", cad.getHamletCode())
                        );
                       ad.setHamlet(foundHamlet);
                    }
                    if (ad.getAddressType().getId() == 3) {
                        if (cad.getHamletCode() != null) {
                            Hamlet foundHamlet = hamletRepo.findByCode(cad.getHamletCode()).orElseThrow(
                                    () -> new ResourceNotFoundException("Hamlet", "HamletCode", cad.getHamletCode())
                            );
                            ad.setHamlet(foundHamlet);
                        } else {
                            ad.setHamlet(null);
                        }
                    }
                }
            }
        }

        //update Association
        List<Association> associations = new ArrayList<>();
        for (AssociationDto as: citizen.getAssociations()) {
//            Association association = new Association();
//            AssociationType associationType = mapper.map(as.getAssociationType(), AssociationType.class);
//            association.setId(as.getId());
//            association.setAssociatedCitizenNationalId(as.getAssociatedCitizenNationalId());
//            association.setAssociatedCitizenName(as.getAssociatedCitizenName());
//            association.setAssociationType(associationType);
//            associations.add(association);
            Association association = mapper.map(as, Association.class);
            associations.add(association);

        }
        List<Integer> associationsOfOriginal = foundCitizen.getAssociations().stream().map(Association::getId).collect(Collectors.toList());
        List<Integer> associationsOfNew = associations.stream().map(Association::getId).collect(Collectors.toList());
        associationsOfOriginal.removeAll(associationsOfNew);
        if (associationsOfOriginal.size() > 0) {
            Iterator<Association> iterator = foundCitizen.getAssociations().iterator();
            while (iterator.hasNext()) {
                Association as = iterator.next();
                if (associationsOfOriginal.contains(as.getId())) {
//                    as.setCitizen(null);
                    iterator.remove();
//                    associationRepo.deleteById(as.getId());
                }
            }
        }
//        List<Association> associationList = foundCitizen.getAssociations();
//        associationList.removeIf(as -> associationsOfOriginal.contains(as.getId()));
        associationRepo.deleteAllById(associationsOfOriginal);

        foundCitizen.setNationalId(c.getNationalId());
        foundCitizen.setName(c.getName());
        foundCitizen.setDateOfBirth(c.getDateOfBirth());
        foundCitizen.setBloodType(c.getBloodType());
        foundCitizen.setSex(c.getSex());
        foundCitizen.setMaritalStatus(c.getMaritalStatus());
        foundCitizen.setOtherNationality(c.getOtherNationality());
        foundCitizen.setEthnicity(c.getEthnicity());
        foundCitizen.setReligion(c.getReligion());
        for (Association as: associations) {
            if (as.getId() != null) {
                for (Association ass : foundCitizen.getAssociations()) {
                    if (as.getId().equals(ass.getId())) {
                        ass.setAssociatedCitizenNationalId(as.getAssociatedCitizenNationalId());
                        ass.setAssociatedCitizenName(as.getAssociatedCitizenName());
                        ass.setAssociationType(as.getAssociationType());
                    }
                }
            } else {
                as.setCitizen(foundCitizen);
                foundCitizen.getAssociations().add(as);
            }
        }
        return mapper.map(foundCitizen, CitizenDto.class);

    }

    private Map<String, Object> getStringObjectMap(int page, Page<Citizen> citizensPage) {
        List<CitizenDto> list = citizensPage.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
        Map<String, Object> res = new HashMap<>();
        res.put("totalPages", citizensPage.getTotalPages());
        res.put("totalElements", citizensPage.getTotalElements());
        res.put("page", page);
        res.put("pageSize", citizensPage.getNumberOfElements());
        res.put("citizens", list);
        return res;
    }

    @Override
    public List<CitizenDto> getAllByWardCode(String wardCode) {
        Ward foundWard = wardRepo.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        List<Citizen> entities = repo.findAllByWardCode(wardCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAllByWardCode(String wardCode, int page) {
        Ward foundWard = wardRepo.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        Page<Citizen> citizensPage = repo.findAllByWardCode(wardCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Override
    public List<CitizenDto> getAllByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        List<Citizen> entities = repo.findAllByDistrictCode(districtCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepo.findByCode(provinceCode).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );
        List<Citizen> entities = repo.findAllByProvinceCode(provinceCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByAddressId(String addressId) {
        return null;
    }

    @Override
    public CitizenDto createCitizen(CitizenDto citizen) {
        String citizenId = citizen.getNationalId();
        repo.findById(citizenId).ifPresent(
                c -> {throw new ResourceFoundException("Citizen", "CititzenId", citizenId);
        });
       if (validateInfo(citizen)) {
           Citizen newCitizen = mapper.map(citizen, Citizen.class);
           newCitizen.setName(Utils.standardizeName(citizen.getName()));
           newCitizen.getAddresses().forEach(address -> address.setCitizen(newCitizen));
           newCitizen.getAssociations().forEach(association -> association.setCitizen(newCitizen));
           Citizen createCitizen = repo.save(newCitizen);
//           addressRepo.saveAll(newCitizen.getAddresses());
//           associationRepo.saveAll(newCitizen.getAssociations());
           return mapper.map(createCitizen, CitizenDto.class);
      }
       return null;
    }


    @Override
    public CitizenDto updateCitizen(String citizenId, CitizenDto citizen) {
        Citizen foundCitizen = repo.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Citizen", "CitizenId", citizenId)
        );
        String cIdUpdate = citizen.getNationalId();

        if (validateInfo(citizen)) {
            if (!cIdUpdate.equals(citizenId)) {
                repo.findById(cIdUpdate).ifPresent(
                        citizen1 -> {throw new ResourceFoundException("Citizen", "CitizenId", cIdUpdate);});
                Citizen newCitizen = mapper.map(citizen, Citizen.class);
                newCitizen.setName(Utils.standardizeName(citizen.getName()));
                newCitizen.getAddresses().forEach(address -> address.setCitizen(newCitizen));
                newCitizen.getAssociations().forEach(association -> association.setCitizen(newCitizen));
                Citizen createCitizen = repo.save(newCitizen);
                repo.delete(foundCitizen);
                return mapper.map(createCitizen, CitizenDto.class);
            } else {
                List<Integer> addressIdListOfOriginalCitizen = foundCitizen.getAddresses().stream().map(Address::getId).collect(Collectors.toList());
                List<Integer> associationIdListOfOriginalCitizen = foundCitizen.getAssociations().stream().map(Association::getId).collect(Collectors.toList());
                List<Integer> addressIdListOfNewCitizen = citizen.getAddresses().stream().map(AddressDto::getId).collect(Collectors.toList());
                List<Integer> associationIdListOfNewCitizen = citizen.getAssociations().stream().map(AssociationDto::getId).collect(Collectors.toList());
                addressIdListOfOriginalCitizen.removeAll(addressIdListOfNewCitizen);
                foundCitizen.getAddresses().forEach(address -> address.setCitizen(foundCitizen));
                foundCitizen.getAssociations().forEach(association -> association.setCitizen(foundCitizen));
                repo.save(foundCitizen);
                if (addressIdListOfOriginalCitizen.size() > 0) {
                    addressIdListOfOriginalCitizen.forEach(id -> {
                        addressRepo.deleteById(id);
                    });
                }
                associationIdListOfOriginalCitizen.removeAll(associationIdListOfNewCitizen);
                if (associationIdListOfOriginalCitizen.size() > 0) {
                    associationIdListOfOriginalCitizen.forEach(a -> {
                        associationRepo.deleteById(a);
                    });
                }
                Citizen updateCititzen = mapper.map(citizen, Citizen.class);
                updateCititzen.getAddresses().forEach(address -> address.setCitizen(updateCititzen));
                updateCititzen.getAssociations().forEach(association -> association.setCitizen(updateCititzen));
                repo.save(updateCititzen);
                return mapper.map(updateCititzen, CitizenDto.class);
            }
        }
        return null;
    }

    @Override
    public void deleteCitizen(String citizenId) {
        Citizen foundCitizen = repo.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Citizen", "CitizenId", citizenId)
        );
        repo.delete(foundCitizen);
    }




    private boolean validateInfo(CitizenDto citizen) {
        String citizenId = citizen.getNationalId();
        String provinceCodeOfQueQuan = null;

        //Kiểm tra mã địa chỉ - address
        int countRequiredAddress = 0;
        for (AddressDto a: citizen.getAddresses()) {
            if (a.getAddressType().getId() == 1 || a.getAddressType().getId() == 2) {
                countRequiredAddress ++;
            }
            Hamlet h = hamletRepo.findByCode(a.getHamlet().getCode()).orElseThrow(
                    () -> new ResourceNotFoundException("Hamlet", "HamletCode", a.getHamlet().getCode())
            );
            if (a.getAddressType().getId() == 1) {
                provinceCodeOfQueQuan = h.getWard().getDistrict().getProvince().getCode();
            }
        }
        if (countRequiredAddress != 2) {
            throw new InvalidException("Chua nhap du cac truong dia chi bat buoc");
        }
        // Kiểm tra số định danh- id
//        if (!Utils.validateNationalId(citizenId, citizen.getSex(), provinceCodeOfQueQuan, citizen.getDateOfBirth())) {
//            throw new InvalidException("Ma so dinh danh khong hop le");
//        }
        //Kiểm tra định dạng tên tiếng Việt hợp lệ - name
//        String name = citizen.getName();
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Dinh dang ten khong hop le");
//        }

        //Kiểm tra giới tính hợp lệ - sex
        String sex = citizen.getSex();
        if (!Utils.validateSex(sex)) {
            throw new InvalidException("Gioi tinh khong hop le");
        }

        //Kiểm tra nhóm máu - bloodType
        String blood = citizen.getBloodType();
        try {
            Utils.BloodType.valueOf(blood);
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Nhom mau khong hop le");
        }

        //Kiểm tra ngày sinh hợp lệ - dateOfBirth
        LocalDate dob = citizen.getDateOfBirth();
        if (dob.isAfter(LocalDate.now())) {
            throw new InvalidException("Ngay thang nam sinh khong hop le");
        }

        //Kiểm tra dân tộc - ethnicity
        if (citizen.getEthnicity() != null) {
            Integer ethnicityId = citizen.getEthnicity().getId();
            Ethnicity foundEthnicity = ethnicityRepo.findById(ethnicityId).orElseThrow(
                    () -> new ResourceNotFoundException("Ethnicity", "EthnicityId", "" + ethnicityId)
            );
        }

        //Kiểm tra tôn giáo - religion
        if (citizen.getReligion() != null) {
            Integer religionId = citizen.getReligion().getId();
            Religion foundReligion = religionRepo.findById(religionId).orElseThrow(
                    () -> new ResourceNotFoundException("Religion", "ReligionId","" + religionId)
            );
        }

        return true;
    }

    private Citizen validate(CustomCitizenRequest citizenRequest) {
        String nationalId = citizenRequest.getNationalId();
        String name = citizenRequest.getName();
        LocalDate dateOfBirth = citizenRequest.getDateOfBirth();
        String bloodType = citizenRequest.getBloodType();
        String sex = citizenRequest.getSex();
        String maritalStatus = citizenRequest.getMaritalStatus();
        Integer ethnicityId = citizenRequest.getEthnicityId();
        String otherNationality = citizenRequest.getOtherNationality();
        ReligionDto religion = citizenRequest.getReligion();
        String provinceCodeOfQueQuan = null;
        //Kiểm tra mã địa chỉ - address
        boolean checkHometown = false; //check có quê quán không
        boolean checkPermanentAddress = false; // check có địa chỉ thường trú không
        List<Address> addressesList = new ArrayList<>();
        for (CustomAddress a: citizenRequest.getAddresses()) {
            if (a.getAddressType() == 1) {
                checkHometown = true;
                if (a.getHamletCode() == null || a.getProvinceCode() == null) {
                    throw new InvalidException("Truong que quan phai khac null");
                }
                provinceCodeOfQueQuan = a.getProvinceCode();
            } else if (a.getAddressType() == 2) {
                checkPermanentAddress = true;
                if (a.getHamletCode() == null || a.getProvinceCode() == null) {
                    throw new InvalidException("Truong dia chi thuong tru phai khac null");
                }
            }
        }
        if (!checkHometown || !checkPermanentAddress) {
            throw new InvalidException("Chua nhap du cac truong dia chi bat buoc");
        }

        // Kiểm tra số định danh- id
//        if (!Utils.validateNationalId(nationalId,sex, provinceCodeOfQueQuan, dateOfBirth)) {
//            throw new InvalidException("Ma so dinh danh khong hop le");
//        }
        //Kiểm tra định dạng tên tiếng Việt hợp lệ - name
//        String name = citizen.getName();
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Dinh dang ten khong hop le");
//        }

        //Kiểm tra giới tính hợp lệ - sex
        if (!Utils.validateSex(sex)) {
            throw new InvalidException("Gioi tinh khong hop le");
        }

        //Kiểm tra nhóm máu - bloodType
        try {
            Utils.BloodType.valueOf(bloodType);
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Nhom mau khong hop le");
        }

        //Kiểm tra ngày sinh hợp lệ - dateOfBirth
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidException("Ngay thang nam sinh khong hop le");
        }
        Ethnicity foundEthnicity = null;
        if (ethnicityId != null) {
            foundEthnicity = ethnicityRepo.findById(ethnicityId).orElseThrow(
                    () -> new ResourceNotFoundException("Ethnicity", "EthnicityId", "" + ethnicityId)
            );
        }

        Religion foundReligion = null;
        if (religion != null) {
            foundReligion = religionRepo.findById(religion.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Religion", "ReligionId","" + religion.getId())
            );
        }
        Citizen c = new Citizen();
        c.setNationalId(nationalId);
        c.setName(Utils.standardizeName(name));
        c.setDateOfBirth(dateOfBirth);
        c.setBloodType(bloodType);
        c.setSex(sex);
        c.setMaritalStatus(maritalStatus);
        c.setOtherNationality(otherNationality);
        c.setEthnicity(foundEthnicity);
        c.setReligion(foundReligion);
        return c;
    }

    @Override
    public List<UserDto> createUserFromExcelFile(File excelFile) {
        try {
            FileInputStream file = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 2;
            int endRow = sheet.getLastRowNum();
            int startCol = 0;
            int endCol = 21;
            List<AddressType> addressTypes = addressTypeRepo.findAll();
            List<AssociationType> associationTypes = associationTypeRepo.findAll();
            List<Citizen> citizens = new ArrayList<>();
            StringBuilder RowInvalid = new StringBuilder();
            String provinceCodeOfHometown = null;
            List<ExcelCitizen> excelModels = new ArrayList<>();
            for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
                Citizen citizen = new Citizen();
                ExcelCitizen model = new ExcelCitizen();
                List<Address> addresses = new ArrayList<>();
                List<Association> associations = new ArrayList<>();
                Row row = sheet.getRow(rowNum);
                Association association = new Association();

                for (int colNum = startCol; colNum <= endCol; colNum++) {
                    Cell cell = row.getCell(colNum);
                    switch (colNum + 1) {
                        case 1: model.setNationalId(cell.getStringCellValue());break;
                        case 2:
                            model.setName(cell.getStringCellValue());
//                            if (!Utils.validateName(model.getName())) {
//                                listRowInvalid.append(rowNum);
//                                break;
//                            }
                            citizen.setName(Utils.standardizeName(cell.getStringCellValue()));
                            break;
                        case 3:
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                            model.setDateOfBirth(LocalDate.parse(cell.getStringCellValue()));
                            citizen.setDateOfBirth(LocalDate.parse(cell.getStringCellValue()));
                            break;
                        case 4:
                            model.setSex(cell.getStringCellValue());
                            if (!Utils.validateSex(model.getSex())) {
                                RowInvalid.append(rowNum);
                                throw new InvalidException("Invalid in row: " + rowNum + " and column: " + (colNum + 1));
                            } else {
                                citizen.setSex(cell.getStringCellValue());
                            }
                            break;
                        case 5:
                            model.setBloodType(cell.getStringCellValue());
                            Object value = model.getSex();
                            if (!(value instanceof Utils.BloodType)) {
                                RowInvalid.append(rowNum);
                                throw new InvalidException("Invalid in row: " + rowNum + " and column: " + (colNum + 1));
                            }
                            break;
                        case 6:
                            model.setMaritalStatus(cell.getStringCellValue());
                            break;
                        case 7:
                            model.setEthnicity(cell.getStringCellValue());
                            int finalRowNum = rowNum;
                            int finalColNum = colNum;
                            Ethnicity ethnicity = ethnicityRepo.findByName(model.getName()).orElseThrow(
                                    () -> new InvalidException("Invalid in row: " + finalRowNum + " and column: " + (finalColNum + 1))
                            );
                            citizen.setEthnicity(ethnicity);
                            break;
                        case 8:
                            model.setReligion(cell.getStringCellValue());
                            if (!cell.getStringCellValue().equals("")) {
                                finalRowNum = rowNum;
                                finalColNum = colNum;
                                Religion religion = religionRepo.findByName(cell.getStringCellValue()).orElseThrow(
                                        () -> new InvalidException("Invalid in row: " + finalRowNum + " and column: " + (finalColNum + 1))
                                );
                                citizen.setReligion(religion);
                            }
                            break;
                        case 9:
                            model.setOtherNationality(cell.getStringCellValue());
                            if (!cell.getStringCellValue().equals("")) {
                                citizen.setOtherNationality(cell.getStringCellValue());
                            }
                            break;
                        case 10:
                            model.setEducationalLevel(cell.getStringCellValue());
                            citizen.setEducationalLevel(cell.getStringCellValue());
                            break;
                        case 11:
                            model.setJob(cell.getStringCellValue());
                            citizen.setJob(cell.getStringCellValue());
                            break;
                        case 12: case 13: case 14:
                            String adr = cell.getStringCellValue();
                            if (colNum != 13 && adr.equals("")) {
                                throw new InvalidException("Invalid in row: " + rowNum + " and column: " + (colNum + 1));
                            }
                            if (!adr.equals("")) {
                                String[] listNameOfAdr = adr.split("-");
                                if (listNameOfAdr.length != 4) {
                                    throw new InvalidException("Invalid in row: " + rowNum + " and column: " + (colNum + 1));
                                }
                                List<Hamlet> hamlet = hamletRepo.findHamletFromExcel(listNameOfAdr[3], listNameOfAdr[2],
                                        listNameOfAdr[1], listNameOfAdr[0]);
                                if (hamlet != null) {
                                    if (colNum == 11) {
                                        provinceCodeOfHometown = hamlet.get(0).getCode().substring(0, 2);
                                    }
                                    Address add = new Address();
                                    add.setHamlet(hamlet.get(0));
                                    add.setAddressType(addressTypes.get(colNum-11));
                                    addresses.add(add);
                                }
                            }
                            if (colNum == 13 && adr.equals("")) {
                                Address add = new Address();
//                                AddressType addressType = addressTypeRepo.findById(3).orElse(null);
                                add.setAddressType(addressTypes.get(2));
                                addresses.add(add);
                            }
                            // Lấy ra associatedCitizenNationalId
                        case 15: case 17: case 19: case 21:
                            if (!cell.getStringCellValue().equals("")) {
                                association.setAssociatedCitizenNationalId(cell.getStringCellValue());
                            }break;
                            //lấy ra associatedCitizenName
                        case 16: case 18: case 20: case 22:
                            if (!cell.getStringCellValue().equals("")) {
                                association.setAssociatedCitizenName(cell.getStringCellValue());
                                association.setAssociationType(associationTypes.get(colNum - 15));
                            }
                            if (association.getAssociatedCitizenName()!= null && association.getAssociatedCitizenNationalId()!= null) {
                                associations.add(association);
                            }
                            association.setAssociatedCitizenName(null);
                            association.setAssociatedCitizenNationalId(null);
                            association.setAssociationType(null);
                            break;
                    }
                    System.out.print(cell.getStringCellValue()+" ");
                }
                excelModels.add(model);
                System.out.println();
                if (!Utils.validateNationalId(citizen.getNationalId(),citizen.getSex(),provinceCodeOfHometown, citizen.getDateOfBirth())) {
                    throw new InvalidException("Invalid identifier at row " + rowNum);
                }
                Citizen foundCitizen = repo.findByNationalId(citizen.getNationalId()).orElse(null);
                if (foundCitizen == null) {
                    citizens.add(citizen);
                }
            }
            for (ExcelCitizen model: excelModels) {
                System.out.println(model.toString());
            }

            return null;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}