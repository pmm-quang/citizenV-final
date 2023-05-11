package com.citizenv.app.service.impl;


import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.*;
import com.citizenv.app.repository.*;
import com.citizenv.app.service.CitizenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CitizenServiceImpl implements CitizenService {
    @Autowired
    private ModelMapper mapper;

    Logger logger = LogManager.getRootLogger();

    @Autowired
    private CitizenRepository repo;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private ProvinceRepository provinceRepo;
    @Autowired
    private WardRepository wardRepo;
    @Autowired
    private HamletRepository hamletRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private EthnicityRepository ethnicityRepo;
    @Autowired
    private ReligionRepository religionRepo;
    @Autowired
    private AssociationRepository associationRepo;


    public List<CitizenDto> getAll() {
        List<Citizen> entities = repo.findAll();
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
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
    public List<CitizenDto> getAllByHamletCode(String hamletCode) {
        Hamlet foundHamlet = hamletRepo.findById(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );
//        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, "1");
        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByWardCode(String wardCode) {
        Ward foundWard = wardRepo.findById(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        List<Citizen> entities = repo.findAllByWardCode(wardCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findById(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        List<Citizen> entities = repo.findAllByDistrictCode(districtCode, 2);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepo.findById(provinceCode).orElseThrow(
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
        String citizenId = citizen.getId();
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
        String cIdUpdate = citizen.getId();

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
//                if (addressIdListOfOriginalCitizen.size() > 0) {
//                    addressIdListOfOriginalCitizen.forEach(id -> {
//                        addressRepo.deleteById(id);
//                    });
//                }
//                associationIdListOfOriginalCitizen.removeAll(associationIdListOfNewCitizen);
//                if (associationIdListOfOriginalCitizen.size() > 0) {
//                    associationIdListOfOriginalCitizen.forEach(a -> {
//                        associationRepo.deleteById(a);
//                    });
//                }
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
        String citizenId = citizen.getId();
        String provinceCodeOfQueQuan = null;

        //Kiểm tra mã địa chỉ - address
        int countRequiredAddress = 0;
        for (AddressDto a: citizen.getAddresses()) {
            if (a.getAddressType().getId() == 1 || a.getAddressType().getId() == 2) {
                countRequiredAddress ++;
            }
            Hamlet h = hamletRepo.findById(a.getHamlet().getCode()).orElseThrow(
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
        if (!Utils.validateNationalId(citizenId, citizen.getSex(), provinceCodeOfQueQuan, citizen.getDateOfBirth())) {
            throw new InvalidException("Ma so dinh danh khong hop le");
        }
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


}
