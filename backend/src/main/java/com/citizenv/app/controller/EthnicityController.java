package com.citizenv.app.controller;

import com.citizenv.app.payload.DistrictDto;
import com.citizenv.app.payload.EthnicityDto;
import com.citizenv.app.service.EthnicityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/ethnicity")
public class EthnicityController {
    private final EthnicityService ethnicityService;

    public EthnicityController(EthnicityService ethnicityService) {
        this.ethnicityService = ethnicityService;
    }

    @GetMapping("/")//OK
    public ResponseEntity<List<EthnicityDto>> getAll() {
        List<EthnicityDto> ethnicityDtoList = ethnicityService.getAll();
        return new ResponseEntity<>(ethnicityDtoList, HttpStatus.OK);
    }

    /*@GetMapping("/{districtId}")//OK
    public ResponseEntity<DistrictDto> getById(@PathVariable String districtId) {
        DistrictDto districtDto = ethnicityService.getById(districtId);
        return new ResponseEntity<>(districtDto, HttpStatus.OK);
    }*/
}