package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.payload.excel.ExcelCitizen;
import com.citizenv.app.repository.*;
import com.citizenv.app.service.ExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public ExcelServiceImpl(CitizenRepository repo, AddressRepository addressRepo, AssociationRepository associationRepo,
                            AddressTypeRepository addressTypeRepo, AssociationTypeRepository associationTypeRepo,
                            HamletRepository hamletRepo, EthnicityRepository ethnicityRepo, ReligionRepository religionRepo) {
        this.repo = repo;
        this.addressRepo = addressRepo;
        this.associationRepo = associationRepo;
        this.addressTypeRepo = addressTypeRepo;
        this.associationTypeRepo = associationTypeRepo;
        this.hamletRepo = hamletRepo;
        this.ethnicityRepo = ethnicityRepo;
        this.religionRepo = religionRepo;
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
            StringBuilder RowInvalid = new StringBuilder();
            String provinceCodeOfHometown = null;
            List<ExcelCitizen> excelModels = new ArrayList<>();
            for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
                Citizen citizen = new Citizen();
                ExcelCitizen model = new ExcelCitizen();
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
                            model.setName(cell.getStringCellValue());
//                            if (!Utils.validateName(model.getName())) {
//                                listRowInvalid.append(rowNum);
//                                break;
//                            }
                            citizen.setName(Utils.standardizeName(cell.getStringCellValue()));
                            break;
                        case 3:
                            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate date = LocalDate.parse(cell.getStringCellValue(), inputFormatter);

                            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = date.format(outputFormatter);
                            model.setDateOfBirth(date);
                            citizen.setDateOfBirth(date);
                            break;
                        case 4:
                            model.setSex(cell.getStringCellValue());
                            if (!Utils.validateSex(model.getSex())) {
                                RowInvalid.append(rowNum);
                                throw new InvalidException("Lỗi trên cột 'Giới tính' tại hàng: " + (rowNum +1) + " và cột: " + (colNum + 1));
                            } else {
                                citizen.setSex(cell.getStringCellValue());
                            }
                            break;
                        case 5:
                            model.setBloodType(cell.getStringCellValue());
                            try {
                                Utils.BloodType.valueOf(cell.getStringCellValue());
                                citizen.setBloodType(cell.getStringCellValue());
                            } catch (Exception e) {
                                throw new InvalidException("Lỗi trên cột 'Nhóm máu' tại hàng: " + (rowNum +1) + " và cột: " + (colNum + 1));
                            }
                            break;
                        case 6:
                            model.setMaritalStatus(cell.getStringCellValue());
                            citizen.setMaritalStatus(cell.getStringCellValue());
                            break;
                        case 7:
                            model.setEthnicity(cell.getStringCellValue());
                            int finalRowNum = rowNum;
                            int finalColNum = colNum;
                            Ethnicity ethnicity = ethnicityRepo.findByName(cell.getStringCellValue()).orElseThrow(
                                    () -> new InvalidException("Lỗi trên cột 'Dân tộc' tại hàng: " + (finalRowNum+1) + " và cột: " + (finalColNum + 1))
                            );
                            citizen.setEthnicity(ethnicity);
                            break;
                        case 8:
                            model.setReligion(cell.getStringCellValue());
                            if (!cell.getStringCellValue().equals("")) {
                                finalRowNum = rowNum;
                                finalColNum = colNum;
                                Religion religion = religionRepo.findByName(cell.getStringCellValue()).orElseThrow(
                                        () -> new InvalidException("Lỗi trên cột 'Tôn giáo' tại hàng: " + (finalRowNum+1) + " và cột: " + (finalColNum + 1))
                                );
                                citizen.setReligion(religion);
                            }
                            break;
                        case 9:
                            model.setOtherNationality(cell.getStringCellValue());
                            if (!cell.getStringCellValue().equals("")) {
                                citizen.setOtherNationality(cell.getStringCellValue());
                            }
                            break;
                        case 10:
                            model.setEducationalLevel(cell.getStringCellValue());
                            citizen.setEducationalLevel(cell.getStringCellValue());
                            break;
                        case 11:
                            model.setJob(cell.getStringCellValue());
                            citizen.setJob(cell.getStringCellValue());
                            break;
                        case 12: case 13: case 14:
                            String adr = cell.getStringCellValue();
                            if (colNum != 13 && adr.equals("")) {
                                throw new InvalidException("Lỗi tại hàng: " + (rowNum +1) + " và cột: " + (colNum + 1));
                            }
                            if (!adr.equals("")) {
                                String[] listNameOfAdr = adr.split(" - ");
                                if (listNameOfAdr.length != 4) {
                                    throw new InvalidException("Lỗi tại hàng: " + (rowNum +1) + " và cột: " + (colNum + 1));
                                }
                                List<Hamlet> hamlet = hamletRepo.findHamletFromExcel(listNameOfAdr[3], listNameOfAdr[2],
                                        listNameOfAdr[1], listNameOfAdr[0]);

                                if (hamlet != null) {
                                    if (colNum == 11) {
                                        provinceCodeOfHometown = hamlet.get(0).getCode().substring(0, 2);
                                    }
                                    Address add = new Address();
                                    add.setHamlet(hamlet.get(0));
                                    add.setAddressType(addressTypes.get(colNum-11));
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
                            // Lấy ra associatedCitizenNationalId
                        case 15: case 17: case 19: case 21:
                            if (!cell.getStringCellValue().equals("")) {
                                association.setAssociatedCitizenNationalId(cell.getStringCellValue());
                            }break;
                        //lấy ra associatedCitizenName
                        case 16: case 18: case 20: case 22:
                            if (!cell.getStringCellValue().equals("")) {
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
                    System.out.print(cell.getStringCellValue()+" ");
                }
                excelModels.add(model);
                System.out.println();
                citizen.setAddresses(addresses);
                citizen.setAssociations(associations);
                if (!Utils.validateNationalId(citizen.getNationalId(),citizen.getSex(),provinceCodeOfHometown, citizen.getDateOfBirth())) {
                    throw new InvalidException("Invalid identifier at row " + (rowNum+1));
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
}
