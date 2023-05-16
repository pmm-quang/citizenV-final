package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.HamletRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.HamletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HamletServiceImpl implements HamletService {

    @Autowired
    private HamletRepository repository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;
    @Autowired
    private ModelMapper mapper;

    private StringBuilder sb = new StringBuilder();

    @Override
    public List<HamletDto> getAll() {
        List<Hamlet> hamlets = repository.findAll();
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public HamletDto getByCode(String hamletCode) {
        Hamlet foundHamlet = repository.findByCode(hamletCode).orElseThrow(
                () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode));

        return mapper.map(foundHamlet, HamletDto.class);
    }

    @Override
    public List<HamletDto> getAllByWardCode(String wardCode) {
        Ward ward = wardRepository.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        List<Hamlet> hamlets = repository.findAllByWard(ward);
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public List<HamletDto> getAllByAdministrativeUnitId(int administrativeUnitID) {
        AdministrativeUnit administrativeUnit = administrativeUnitRepository.findById(administrativeUnitID)
                .orElseThrow(() -> new ResourceNotFoundException("AdministrativeUnit","AdministrativeUnitID", String.valueOf(administrativeUnitID)));
        List<Hamlet> hamlets = repository.findAllByAdministrativeUnit(administrativeUnit);
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public HamletDto createHamlet(CustomHamletRequest hamlet) {
       String hamletCode = hamlet.getCode();
       repository.findByCode(hamletCode).ifPresent(h -> {
           throw new ResourceFoundException("Hamlet", "HamletCode", hamletCode);
       });
       Hamlet createHamlet = validate(hamlet);
       Hamlet newHamlet = repository.save(createHamlet);
       return mapper.map(newHamlet, HamletDto.class);
    }

    @Transactional
    @Override
    public HamletDto updateHamlet(String hamletCodeNeedUpdate, HamletDto hamlet) {
        String hamletCode = hamlet.getCode();

        Hamlet foundHamlet =  repository.findByCode(hamletCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCodeNeedUpdate)
        );

        if (!hamletCodeNeedUpdate.equals(hamletCode)) {
            repository.findByCode(hamletCode).ifPresent(h -> {
                throw new ResourceFoundException("Hamlet", "HamletCode", hamletCode);});
        }
        Hamlet createHamlet = validate(hamlet.getCode(), hamlet.getName(), hamlet.getWard().getCode(), hamlet.getAdministrativeUnit().getId());
        foundHamlet.setCode(createHamlet.getCode());
        foundHamlet.setName(createHamlet.getName());
        foundHamlet.setWard(createHamlet.getWard());
        foundHamlet.setAdministrativeUnit(createHamlet.getAdministrativeUnit());
        return mapper.map(foundHamlet, HamletDto.class);
    }

    @Transactional
    @Override
    public HamletDto updateHamlet(String hamletCodeNeedUpdate, CustomHamletRequest hamlet) {
        Hamlet foundHamlet =  repository.findByCode(hamletCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCodeNeedUpdate)
        );
        if (!hamletCodeNeedUpdate.equals(hamlet.getCode())) {
            repository.findByCode(hamlet.getCode()).ifPresent(h -> {
                throw new ResourceFoundException("Hamlet", "HamletCode", hamlet.getCode());});
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
//        if (!Utils.validateName(hamletName)) {
//            throw new InvalidException("Ten don vi khong dung dinh dang");
//        }
        if (!Utils.AdministrativeUnitsLv4.containsKey(admUnitId)) {
            throw new InvalidException("Don vi hanh chinh khong hop le");
        }
        if (hamletCode.indexOf(wardCode) != 0) {
            throw new InvalidException("Ma don vi khong hop le");
        }
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );
        Ward foundWard  = wardRepository.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        Hamlet newHamlet = new Hamlet();
        newHamlet.setName(hamletName);
        newHamlet.setCode(hamletCode);
        newHamlet.setAdministrativeUnit(foundAdmUnit);
        newHamlet.setWard(foundWard);
        return newHamlet;
    }

    private Hamlet validate(String hamletCode, String hamletName, String wardCode, Integer admUnitId) {
//        if (!Utils.validateName(hamletName)) {
//            throw new InvalidException("Ten don vi khong dung dinh dang");
//        }
        if (!Utils.AdministrativeUnitsLv4.containsKey(admUnitId)) {
            throw new InvalidException("Don vi hanh chinh khong hop le");
        }
        if (hamletCode.indexOf(wardCode) != 0) {
            throw new InvalidException("Ma don vi khong hop le");
        }
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );
        Ward foundWard  = wardRepository.findByCode(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
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

