package com.citizenv.app.controller;

import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.HamletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("/api/v1/hamlet/")
public class HamletController {
    private final HamletService service;

    public HamletController(HamletService service) {
        this.service = service;
    }

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

    @GetMapping("/by-ward/{wardCode}")
    public ResponseEntity<List<HamletDto>> getAllByWardCode(@PathVariable String wardCode) {
        List<HamletDto> list = service.getAllByWardCode(wardCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Secured("ROLE_B1")
    @GetMapping("/by-ward")
    public ResponseEntity<List<HamletDto>> getAllByWardCode() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
//        String wardCode = userDetail.getUser().getDivision().getCode();
        String wardCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        List<HamletDto> list = service.getAllByWardCode(wardCode);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-administrative-unit/{admUnitID}")
    public ResponseEntity<List<HamletDto>> getAllByAdministrativeUnitId(@PathVariable int admUnitID) {
        List<HamletDto> list = service.getAllByAdministrativeUnitId(admUnitID);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<HamletDto> createHamlet(@RequestBody CustomHamletRequest hamlet) {
        String divisionCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        HamletDto hamletDto = service.createHamlet(divisionCode,hamlet);
        return ResponseEntity.status(201).body(hamletDto);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/save/{hamletCode}")
    public ResponseEntity<Object> updateHamlet(@PathVariable String hamletCode,
                                                  @RequestBody CustomHamletRequest hamlet) {
        HamletDto hamletDto = service.updateHamlet(hamletCode, hamlet);
        return ResponseEntity.ok().body(hamletDto);
    }

    /*Script to add more hamlets to Hanoi. Dangerous.*/
    @GetMapping("/no/")
    public void getNo() {
        service.nono();
        System.out.println("haha");
    }
}
