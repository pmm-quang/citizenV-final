package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.HamletRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.HamletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public List<HamletDto> getAll() {
        List<Hamlet> hamlets = repository.findAll();
        List<HamletDto> hamletDtos = hamlets.stream().map(hamlet -> mapper.map(hamlet, HamletDto.class)).collect(Collectors.toList());
        return hamletDtos;
    }

    @Override
    public HamletDto getByCode(String hamletCode) {
        Hamlet foundHamlet = repository.findById(hamletCode).orElseThrow(
                () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode));

        return mapper.map(foundHamlet, HamletDto.class);
    }

    @Override
    public List<HamletDto> getAllByWardCode(String wardCode) {
        Ward ward = wardRepository.findById(wardCode).orElseThrow(
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
    public HamletDto createHamlet(HamletDto hamlet) {
        String hamletCode = hamlet.getCode();
        String wardCode = hamlet.getWard().getCode();
        int administrativeUnitID = hamlet.getAdministrativeUnit().getId();
        boolean isAdministrativeUnitsLv4 = false;

        repository.findById(hamletCode).ifPresent(foundHamlet -> {
            throw new ResourceFoundException("Hamlet", "HamletCode", hamletCode);
        });

        Ward foundWard = wardRepository.findById(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );

        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(administrativeUnitID).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitID", String.valueOf(administrativeUnitID))
        );

        for (Utils.AdministrativeUnitsLv4 a: Utils.AdministrativeUnitsLv4.values()) {
            if (administrativeUnitID == a.getId()) {
                isAdministrativeUnitsLv4 = true;
            }
        }

        if (hamletCode.indexOf(wardCode) == 0 && isAdministrativeUnitsLv4) {
            Hamlet newHamlet = new Hamlet();
            newHamlet.setCode(hamlet.getCode());
            newHamlet.setWard(foundWard);
            newHamlet.setName(hamlet.getName());
            newHamlet.setAdministrativeUnit(foundAdmUnit);
            return mapper.map(repository.save(newHamlet), HamletDto.class);
        }
        return null;
    }

    @Override
    public HamletDto updateHamlet(String hamletCodeNeedUpdate, HamletDto hamlet) {
        String hamletCode = hamlet.getCode();
        String wardCode = hamlet.getWard().getCode();
        int administrativeUnitID = hamlet.getAdministrativeUnit().getId();
        boolean isAdministrativeUnitsLv4 = false;

        Hamlet foundHamlet =  repository.findById(hamletCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCodeNeedUpdate)
        );

        Ward foundWard = wardRepository.findById(wardCode).orElseThrow(
                ()-> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );

        if (!hamletCodeNeedUpdate.equals(hamletCode)) {
            repository.findById(hamletCode).ifPresent(h -> {
                throw new ResourceFoundException("Hamlet", "HamletCode", hamletCode);});
        }

        for (Utils.AdministrativeUnitsLv4 a: Utils.AdministrativeUnitsLv4.values()) {
            if (administrativeUnitID == a.getId()) {
                isAdministrativeUnitsLv4 = true;
            }
        }
        if (hamletCode.indexOf(wardCode) == 0 && isAdministrativeUnitsLv4) {
            foundHamlet.setName(hamlet.getName());
            foundHamlet.setWard(foundWard);
            return mapper.map(repository.save(foundHamlet), HamletDto.class);
        }
        return null;
    }
}

