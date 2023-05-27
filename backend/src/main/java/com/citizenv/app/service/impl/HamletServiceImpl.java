package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.HamletRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.HamletService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HamletServiceImpl implements HamletService {

    private final HamletRepository repository;
    private final WardRepository wardRepository;
    private final AdministrativeUnitRepository administrativeUnitRepository;
    private final ModelMapper mapper;
    private final AdministrativeDivisionRepository admDivisionRepo;

    public HamletServiceImpl(HamletRepository repository, WardRepository wardRepository, AdministrativeUnitRepository administrativeUnitRepository, ModelMapper mapper, AdministrativeDivisionRepository admDivisionRepo) {
        this.repository = repository;
        this.wardRepository = wardRepository;
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.mapper = mapper;
        this.admDivisionRepo = admDivisionRepo;
    }

    private StringBuilder sb = new StringBuilder();


    @Override
    public List<HamletDto> getAll() {
        List<Hamlet> hamlets = repository.findAll();
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public List<HamletDto> getAll(String divisionCodeOfUserDetail) {
        List<Hamlet> list = new ArrayList<>();
        if (divisionCodeOfUserDetail == null) {
            list.addAll(repository.findAll());
        } else {
            list.addAll(repository.findAllBySupDivisionCode(divisionCodeOfUserDetail));
        }
        return list.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
    }

    @Override
    public HamletDto getByCode(String hamletCode) {
        Hamlet foundHamlet = repository.findByCode(hamletCode).orElseThrow(
                () -> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamletCode));

        return mapper.map(foundHamlet, HamletDto.class);
    }

    @Override
    public List<HamletDto> getAllByWardCode(String wardCode) {
        Ward ward = wardRepository.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Xã/phường/thị trấn", "mã định danh", wardCode)
        );
        List<Hamlet> hamlets = repository.findAllByWard(ward);
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public List<HamletDto> getAllByAdministrativeUnitId(int administrativeUnitID) {
        AdministrativeUnit administrativeUnit = administrativeUnitRepository.findById(administrativeUnitID)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn vị hành chính","id", String.valueOf(administrativeUnitID)));
        List<Hamlet> hamlets = repository.findAllByAdministrativeUnit(administrativeUnit);
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public HamletDto createHamlet(String divisionCode, CustomHamletRequest hamlet) {
        hamlet.setWardCode(divisionCode);
       String hamletCode = hamlet.getCode();
       if (hamletCode != null) {
           repository.findByCode(hamletCode).ifPresent(h -> {
               throw new ResourceFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamletCode);
           });
       }
       if (hamlet.getName() != null && hamlet.getWardCode() != null) {
           admDivisionRepo.findByName(hamlet.getName(), hamlet.getName()).ifPresent(
                   division -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
           );
       }
       Hamlet createHamlet = validate(hamlet);
       Hamlet newHamlet = repository.save(createHamlet);
       return mapper.map(newHamlet, HamletDto.class);
    }

    @Transactional
    @Override
    public HamletDto updateHamlet(String hamletCodeNeedUpdate, CustomHamletRequest hamlet) {
        Hamlet foundHamlet =  repository.findByCode(hamletCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamletCodeNeedUpdate)
        );
        if (hamlet.getCode() != null && !hamletCodeNeedUpdate.equals(hamlet.getCode())) {
            repository.findByCode(hamlet.getCode()).ifPresent(h -> {
                throw new ResourceFoundException("Thôn/xóm/bản/tổ dân phố", "mã định danh", hamlet.getCode());});
        }
        if (hamlet.getName() != null && hamlet.getWardCode() != null && !hamlet.getName().equals(foundHamlet.getName())) {
            String wardCode = hamlet.getWardCode();
            admDivisionRepo.findByName(hamlet.getName(),wardCode).ifPresent(
                    division -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
            );
        }
        Hamlet createHamlet = validate(hamlet);
        foundHamlet.setCode(createHamlet.getCode());
        foundHamlet.setName(createHamlet.getName());
        foundHamlet.setWard(createHamlet.getWard());
        foundHamlet.setAdministrativeUnit(createHamlet.getAdministrativeUnit());
        return mapper.map(foundHamlet, HamletDto.class);
    }

    private Hamlet validate(CustomHamletRequest hamlet) {
        String hamletName = hamlet.getName();
        String hamletCode = hamlet.getCode();
        String wardCode = hamlet.getWardCode();
        Integer admUnitId = hamlet.getAdministrativeUnitId();

        boolean checkNull = (hamletCode == null || hamletCode.equals("")
                || hamletName == null || hamletName.equals("")
                || wardCode == null || wardCode.equals("") ||
                admUnitId == null);
        if (checkNull) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }
//        if (!Utils.validateName(hamletName)) {
//            throw new InvalidException("Ten don vi khong dung dinh dang");
//        }
        if (!Utils.AdministrativeUnitsLv4.containsKey(admUnitId)) {
            throw new InvalidException(Constant.ERR_MESSAGE_ADMINISTRATIVE_UNIT_INVALID);
        }
        if (hamletCode.indexOf(wardCode) != 0 || !Utils.validateFormatDivisionCode(wardCode)) {
            throw new InvalidException(Constant.ERR_MESSAGE_UNIT_CODE_INVALID);
        }
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính", "id", String.valueOf(admUnitId))
        );
        Ward foundWard  = wardRepository.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Xã/phường/thị trấn", "mã định danh", wardCode)
        );
        Hamlet newHamlet = new Hamlet();
        newHamlet.setName(hamletName);
        newHamlet.setCode(hamletCode);
        newHamlet.setAdministrativeUnit(foundAdmUnit);
        newHamlet.setWard(foundWard);
        return newHamlet;
    }

    @Override
    public void nono() {
        List<Ward> lw = wardRepository.findByQuery();
        for (Ward w :
                lw) {
            for (int i = 0; i < 5; i++) {
                Hamlet h = new Hamlet();
                sb.setLength(0);
                h.setCode(sb.append(w.getCode()).append("0").append(i + 1).toString());
                h.setName(String.valueOf(i + 1));
                h.setWard(w);
                administrativeUnitRepository.findById(14).ifPresent(h::setAdministrativeUnit);
                repository.save(h);
            }
        }
    }


}

