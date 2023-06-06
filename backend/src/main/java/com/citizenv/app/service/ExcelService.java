package com.citizenv.app.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    String createUserFromExcelFile(MultipartFile excelFile);
    ByteArrayResource exportDataToExcel(String divisionCode) throws IOException;
}
