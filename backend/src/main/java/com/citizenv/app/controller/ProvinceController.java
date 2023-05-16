package com.citizenv.app.controller;

import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/province")
public class ProvinceController {
    @Autowired
    ProvinceService provinceService;

    @GetMapping("/")
    public ResponseEntity<List<ProvinceDto>> getAll() {
        List<ProvinceDto> provinceDtoList = provinceService.getAll();
        return new ResponseEntity<List<ProvinceDto>>(provinceDtoList, HttpStatus.OK);
    }

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

    @GetMapping("/administrativeUnitId/{admUnitId}")
    public ResponseEntity<Object> getAllByAdministrativeUnitId(@PathVariable int admUnitId) {
        List<ProvinceDto> list = provinceService.getAllByAdministrativeUnitId(admUnitId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/administrativeRegionId/{admRegionId}")
    public ResponseEntity<Object> getAllByAdministrativeRegionId(@PathVariable int admRegionId) {
        List<ProvinceDto> list = provinceService.getAllByAdministrativeRegionId(admRegionId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody ProvinceDto provinceDto) {
         ProvinceDto newProvince = provinceService.createProvince(provinceDto);
         return new ResponseEntity<>(newProvince, HttpStatus.CREATED);
    }

    @PutMapping("/{provinceCode}")
    public ResponseEntity<ProvinceDto> update(@PathVariable String provinceCode, @RequestBody ProvinceDto province) {
        ProvinceDto newProvince = provinceService.updateProvince(provinceCode, province);
        return new ResponseEntity<>(newProvince, HttpStatus.OK);
    }


    @DeleteMapping("/{provinceId}")
    public ResponseEntity<String> deleteById(@PathVariable Long provinceId) {
            return new ResponseEntity<>(provinceService.deleteById(provinceId), HttpStatus.OK);
    }
}
