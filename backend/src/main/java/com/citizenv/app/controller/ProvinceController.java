package com.citizenv.app.controller;

import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000/")
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

    @GetMapping("/{provinceId}")
    public ResponseEntity<Object> getById(@PathVariable String provinceId) {
        ProvinceDto provinceDto = provinceService.getById(provinceId);
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
    public ResponseEntity<Object> create(@RequestBody Map<String, Object> provinceJSONInfo) {
         ProvinceDto newProvince = provinceService.createProvince(provinceJSONInfo);
         return new ResponseEntity<>(newProvince, HttpStatus.CREATED);
    }

    @PutMapping("/{provinceId}")
    public ResponseEntity<ProvinceDto> update(@PathVariable String provinceId, @RequestBody ProvinceDto province) {
        ProvinceDto newProvince = provinceService.updateProvince(provinceId, province);
        return new ResponseEntity<>(newProvince, HttpStatus.OK);
    }

//    @PutMapping("/{provinceId}")
//    public ResponseEntity<ProvinceDto> update(@PathVariable String provinceId, @RequestBody Map<String, Object> provinceJSONInfo) throws NoSuchMethodException {
//        ProvinceDto newProvince = provinceService.updateProvince(provinceId, provinceJSONInfo);
//        return new ResponseEntity<>(newProvince, HttpStatus.OK);
//    }

    @DeleteMapping("/{provinceId}")
    public ResponseEntity<String> deleteById(@PathVariable String provinceId) {
            return new ResponseEntity<>(provinceService.deleteById(provinceId), HttpStatus.OK);
    }
}
