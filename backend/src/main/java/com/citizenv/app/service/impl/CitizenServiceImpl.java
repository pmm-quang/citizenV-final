package com.citizenv.app.service.impl;


import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.*;
import com.citizenv.app.payload.custom.CustomAddress;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import com.citizenv.app.payload.custom.CustomCitizenResponse;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CitizenServiceImpl implements CitizenService {

    @PersistenceContext
    private EntityManager entityManager;
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
    public CitizenDto getByNationalId(String nationalId) {
        Citizen citizen = repo.findByNationalId(nationalId).orElseThrow(
                () -> new ResourceNotFoundException("Người dân", "mã định danh", nationalId));
        return mapper.map(citizen, CitizenDto.class);
    }

    @Override
    public List<CitizenDto> getAllByHamletCode(String hamletCode) {
        Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh" , hamletCode)
        );
//        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, "1");
        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAllByHamletCode(String hamletCode, int page) {
        Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamletCode)
        );
        Long hamletId = foundHamlet.getId();
        Page<CustomCitizenResponse> citizensPage = repo.findAllByHamletCode(hamletCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Override
    public Map<String, Object> getAllByDistrictCode(String districtCode, int page) {
        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("Huyện/thị xã/thành phố", "mã định danh", districtCode)
        );
        Page<CustomCitizenResponse> citizensPage = repo.findAllByDistrictCode(districtCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Transactional
    @Override
    @CacheEvict(value = "addressesForPopulationCount", allEntries = true)
    public String createCitizen(CustomCitizenRequest citizen) {
        String nId = citizen.getNationalId();
        repo.findByNationalId(nId).ifPresent(
                citizen1 -> {
                    throw new ResourceFoundException("Nguời dân", "mã định danh", nId);
                }
        );

        Citizen c = validate(citizen);
        Citizen newCitizen = mapper.map(c, Citizen.class);
        List<Address> addresses = new ArrayList<>();
        Address hometown = new Address();
        Address permanentAddress = new Address();
        Address temporaryAddress = new Address();
        for (CustomAddress ad : citizen.getAddresses()) {
            Address address = new Address();
            Integer adTypeId = ad.getAddressType();
            String hamletCode = ad.getHamletCode();
            AddressType foundAddressType = addressTypeRepo.findById(adTypeId).orElseThrow(
                    () -> new ResourceNotFoundException("AddressType", "AddressTypeId","" + adTypeId)
            );
            Hamlet foundHamlet = hamletRepo.findByCode(hamletCode).orElseThrow(
                    () -> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamletCode)
            );
            address.setParam(c, foundHamlet, foundAddressType);
//            address.fullAddressInfo(fullAddressInfo(address));
            addresses.add(address);
        }
        if (citizen.getAddresses().size() == 2) {
            AddressType foundAddressType = addressTypeRepo.findById(3).orElseThrow(
                    () -> new ResourceNotFoundException("Loại địa chỉ cư trú", "id","" + 3)
            );
            temporaryAddress.setCitizen(c);
            temporaryAddress.setAddressType(foundAddressType);
            addresses.add(temporaryAddress);
        }
//        addresses.add(hometown);
//        addresses.add(permanentAddress);
//        addresses.add(temporaryAddress);
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
        return "Thêm thông tin người dân thành công!";
    }

    @Transactional
    @Override
    @CacheEvict(value = "addressesForPopulationCount", allEntries = true)
    public String updateCitizen(String nationalId, CustomCitizenRequest citizen) {
        Citizen foundCitizen = repo.findByNationalId(nationalId).orElseThrow(
                () -> new ResourceNotFoundException("Người dân", "mã số định danh", nationalId)
        );
        if (!nationalId.equals(citizen.getNationalId())) {
            repo.findByNationalId(citizen.getNationalId()).ifPresent(citizen1 -> {
                throw new ResourceFoundException("Người dân", "mã số định danh", citizen.getNationalId());
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
                                () -> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", cad.getHamletCode())
                        );
                       ad.setHamlet(foundHamlet);
                    }
                    if (ad.getAddressType().getId() == 3) {
                        if (cad.getHamletCode() != null) {
                            Hamlet foundHamlet = hamletRepo.findByCode(cad.getHamletCode()).orElseThrow(
                                    () -> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", cad.getHamletCode())
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
        return "Chỉnh sửa thông tin người dân thành công!";

    }

    private Map<String, Object> getStringObjectMap(int page, Page<CustomCitizenResponse> citizensPage) {
//        List<CitizenDto> list = citizensPage.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
        Map<String, Object> res = new HashMap<>();
        res.put("totalPages", citizensPage.getTotalPages());
        res.put("totalElements", citizensPage.getTotalElements());
        res.put("page", page);
        res.put("pageSize", citizensPage.getNumberOfElements());
        res.put("citizens", citizensPage);
        return res;
    }

    @Override
    public Map<String, Object> getAllByWardCode(String wardCode, int page) {
        Ward foundWard = wardRepo.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Xã/phường/thị trấn", "mã định danh", wardCode)
        );
        Page<CustomCitizenResponse> citizensPage = repo.findAllByWardCode(wardCode, 2, PageRequest.of(page - 1, 15));
        return getStringObjectMap(page, citizensPage);
    }

    @Override
    public void deleteCitizen(String citizenId) {
        Citizen foundCitizen = repo.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Người dân", "mã số định danh", citizenId)
        );
        repo.delete(foundCitizen);
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
        Integer religionId = citizenRequest.getReligionId();
        String provinceCodeOfQueQuan = null;
        //Kiểm tra mã địa chỉ - address
        boolean checkHometown = false; //check có quê quán không
        boolean checkPermanentAddress = false; // check có địa chỉ thường trú không
        List<Address> addressesList = new ArrayList<>();
        for (CustomAddress a: citizenRequest.getAddresses()) {
            if (a.getAddressType() == 1) {
                checkHometown = true;
                if (a.getHamletCode() == null || a.getProvinceCode() == null) {
                    throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                }
                provinceCodeOfQueQuan = a.getProvinceCode();
            } else if (a.getAddressType() == 2) {
                checkPermanentAddress = true;
                if (a.getHamletCode() == null || a.getProvinceCode() == null) {
                    throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                }
            }
        }
        if (!checkHometown || !checkPermanentAddress) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }

        // Kiểm tra số định danh- id
        if (!Utils.validateNationalId(nationalId,sex, provinceCodeOfQueQuan, dateOfBirth)) {
            throw new InvalidException("Mã số định danh không hợp lệ");
        }
        //Kiểm tra định dạng tên tiếng Việt hợp lệ - name
//        String name = citizen.getName();
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Dinh dang ten khong hop le");
//        }

        //Kiểm tra giới tính hợp lệ - sex
        if (!Utils.validateSex(sex)) {
            throw new InvalidException("Giới tính không hợp lệ");
        }

        //Kiểm tra nhóm máu - bloodType
        if (bloodType != null) {
            try {
                Utils.BloodType.valueOf(bloodType);
            } catch (IllegalArgumentException e) {
                throw new InvalidException("Nhóm máu không hợp lệ");
            }
        }

        //Kiểm tra ngày sinh hợp lệ - dateOfBirth
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidException("Ngày tháng năm sinh không hợp lệ");
        }
        Ethnicity foundEthnicity = null;
        if (ethnicityId != null) {
            foundEthnicity = ethnicityRepo.findById(ethnicityId).orElseThrow(
                    () -> new ResourceNotFoundException("Dân tộc", "id", String.valueOf(ethnicityId))
            );
        }

        Religion foundReligion = null;
        if (religionId != null) {
            foundReligion = religionRepo.findById(religionId).orElseThrow(
                    () -> new ResourceNotFoundException("Tôn giáo", "id", String.valueOf(religionId))
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
        c.setEducationalLevel(citizenRequest.getEducationalLevel());
        c.setJob(citizenRequest.getJob());
        c.setEthnicity(foundEthnicity);
        c.setReligion(foundReligion);
        return c;
    }

    @Override
    public List<CustomCitizenResponse> searchCitizen(CustomCitizenRequest request) {
        StringBuilder query  = new StringBuilder();
        StringBuilder joinAddresses = new StringBuilder();
        StringBuilder joinAssociation = new StringBuilder();
        StringBuilder conditionAddress = new StringBuilder();
        StringBuilder conditionAssociation = new StringBuilder();
        StringBuilder conditionCitizen = new StringBuilder();
        List<String> joinConditions = new ArrayList<>();
        query.append("select distinct new com.citizenv.app.payload.custom.CustomCitizenResponse(c.nationalId, c.name, c.sex) from Citizen c ");

        List<CustomAddress> addressConditions = request.getAddresses();
        List<AssociationDto> associationConditions = request.getAssociations();
        StringBuilder condition = new StringBuilder();
        List<String> joins = new ArrayList<>();
        List<String> conditionClauses = new ArrayList<>();
        if (addressConditions != null) {
            int index = 1;
           for (CustomAddress address:addressConditions) {
               joinAddresses.append("c.addresses ad").append(index).append(" \n");
               conditionAddress.append(" ad").append(index).append(".hamlet.code = '").append(address.getHamletCode()).append("' ")
                       .append(" and ").append("ad").append(index).append(".addressType.id = ").append(address.getAddressType())
                       .append(" \n");
               conditionClauses.add(String.valueOf(conditionAddress));
               conditionAddress.setLength(0);
               joins.add(String.valueOf(joinAddresses));
               joinAddresses.setLength(0);
               index++;
           }
        }

        if (associationConditions != null) {
            int index = 1;
            for (AssociationDto association: associationConditions) {
                joinAssociation.append("c.associations ass").append(index).append(" ");
//                conditionAssociation.append(" (");
                if (association.getAssociatedCitizenNationalId() != null) {
                    conditionAssociation.append(" ass").append(index).append(".associatedCitizenNationalId = '")
                            .append(association.getAssociatedCitizenNationalId())
                            .append("' ");
                    conditionClauses.add(String.valueOf(conditionAssociation));
                    conditionAssociation.setLength(0);
                }
                if (association.getAssociatedCitizenName() != null) {
                    conditionAssociation.append(" ass").append(index).append(".associatedCitizenName = '")
                            .append(association.getAssociatedCitizenName())
                            .append("' \n");
                    conditionClauses.add(String.valueOf(conditionAssociation));
                    conditionAssociation.setLength(0);
                }
                if (association.getAssociationType()!= null) {
                    conditionAssociation.append(" ass").append(index).append(".associationType.id = ")
                            .append(association.getAssociationType().getId())
                            .append(" \n");
                    conditionClauses.add(String.valueOf(conditionAssociation));
                    conditionAssociation.setLength(0);
                }
                joins.add(String.valueOf(joinAssociation));
                joinAssociation.setLength(0);
            }
        }
        String joinsTable = String.join(" join ",joins);
        if (joinsTable.length() > 0) {
            joinsTable = " join " + joinsTable;
            query.append(joinsTable);
        }
        if (request.getName() != null) {
            conditionCitizen.append(" c.name = '").append(request.getName()).append("' ");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("name");
        }
        if (request.getDateOfBirth() != null) {
            conditionCitizen.append(" c.dateOfBirth = '").append(request.getDateOfBirth()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("dob");
        }
        if (request.getBloodType() != null) {
            conditionCitizen.append(" c.bloodType = '").append(request.getBloodType()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("blood");
        }
        else {
            conditionCitizen.append("c.bloodType is null \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("blood null");
        }
        if (request.getSex() != null) {
            conditionCitizen.append(" c.sex = '").append(request.getSex()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("sex");
        }
        if (request.getMaritalStatus() != null) {
            conditionCitizen.append(" c.maritalStatus = '").append(request.getMaritalStatus()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("maria");
        }
        if (request.getEthnicityId() != null) {
            conditionCitizen.append(" c.ethnicity.id = ").append(request.getEthnicityId()).append(" \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("ethnicity");
        }
        if (request.getOtherNationality() != null) {
            conditionCitizen.append(" c.otherNationality = '").append(request.getOtherNationality()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("otherNational");
        }
        else {
            conditionCitizen.append(" c.otherNationality is null \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
        }
        if (request.getReligionId() != null) {
            conditionCitizen.append(" c.religion.id = ").append(request.getReligionId()).append(" \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("religion");
        }
        else {
            conditionCitizen.append(" c.religion is null \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
        }
        if (request.getJob() != null) {
            conditionCitizen.append(" c.job = '").append(request.getJob()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("job");
        }
        else {
            conditionCitizen.append(" c.job is null \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
        }
        if (request.getEducationalLevel() != null) {
            conditionCitizen.append(" c.educationalLevel = '").append(request.getEducationalLevel()).append("' \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
            System.out.println("education Level");
        }
        else {
            conditionCitizen.append(" c.educationalLevel is null \n");
            conditionClauses.add(String.valueOf(conditionCitizen));
            conditionCitizen.setLength(0);
        }

        String  conditionsQuery = String.join(" and ", conditionClauses);

        if (conditionsQuery.length() > 0) {
            conditionsQuery = " where " + conditionsQuery;
            query.append(conditionsQuery);
        }
        System.out.println(query);
        Query nativeQuery = entityManager.createQuery(String.valueOf(query));
        List<CustomCitizenResponse> list = nativeQuery.getResultList();
        System.out.println(list.size());
        return list;
    }


}