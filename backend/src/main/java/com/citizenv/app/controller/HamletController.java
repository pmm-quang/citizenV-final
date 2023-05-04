package com.citizenv.app.controller;

import com.citizenv.app.exception.ResourceException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.service.HamletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hamlet/")
public class HamletController {
    @Autowired
    private HamletService service;

    @GetMapping("/")
    public ResponseEntity<List<HamletDto>> getAll() {
        List<HamletDto> list = service.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{hamletCode}")
    public ResponseEntity<HamletDto> getByCode(@PathVariable String hamletCode) {
        HamletDto hamletDto = service.getByCode(hamletCode);
        return new ResponseEntity<>(hamletDto, HttpStatus.OK);
    }

    @GetMapping("/ward/{wardCode}")
    public ResponseEntity<List<HamletDto>> getAllByWardCode(@PathVariable String wardCode) {
        List<HamletDto> list = service.getAllByWardCode(wardCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/administrativeUnit/{admUnitID}")
    public ResponseEntity<List<HamletDto>> getAllByAdministrativeUnitId(@PathVariable int admUnitID) {
        List<HamletDto> list = service.getAllByAdministrativeUnitId(admUnitID);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create/")
    public ResponseEntity<Object> createHamlet(@RequestBody HamletDto hamlet) {
        try {
            HamletDto hamletDto = service.createHamlet(hamlet);
            return hamletDto != null ? new ResponseEntity<>(hamletDto, HttpStatus.OK) : new ResponseEntity<>("Mã đơn vị không trùng khớp hoặc cấp bậc hành chính không chính xác", HttpStatus.OK);
//            if (hamletDto != null) {
//                return new ResponseEntity<>(hamletDto, HttpStatus.CREATED);
//            }
//            return new ResponseEntity<>("Mã đơn vị không trùng khớp hoặc cấp bậc hành chính không chính xác", HttpStatus.OK);
        } catch (ResourceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{hamletCode}/")
    public ResponseEntity<Object> updateHamlet(@PathVariable String hamletCode,
                                                  @RequestBody HamletDto hamlet) {
        try {
            HamletDto hamletDto = service.updateHamlet(hamletCode, hamlet);
            return hamletDto != null ? new ResponseEntity<>(hamletDto, HttpStatus.OK) : new ResponseEntity<>("Mã đơn vị không trùng khớp hoặc cấp bậc hành chính không chính xác", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
