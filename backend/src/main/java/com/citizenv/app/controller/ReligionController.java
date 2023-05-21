package com.citizenv.app.controller;

import com.citizenv.app.payload.ReligionDto;
import com.citizenv.app.service.EthnicityService;
import com.citizenv.app.service.ReligionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/religion")
public class ReligionController {
    private final ReligionService religionService;

    public ReligionController(ReligionService religionService) {
        this.religionService = religionService;
    }

    @GetMapping("/")//OK
    public ResponseEntity<List<ReligionDto>> getAll() {
        List<ReligionDto> dtoList = religionService.getAll();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}