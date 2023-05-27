package com.citizenv.app.controller;

import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/province")
public class ProvinceController {
    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }


    //    @PreAuthorize("hasAuthority('READ')")
//    @Secured("ROLE_A1")
    @GetMapping("/")
    public ResponseEntity<List<ProvinceDto>> getAll() {
        List<ProvinceDto> provinceDtoList = provinceService.getAll();
        return new ResponseEntity<List<ProvinceDto>>(provinceDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_A1")
    @GetMapping(value = "/", params = "page")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam int page) {
        Map<String, Object> provinceDtoPaginationList = provinceService.getAll(page);
        return new ResponseEntity<>(provinceDtoPaginationList, HttpStatus.OK);
    }

    @GetMapping("/{provinceCode}")
    public ResponseEntity<ProvinceDto> getByCode(@PathVariable String provinceCode) {
        ProvinceDto provinceDto = provinceService.getByCode(provinceCode);
        return new ResponseEntity<>(provinceDto, HttpStatus.OK);
    }

    @GetMapping("/by-administrative-unit/{admUnitId}")
    public ResponseEntity<Object> getAllByAdministrativeUnitId(@PathVariable int admUnitId) {
        List<ProvinceDto> list = provinceService.getAllByAdministrativeUnitId(admUnitId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/by-administrative-region/{admRegionId}")
    public ResponseEntity<Object> getAllByAdministrativeRegionId(@PathVariable int admRegionId) {
        List<ProvinceDto> list = provinceService.getAllByAdministrativeRegionId(admRegionId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<Object> create(@RequestBody ProvinceDto provinceDto) {
         ProvinceDto newProvince = provinceService.createProvince(provinceDto);
         return new ResponseEntity<>(newProvince, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("save/{provinceCode}")
    public ResponseEntity<ProvinceDto> update(@PathVariable String provinceCode, @RequestBody ProvinceDto province) {
        ProvinceDto newProvince = provinceService.updateProvince(provinceCode, province);
        return new ResponseEntity<>(newProvince, HttpStatus.OK);
    }


//    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> deleteById(@PathVariable String code) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
