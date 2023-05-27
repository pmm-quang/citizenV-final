package com.citizenv.app.controller;

import com.citizenv.app.payload.DistrictDto;

import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/district")
public class DistrictController {
    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/")//OK
    public ResponseEntity<List<DistrictDto>> getAll() {
        String divisionCodeOfUserDetail = SecurityUtils.getDivisionCodeCurrentUserLogin();
        List<DistrictDto> districtDtoList = districtService.getAll(divisionCodeOfUserDetail);
        return new ResponseEntity<List<DistrictDto>>(districtDtoList, HttpStatus.OK);
    }

    @GetMapping("/{districtCode}")//OK
    public ResponseEntity<DistrictDto> getByCode(@PathVariable String districtCode) {
        DistrictDto districtDto = districtService.getByCode(districtCode);
        return new ResponseEntity<>(districtDto, HttpStatus.OK);
    }

    @GetMapping("/by-province/{provinceCode}")
    public ResponseEntity<List<DistrictDto>> getAllByProvinceCode(@PathVariable String provinceCode) {
        List<DistrictDto> list = districtService.getAllByProvinceCode(provinceCode);
        return ResponseEntity.ok().body(list);
    }

    /**
     * Lấy ra toàn bộ district mà user quản lý
     */
    @Secured("ROLE_A2")
    @GetMapping("/by-province")
    public ResponseEntity<List<DistrictDto>> getAllByProvinceCode() {
        String provinceCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        List<DistrictDto> list = districtService.getAllByProvinceCode(provinceCode);
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<DistrictDto> createDistrict(@RequestBody DistrictDto district) {
        String divisionCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        DistrictDto districtDto = districtService.createDistrict(divisionCode,district);
        return new ResponseEntity<>(districtDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/save/{districtCode}")
    public ResponseEntity<DistrictDto> updateDistrict(@PathVariable String districtCode,
                                                      @RequestBody DistrictDto district) {
        DistrictDto districtDto = districtService.updateDistrict(districtCode, district);
        return ResponseEntity.ok().body(districtDto);
    }
}