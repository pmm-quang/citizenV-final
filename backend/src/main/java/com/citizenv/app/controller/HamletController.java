package com.citizenv.app.controller;

import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.service.HamletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
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

    @GetMapping("/wardCode/{wardCode}")
    public ResponseEntity<List<HamletDto>> getAllByWardCode(@PathVariable String wardCode) {
        List<HamletDto> list = service.getAllByWardCode(wardCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/administrativeUnit/{admUnitID}")
    public ResponseEntity<List<HamletDto>> getAllByAdministrativeUnitId(@PathVariable int admUnitID) {
        List<HamletDto> list = service.getAllByAdministrativeUnitId(admUnitID);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<HamletDto> createHamlet(@RequestBody CustomHamletRequest hamlet) {
        HamletDto hamletDto = service.createHamlet(hamlet);
        return ResponseEntity.status(201).body(hamletDto);
    }

    @PutMapping("/save/{hamletCode}")
    public ResponseEntity<Object> updateHamlet(@PathVariable String hamletCode,
                                                  @RequestBody CustomHamletRequest hamlet) {
        HamletDto hamletDto = service.updateHamlet(hamletCode, hamlet);
        return ResponseEntity.ok().body(hamletDto);
    }
    @GetMapping("/no/")
    public void getNo() {
        service.nono();
        System.out.println("haha");
    }
}
