package com.citizenv.app.controller;

import com.citizenv.app.config.SecurityConfig;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/ward")
public class WardController {
    final
    WardService wardService;

    public WardController(WardService wardService) {
        this.wardService = wardService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        String divisionCodeOfUserDetail = SecurityUtils.getDivisionCodeCurrentUserLogin();
//        System.out.println(wardDtoList.size());
        List<WardDto> wardDtoList = wardService.getAll(divisionCodeOfUserDetail);
        return new ResponseEntity<List<WardDto>>(wardDtoList, HttpStatus.OK);
    }

    @GetMapping("/{wardCode}")
    public ResponseEntity<WardDto> getByCode(@PathVariable String wardCode) {
        WardDto wardDto = wardService.getByCode(wardCode);
        return new ResponseEntity<>(wardDto, HttpStatus.OK);
    }

    @GetMapping("/by-district/{districtCode}")
    public ResponseEntity<?> getAllByDistrictCode(@PathVariable String districtCode) {
        List<WardDto> list = wardService.getByDistrictCode(districtCode);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/by-district")
    public ResponseEntity<?> getAllByDistrictCode() {
        String districtCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        List<WardDto> list = wardService.getByDistrictCode(districtCode);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/by-administrative-unit/{admUnitID}")
    public ResponseEntity<?> getAllByAdministrativeUnitID(@PathVariable int admUnitID) {
        List<WardDto> list= wardService.getByAdministrativeUnitId(admUnitID);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<?> createWard(@RequestBody CustomWardRequest ward) {
        String divisionCodeOfUserDetail = SecurityUtils.getDivisionCodeCurrentUserLogin();
        String message = wardService.createWard(divisionCodeOfUserDetail,ward);
       return ResponseEntity.status(201).body(message);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/save/{wardCode}/")
    public ResponseEntity<?> updateWard(@PathVariable String wardCode,
                                              @RequestBody CustomWardRequest ward) {
       String message = wardService.updateWard(wardCode, ward);
       return ResponseEntity.ok().body(message);
    }

}