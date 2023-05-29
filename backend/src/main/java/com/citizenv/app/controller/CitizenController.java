package com.citizenv.app.controller;

import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import com.citizenv.app.payload.custom.CustomCitizenResponse;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.CitizenService;
import com.citizenv.app.service.ExcelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/citizen")
public class CitizenController {
//    @Autowired
    private final CitizenService citizenService;
    private final ExcelService excelService;

    public CitizenController(CitizenService citizenService, ExcelService excelService) {
        this.citizenService = citizenService;
        this.excelService = excelService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CitizenDto>> getAll() {
        List<CitizenDto> citizenDtoList = citizenService.getAll();
        return new ResponseEntity<>(citizenDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/", params = "page")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam int page) {
        Map<String, Object> dtoPaginationList = citizenService.getAll(page);
        return new ResponseEntity<>(dtoPaginationList, HttpStatus.OK);
    }

    @GetMapping("/{nationalId}")
    public ResponseEntity<CitizenDto> getByNationalId(@PathVariable String nationalId) {
        CitizenDto citizenDto = citizenService.getByNationalId(nationalId);
        return new ResponseEntity<>(citizenDto, HttpStatus.OK);
    }

    @GetMapping(value = "/by-hamlet/{hamletCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByHamletCode(@PathVariable String hamletCode, @RequestParam int page) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String divisionCode = SecurityUtils.getDivisionCodeCurrentUserLogin();
        if (divisionCode == null) {
            Map<String, Object> list = citizenService.getAllByHamletCode(hamletCode, page);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            if (hamletCode.indexOf(divisionCode) == 0) {
                Map<String, Object> list = citizenService.getAllByHamletCode(hamletCode, page);
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                throw new AccessDeniedException("Khong co quyen try cap");
            }
        }
    }

    @GetMapping(value = "/by-ward/{wardCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByWardCode(@PathVariable String wardCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByWardCode(wardCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/by-district/{districtCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByDistrictCode(@PathVariable String districtCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByDistrictCode(districtCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    @GetMapping(value = "/by-province/{provinceCode}", params = "page")
//    public ResponseEntity<Map<String, Object>> getAllByProvinceCode(@PathVariable String provinceCode, @RequestParam int page) {
//        Map<String, Object> list = citizenService.getAllByProvinceCode(provinceCode, page);
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

//    @GetMapping("/by-hamlet/{hamletCode}")
//    public ResponseEntity<List<CitizenDto>> getAllByHamletCode(@PathVariable String hamletCode) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
//        String divisionCode = userDetail.getUser().getDivision().getCode();
//        if (hamletCode.indexOf(divisionCode) == 0) {
//            List<CitizenDto> list = citizenService.getAllByHamletCode(hamletCode);
//            return new ResponseEntity<>(list, HttpStatus.OK);
//        } else {
//            throw new AccessDeniedException("Khong co quyen truy cap");
//        }
//    }

//    @GetMapping("/by-ward/{wardCode}")
//    public ResponseEntity<List<CitizenDto>> getAllByWardCode(@PathVariable String wardCode) {
//        List<CitizenDto> list = citizenService.getAllByWardCode(wardCode);
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

//    @GetMapping("/by-district/{districtCode}")
//    public ResponseEntity<List<CitizenDto>> getAllByDistrictCode(@PathVariable String districtCode) {
//        List<CitizenDto> list = citizenService.getAllByDistrictCode(districtCode);
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

//    @GetMapping("/by-province/{provinceCode}")
//    public ResponseEntity<List<CitizenDto>> getAllByProvinceCode(@PathVariable String provinceCode) {
//        List<CitizenDto> list = citizenService.getAllByProvinceCode(provinceCode);
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

    @PreAuthorize("hasAnyAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<?> createCitizen(@RequestBody CustomCitizenRequest citizen) {
        String message = citizenService.createCitizen(citizen);
        return ResponseEntity.status(201).body(message);
    }

    @PreAuthorize("hasAnyAuthority('WRITE')")
    @PutMapping("/save/{citizenId}")
    public ResponseEntity<?> updateCitizen(@PathVariable String citizenId, @RequestBody CustomCitizenRequest citizen) {
        String message = citizenService.updateCitizen(citizenId, citizen);
        return ResponseEntity.ok().body(message);
    }

    @PreAuthorize("hasAnyAuthority('WRITE')")
    @PostMapping(value = "/excel/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("excelFile") MultipartFile excelFile) throws IOException {
        System.out.println(excelFile.getSize());
//        File tempFile = ((MultipartFile) excelFile).getInputStream().getFile();
//        if (tempFile != null && tempFile.exists()) {
//            tempFile.delete();
//        }
//        String filePath = "src/main/java/com/citizenv/app/controller/file.xlsx";
        String message = excelService.createUserFromExcelFile(excelFile);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping ("/search")
    public ResponseEntity<?> searchCitizen(@RequestBody CustomCitizenRequest request) {
        List<CustomCitizenResponse> list = citizenService.searchCitizen(request);
        return ResponseEntity.ok().body(list);
    }

}
