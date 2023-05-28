package com.citizenv.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    String createUserFromExcelFile(MultipartFile excelFile);

}
