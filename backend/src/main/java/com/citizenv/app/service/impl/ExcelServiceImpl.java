package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.excel.ExcelCitizen;
import com.citizenv.app.repository.*;
import com.citizenv.app.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    private final CitizenRepository repo;
    private final AddressRepository addressRepo;
    private final AssociationRepository associationRepo;
    private final AddressTypeRepository addressTypeRepo;
    private final AssociationTypeRepository associationTypeRepo;

    private final HamletRepository hamletRepo;
    private final EthnicityRepository ethnicityRepo;
    private final ReligionRepository religionRepo;

    private final ProvinceRepository provinceRepo;

    private final DistrictRepository districtRepo;

    private final WardRepository wardRepo;

    private final AdministrativeDivisionRepository admDivisionRepo;

    public ExcelServiceImpl(CitizenRepository repo, AddressRepository addressRepo, AssociationRepository associationRepo,
                            AddressTypeRepository addressTypeRepo, AssociationTypeRepository associationTypeRepo,
                            HamletRepository hamletRepo, EthnicityRepository ethnicityRepo, ReligionRepository religionRepo,
                            ProvinceRepository provinceRepo, DistrictRepository districtRepo, WardRepository wardRepo, AdministrativeDivisionRepository admDivisionRepo) {
        this.repo = repo;
        this.addressRepo = addressRepo;
        this.associationRepo = associationRepo;
        this.addressTypeRepo = addressTypeRepo;
        this.associationTypeRepo = associationTypeRepo;
        this.hamletRepo = hamletRepo;
        this.ethnicityRepo = ethnicityRepo;
        this.religionRepo = religionRepo;
        this.provinceRepo = provinceRepo;
        this.districtRepo = districtRepo;
        this.wardRepo = wardRepo;
        this.admDivisionRepo = admDivisionRepo;
    }

    @Override
    public String createUserFromExcelFile(MultipartFile excelFile) {
        try {
//            FileInputStream file = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 2;
            int endRow = sheet.getLastRowNum();
            int startCol = 0;
            int endCol = 21;
            List<AddressType> addressTypes = addressTypeRepo.findAll();
            List<AssociationType> associationTypes = associationTypeRepo.findAll();
            List<Citizen> citizens = new ArrayList<>();
            String provinceCodeOfHometown = null;
            for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
                Citizen citizen = new Citizen();
                List<Address> addresses = new ArrayList<>();
                List<Association> associations = new ArrayList<>();
                Row row = sheet.getRow(rowNum);
                Association association = new Association();
                Integer associationTypeIndex = 0;
                for (int colNum = startCol; colNum <= endCol; colNum++) {
                    Cell cell = row.getCell(colNum);
                    switch (colNum + 1) {
                        case 1: citizen.setNationalId(cell.getStringCellValue());break;
                        case 2:
                            if (cell == null || cell.getStringCellValue().equals("")) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
//                            if (!Utils.validateName(model.getName())) {
//                                listRowInvalid.append(rowNum);
//                                break;
//                            }
                            citizen.setName(Utils.standardizeName(cell.getStringCellValue()));
                            break;
                        case 3:
                            if (cell == null || cell.getStringCellValue().equals("")) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
                            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate date = LocalDate.parse(cell.getStringCellValue(), inputFormatter);
                            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = date.format(outputFormatter);
                            citizen.setDateOfBirth(date);
                            break;
                        case 4:
                            if (cell == null || cell.getStringCellValue().equals("")) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                if (!Utils.validateSex(cell.getStringCellValue())) {
                                    throw new InvalidException("Lỗi trên cột 'Giới tính' tại hàng: " + (rowNum + 1) + " và cột: " + (colNum + 1));
                                } else {
                                    citizen.setSex(cell.getStringCellValue());
                                }
                            }
                            break;
                        case 5:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                try {
                                    Utils.BloodType.valueOf(cell.getStringCellValue());
                                    citizen.setBloodType(cell.getStringCellValue());
                                } catch (Exception e) {
                                    throw new InvalidException("Lỗi trên cột 'Nhóm máu' tại hàng: " + (rowNum +1) + " và cột: " + (colNum + 1));
                                }
                            }
                            break;
                        case 6:
                            if (cell == null || cell.getStringCellValue().equals("")) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
                            citizen.setMaritalStatus(cell.getStringCellValue());
                            break;
                        case 7:
                            if (cell == null || cell.getStringCellValue().equals("")) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
                            int finalRowNum = rowNum;
                            int finalColNum = colNum;
                            Ethnicity ethnicity = ethnicityRepo.findByName(cell.getStringCellValue()).orElseThrow(
                                    () -> new InvalidException("Lỗi trên cột 'Dân tộc' tại hàng: " + (finalRowNum+1) + " và cột: " + (finalColNum + 1))
                            );
                            citizen.setEthnicity(ethnicity);
                            break;
                        case 8:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                finalRowNum = rowNum;
                                finalColNum = colNum;
                                Religion religion = religionRepo.findByName(cell.getStringCellValue()).orElseThrow(
                                        () -> new InvalidException("Lỗi trên cột 'Tôn giáo' tại hàng: " + (finalRowNum+1) + " và cột: " + (finalColNum + 1))
                                );
                                citizen.setReligion(religion);
                            }
                            break;
                        case 9:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                citizen.setOtherNationality(cell.getStringCellValue());
                            }
                            break;
                        case 10:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                citizen.setEducationalLevel(cell.getStringCellValue());
                            }
                            break;
                        case 11:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                citizen.setJob(cell.getStringCellValue());
                            }
                            break;
                        case 12: case 13: case 14:
                            if (colNum != 13 && (cell == null || cell.getStringCellValue().equals(""))) {
                                throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
                            }
                            if (cell != null) {
                                String adr = cell.getStringCellValue();
                                if (colNum != 13 && adr.equals("")) {
                                    System.out.println("111");
                                    throw new InvalidException("Lỗi tại hàng: " + (rowNum + 1) + " và cột: " + (colNum + 1));
                                }
                                if (!adr.equals("")) {
                                    String[] listNameOfAdr = adr.split(", ");
                                    if (listNameOfAdr.length != 4) {
                                        System.out.println("222");
                                        throw new InvalidException("Lỗi tại hàng: " + (rowNum + 1) + " và cột: " + (colNum + 1));
                                    }
                                    List<Hamlet> hamlet = hamletRepo.findHamletFromExcel(listNameOfAdr[3], listNameOfAdr[2],
                                            listNameOfAdr[1], listNameOfAdr[0]);
                                    System.out.println("hamlet " + hamlet);
                                    if (hamlet != null && !hamlet.isEmpty()) {
                                        if (colNum == 11) {
                                            provinceCodeOfHometown = hamlet.get(0).getCode().substring(0, 2);
                                        }
                                        Address add = new Address();
                                        add.setHamlet(hamlet.get(0));
                                        add.setAddressType(addressTypes.get(colNum - 11));
                                        add.setCitizen(citizen);
                                        addresses.add(add);
                                    }
                                }
                                if (colNum == 13 && adr.equals("")) {
                                    Address add = new Address();
//                                AddressType addressType = addressTypeRepo.findById(3).orElse(null);
                                    add.setAddressType(addressTypes.get(2));
                                    add.setCitizen(citizen);
                                    addresses.add(add);
                                }
                            }
                            // Lấy ra associatedCitizenNationalId
                        case 15: case 17: case 19: case 21:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                association.setAssociatedCitizenNationalId(cell.getStringCellValue());
                            }break;
                        //lấy ra associatedCitizenName
                        case 16: case 18: case 20: case 22:
                            if (cell != null && !cell.getStringCellValue().equals("")) {
                                association.setAssociatedCitizenName(cell.getStringCellValue());
                                association.setAssociationType(associationTypes.get(associationTypeIndex++));
                                association.setCitizen(citizen);
                            }
                            if (association.getAssociatedCitizenName()!= null && association.getAssociatedCitizenNationalId()!= null) {
                                associations.add(association);
                                System.out.println("Dm quan col: " + (colNum + 1)  +" row: " + (rowNum + 1));
                                System.out.println(association.getAssociatedCitizenName() + association.getAssociatedCitizenNationalId() + association.getCitizen());
                            }
                            association = new Association();
//                            association.setAssociatedCitizenName(null);
//                            association.setAssociatedCitizenNationalId(null);
//                            association.setAssociationType(null);
//                            association.setCitizen(null);
                            break;
                    }
                }

                System.out.println();
                citizen.setAddresses(addresses);
                citizen.setAssociations(associations);
                if (!Utils.validateNationalId(citizen.getNationalId(),citizen.getSex(),provinceCodeOfHometown, citizen.getDateOfBirth())) {
                    throw new InvalidException("Invalid identifier at row " + (rowNum+1)+ " nationalId: " + citizen.getNationalId()
                            + " sex: " + citizen.getSex() + " provinceCode: " + provinceCodeOfHometown + " dob: " + citizen.getDateOfBirth());
                }
                for (Association as :
                        associations) {
                    System.out.println(as.getCitizen() + as.getAssociatedCitizenName() + as.getAssociatedCitizenNationalId() + as.getAssociationType());
                }
                Citizen foundCitizen = repo.findByNationalId(citizen.getNationalId()).orElse(null);
                if (foundCitizen == null) {
                    citizens.add(citizen);
                }
            }
            List<Citizen> list = repo.saveAll(citizens);

            return "Thêm thông tin người dân từ file excel thành công!";
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ByteArrayResource exportDataToExcel(String divisionCode) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        List<Citizen> list = null;
        List<Object[]> listCodeOfSubDivision = admDivisionRepo.getAllCodeOfSubDivision(divisionCode);
        switch (divisionCode.length()) {
            case 2:
                for (Object[] division: listCodeOfSubDivision) {
                    String code = (String) division[0];
                    String name = (String) division[1];
                    list = repo.findAllByDistrictCode(code, 2);
                    Sheet sheet = createSheet(workbook, name, list);
                }
                break;
            case 4:
                for (Object[] division: listCodeOfSubDivision) {
                    String code = (String) division[0];
                    String name = (String) division[1];
                    list = repo.findAllByWardCode(code, 2);
                    Sheet sheet = createSheet(workbook, code, list);
                }
                break;
            case 6:
                for (Object[] division: listCodeOfSubDivision) {
                    String code = (String) division[0];
                    String name = (String) division[1];
                    list = repo.findAllByHamletCode(code, 2);
                    Sheet sheet = createSheet(workbook, code, list);
                }
                break;
            case 8:
                list = repo.findAllByHamletCode(divisionCode, 2);
                Sheet sheet = createSheet(workbook, "Danh sách dân cư", list);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        byte[] bytes = outputStream.toByteArray();
        return new ByteArrayResource(bytes);
    }

    private Sheet createSheet(Workbook workbook, String sheetName, List<Citizen> list){
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(0);
        Row headerRow1 = sheet.createRow(1);


        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        String[] columnNames = {"Số CMND/CCCD", "Họ và tên", "Ngày sinh","Giới tính", "Nhóm máu","Tình trạng hôn nhân",
                "Dân tộc", "Tôn giáo", "Quốc tịch khác", "Trình độ học vấn", "Nghề nghiệp", "Quê quán", "Địa chỉ thường trú"
                , "Địa chỉ tạm trú"};

        for (int i = 0; i < 14; i++) {
            // Gộp các ô dữ liệu của dòng 1 và dòng 2 thành 1
            sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(columnNames[i]);
            headerCell.setCellStyle(headerCellStyle);

        }
        String[] columnNames2 = {"Cha", "Mẹ", "Người giám hộ", "Vợ (hoặc chồng)"};
        for (int i = 0; i < columnNames2.length * 2; i += 2) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i + 14, i + 15));
            Cell headerCell = headerRow.createCell(i + 14);
            headerCell.setCellValue(columnNames2[i / 2]);
            headerCell.setCellStyle(headerCellStyle);
        }
        String[] columnNames3 = {"Số CMND/CCCD", "Họ và tên"};
        for (int i = 0; i < columnNames3.length * 4; i++) {
            Cell headerCell = headerRow1.createCell(i + 14);
            headerCell.setCellValue(columnNames3[i % 2]);
            headerCell.setCellStyle(headerCellStyle);

        }
        int rowIndex = 2;
        for (Citizen c: list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(c.getNationalId());
            row.createCell(1).setCellValue(c.getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Định dạng mới
            String formattedDate = c.getDateOfBirth().format(formatter);
            row.createCell(2).setCellValue(formattedDate);
            row.createCell(3).setCellValue(c.getSex());
            if (c.getBloodType() != null) {
                row.createCell(4).setCellValue(c.getBloodType());
            } else {
                row.createCell(4).setCellValue("");;
            }
            row.createCell(5).setCellValue(c.getMaritalStatus());
            row.createCell(6).setCellValue(c.getEthnicity().getName());
            if (c.getReligion() != null) {
                row.createCell(7).setCellValue(c.getReligion().getName());
            } else {
                row.createCell(7).setCellValue("");;
            }

            if (c.getOtherNationality() != null) {
                row.createCell(8).setCellValue(c.getOtherNationality());
            } else {
                row.createCell(8).setCellValue("");;
            }

            if (c.getEducationalLevel() != null) {
                row.createCell(9).setCellValue(c.getEducationalLevel());
            } else {
                row.createCell(9).setCellValue("");
            }

            if (c.getJob() != null) {
                row.createCell(10).setCellValue(c.getJob());
            } else {
                row.createCell(10).setCellValue("");;
            }
            for (Address address: c.getAddresses()) {
                if (address.getAddressType().getId() == 1) {
                    String fullAddress = address.getHamlet().getName() + ", " + address.getHamlet().getWard().getName()
                            + ", " + address.getHamlet().getWard().getDistrict().getName()
                            + ", " + address.getHamlet().getWard().getDistrict().getProvince().getName();
                    row.createCell(11).setCellValue(fullAddress);
                    break;
                }
            }
            for (Address address: c.getAddresses()) {
                if (address.getAddressType().getId() == 2) {
                    String fullAddress = address.getHamlet().getName() + ", " + address.getHamlet().getWard().getName()
                            + ", " + address.getHamlet().getWard().getDistrict().getName()
                            + ", " + address.getHamlet().getWard().getDistrict().getProvince().getName();
                    row.createCell(12).setCellValue(fullAddress);
                    break;
                }
            }

            for (Address address: c.getAddresses()) {
                if (address.getAddressType().getId() == 3) {
                    if (address.getHamlet() != null) {
                        String fullAddress = address.getHamlet().getName() + ", " + address.getHamlet().getWard().getName()
                                + ", " + address.getHamlet().getWard().getDistrict().getName()
                                + ", " + address.getHamlet().getWard().getDistrict().getProvince().getName();
                        row.createCell(13).setCellValue(fullAddress);
                    } else {
                        row.createCell(13);
                    }
                    break;
                }
            }
            boolean checkAssociation = false;
            for (Association association: c.getAssociations()) {
                if (association.getAssociationType().getId() == 1) {
                    checkAssociation = true;
                    row.createCell(14).setCellValue(association.getAssociatedCitizenNationalId());
                    row.createCell(15).setCellValue(association.getAssociatedCitizenName());
                    break;
                }
            }
            if (!checkAssociation) {
                row.createCell(14);
                row.createCell(15);
            }

            checkAssociation = false;
            for (Association association: c.getAssociations()) {
                if (association.getAssociationType().getId() == 2) {
                    checkAssociation = true;
                    row.createCell(16).setCellValue(association.getAssociatedCitizenNationalId());
                    row.createCell(17).setCellValue(association.getAssociatedCitizenName());
                    break;
                }
            }
            if (!checkAssociation) {
                row.createCell(16);
                row.createCell(17);
            }

            checkAssociation = false;
            for (Association association: c.getAssociations()) {
                if (association.getAssociationType().getId() == 3) {
                    checkAssociation = true;
                    row.createCell(18).setCellValue(association.getAssociatedCitizenNationalId());
                    row.createCell(19).setCellValue(association.getAssociatedCitizenName());
                    break;
                }
            }
            if (!checkAssociation) {
                row.createCell(18);
                row.createCell(19);
            }

            checkAssociation = false;
            for (Association association: c.getAssociations()) {
                if (association.getAssociationType().getId() == 3) {
                    checkAssociation = true;
                    row.createCell(20).setCellValue(association.getAssociatedCitizenNationalId());
                    row.createCell(21).setCellValue(association.getAssociatedCitizenName());
                    break;
                }
            }
            if (!checkAssociation) {
                row.createCell(20);
                row.createCell(21);
            }

        }
        for (int i = 0; i < 22; i++) {
            sheet.autoSizeColumn(i);
        }
        Row headRow = sheet.getRow(0); // Giả sử hàng đầu tiên là cột tiêu đề
        for (int i = 0; i < 14; i++) {
            Cell headerCell = headRow.getCell(i);
            int headerLength = headerCell.getStringCellValue().length();
            int columnWidth = sheet.getColumnWidth(i);
            if (columnWidth < headerLength * 256) {
                sheet.setColumnWidth(i, headerLength * 256);
            }
        }
        return sheet;
    }
}
