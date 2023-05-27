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
    public ResponseEntity<List<WardDto>> getAll() {
//        CustomUserDetail userDetail = getUserDetail();
//        List<WardDto> wardDtoList = wardService.getAll(userDetail);
        List<WardDto> wardDtoList = wardService.getAll();
        System.out.println(wardDtoList.size());
        return new ResponseEntity<List<WardDto>>(wardDtoList, HttpStatus.OK);
    }

    @GetMapping("/{wardCode}")
    public ResponseEntity<WardDto> getByCode(@PathVariable String wardCode) {
        WardDto wardDto = wardService.getByCode(wardCode);
        return new ResponseEntity<>(wardDto, HttpStatus.OK);
    }

    @GetMapping("/by-district/{districtCode}")
    public ResponseEntity<Object> getAllByDistrictCode(@PathVariable String districtCode) {
        List<WardDto> list = wardService.getByDistrictCode(districtCode);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/by-district")
    public ResponseEntity<List<WardDto>> getAllByDistrictCode() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
//        String districtCode = userDetail.getUser().getDivision().getCode();
        String districtCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        List<WardDto> list = wardService.getByDistrictCode(districtCode);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/by-administrative-unit/{admUnitID}")
    public ResponseEntity<List<WardDto>> getAllByAdministrativeUnitID(@PathVariable int admUnitID) {
        List<WardDto> list= wardService.getByAdministrativeUnitId(admUnitID);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


//    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<WardDto> createWard(@RequestBody CustomWardRequest ward) {
//        CustomUserDetail userDetail = getUserDetail();
//        String divisionCodeOfUserDetail = userDetail.getUser().getDivision().getCode();
//        String districtCode = ward.getDistrictCode();
//        if (ward.getCode() != null) {
//            if (divisionCodeOfUserDetail == null || ward.getCode().indexOf(divisionCodeOfUserDetail) != 0) {
//                throw new InvalidException("Khong co quyen tao ward voi ma nay");
//            }
//        }
        String divisionCodeOfUserDetail = SecurityUtils.getDivisionCodeCurrentUserLogin();
        WardDto wardDto = wardService.createWard(divisionCodeOfUserDetail,ward);
       return ResponseEntity.status(201).body(wardDto);
    }

//    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/save/{wardCode}/")
    public ResponseEntity<WardDto> updateWard(@PathVariable String wardCode,
                                              @RequestBody CustomWardRequest ward) {
//        CustomUserDetail userDetail = getUserDetail();
//        String divisionCodeOfUserDetail = userDetail.getUser().getDivision().getCode();
//        String districtCode = ward.getDistrictCode();
//        if (wardCode != null) {
//            if (divisionCodeOfUserDetail == null || ward.getCode().indexOf(divisionCodeOfUserDetail) != 0) {
//                throw new InvalidException("Khong co quyen chinh sua ward voi ma nay");
//            }
//        }
       WardDto wardDto = wardService.updateWard(wardCode, ward);
       return ResponseEntity.ok().body(wardDto);
    }

//    private CustomUserDetail getUserDetail() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (CustomUserDetail) authentication.getPrincipal();
//    }
}