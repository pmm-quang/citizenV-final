package com.citizenv.app.controller;

import com.citizenv.app.payload.AdministrativeRegionDto;
import com.citizenv.app.service.AdministrativeRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/administrativeRegion/")
public class AdministrativeRegionController {

    @Autowired
    private AdministrativeRegionService service;

    @GetMapping("/")
    public ResponseEntity<List<AdministrativeRegionDto>> getAll() {
        List<AdministrativeRegionDto> list = service.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{admRegionID}")
    public ResponseEntity<Object> getById(@PathVariable int admRegionID) {
        try {
            AdministrativeRegionDto admRegion = service.getById(admRegionID);
            return new ResponseEntity<>(admRegion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
