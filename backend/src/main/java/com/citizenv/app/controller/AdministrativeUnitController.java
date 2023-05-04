package com.citizenv.app.controller;

import com.citizenv.app.payload.AdministrativeUnitDto;
import com.citizenv.app.service.AdministrativeUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administrativeUnit/")
public class AdministrativeUnitController {
    @Autowired
    private AdministrativeUnitService service;

    @GetMapping("/")
    public ResponseEntity<List<AdministrativeUnitDto>> getAll() {
        List<AdministrativeUnitDto>  list = service.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{admUnitID}")
    public ResponseEntity<Object> getById(@PathVariable int admUnitID) {
        try {
            AdministrativeUnitDto admUnit = service.getById(admUnitID);
            return new ResponseEntity<>(admUnit, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
