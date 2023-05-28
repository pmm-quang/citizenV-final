package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.Declaration;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AdministrativeDivisionDto;
import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.excel.ExcelCitizen;
import com.citizenv.app.payload.request.ChangePasswordRequest;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.RoleRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper mapper;
//    @Autowired
    private final UserRepository repository;
//    @Autowired
    private final AdministrativeDivisionRepository divisionRepo;
//    @Autowired
    private final RoleRepository roleRepo;

//    @Autowired
    private final PasswordEncoder encoder;

    private final DeclarationRepository declarationRepo;

    Logger logger = LogManager.getRootLogger();

    public UserServiceImpl(UserRepository repository, AdministrativeDivisionRepository divisionRepo
            , RoleRepository roleRepo, PasswordEncoder encoder, DeclarationRepository declarationRepo) {
        this.repository = repository;
        this.divisionRepo = divisionRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.declarationRepo = declarationRepo;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAll(CustomUserDetail userDetail) {
        String username = userDetail.getUsername();
        User foundUser = repository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );
        List<User> entities = repository.findAllByCreatedBy(foundUser);
        List<UserDto> dtoList = new ArrayList<>();
        for (User user: entities) {
            UserDto userDto = new UserDto();
            DeclarationDto declarationDto = new DeclarationDto();
            userDto.setUsername(user.getUsername());
            AdministrativeDivisionDto divisionDto = mapper.map(user.getDivision(), AdministrativeDivisionDto.class);
            userDto.setDivision(divisionDto);
            declarationDto.setStartTime(user.getDeclaration().getStartTime().toLocalDateTime().toLocalDate());
            declarationDto.setEndTime(user.getDeclaration().getEndTime().toLocalDateTime().toLocalDate());
            declarationDto.setStatus(user.getDeclaration().getStatus());
            userDto.setDeclaration(declarationDto);
            dtoList.add(userDto);
        }
        return dtoList;
    }

    @Override
    public List<UserDto> getByCreatedBy(String usernameUserDetail) {
        User foundUser = repository.findByUsername(usernameUserDetail).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", usernameUserDetail)
        );
        List<User> entities = repository.findAllByCreatedBy(foundUser);
        List<UserDto> dtoList = new ArrayList<>();
        for (User user: entities) {
            UserDto userDto = new UserDto();
            DeclarationDto declarationDto = new DeclarationDto();
            userDto.setUsername(user.getUsername());
            AdministrativeDivisionDto divisionDto = mapper.map(user.getDivision(), AdministrativeDivisionDto.class);
            userDto.setDivision(divisionDto);
            if (user.getDeclaration()!= null) {
                if (user.getDeclaration().getStartTime()!= null) {
                    declarationDto.setStartTime(user.getDeclaration().getStartTime().toLocalDateTime().toLocalDate());
                }
                if (user.getDeclaration().getEndTime()!= null) {
                    declarationDto.setEndTime(user.getDeclaration().getEndTime().toLocalDateTime().toLocalDate());
                }
                if (user.getDeclaration().getStatus()!= null) {
                    declarationDto.setStatus(user.getDeclaration().getStatus());
                }
            }
            userDto.setDeclaration(declarationDto);
            dtoList.add(userDto);
        }
        return dtoList;
    }

    @Override
    public UserDto getById(String userId) {
        User province = repository.findByUsername(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        return mapper.map(province, UserDto.class);
    }

    @Transactional
    @Override
    public String createUser(String usernameUserDetail, String divisionCodeUserDetail, UserDto userDto) {
        boolean checkNull = (userDto.getUsername() == null || userDto.getUsername().equals("")
                || userDto.getPassword() == null || userDto.getPassword().equals("")
                || userDto.getDivision() == null || userDto.getDivision().getCode() == null);

        if (checkNull) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }
        String newUsername = userDto.getUsername();
        AdministrativeDivisionDto divisionOfNewUser = userDto.getDivision();
        String divisionCodeOfNewUser = divisionOfNewUser.getCode();
        long roleId = 0L;
        boolean check = true;
        if (divisionCodeUserDetail == null) {
            check = (divisionCodeOfNewUser.length() == 2);
            roleId = Utils.A2;
        } else {
            switch (divisionCodeUserDetail.length()) {
                case 2: check = (divisionCodeOfNewUser.length() == 4) && (divisionCodeOfNewUser.indexOf(divisionCodeUserDetail) == 0);
                        roleId = Utils.A3;
                        break;
                case 4: check = (divisionCodeOfNewUser.length() == 6) && (divisionCodeOfNewUser.indexOf(divisionCodeUserDetail) == 0);
                        roleId = Utils.B1;
                        break;
                case 6: check = (divisionCodeOfNewUser.length() == 8) && (divisionCodeOfNewUser.indexOf(divisionCodeUserDetail) == 0);
                        roleId = Utils.B2;
                        break;
            }
        }
        if (!check) {
            throw new AccessDeniedException("Không có quyền tạo tài khoản cho đơn vị có mã: " + divisionCodeOfNewUser);
        }
        if (!divisionCodeOfNewUser.equals(newUsername)) {
            throw new InvalidException(Constant.ERR_MESSAGE_USERNAME_AND_UNIT_CODE_DO_NOT_MATCH);
        }
        repository.findByUsername(newUsername).ifPresent(user1 -> {
            throw new ResourceFoundException("Tài khoản", "username",newUsername);
        });
        AdministrativeDivision division = divisionRepo.findByCode(divisionOfNewUser.getCode()).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính", "mã đơn vị", divisionOfNewUser.getCode())
        );

        User userDetail = repository.findByUsername(usernameUserDetail).orElseThrow(
                () -> new ResourceNotFoundException("UserDetail", "username", usernameUserDetail)
        );


        User newUser = new User();
        newUser.setUsername(newUsername);
        newUser.setPassword(encoder.encode(userDto.getPassword()));
        newUser.setDivision(division);
        newUser.setCreatedBy(userDetail);
        newUser.setIsActive(true);

        long finalRoleId = roleId;
        Role role = roleRepo.findById(finalRoleId).orElseThrow(
                () -> new ResourceNotFoundException("Vai trò", "id", String.valueOf(finalRoleId))
        );
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        newUser.setRoles(roles);
        Declaration declaration = new Declaration();

        declaration.setStatus(Constant.DECLARATION_STATUS_NOT_GRANT_YET);
        User createUser = repository.save(newUser);
        createUser.setDeclaration(declaration); // Thiết lập quan hệ giữa User và Declaration
        User savedUser = repository.save(createUser); // Lưu lại đối tượng User để cập nhật quan hệ với Declaration
        declaration.setUser(savedUser); // Thiết lập quan hệ giữa Declaration và User
        declarationRepo.save(declaration);
        return "Tạo mới tài khoản thành công!";

    }


    @Transactional
    @Override
    public String createNewPassword(String userDetailUsername, String username, String newPassword) {
        User foundUser = repository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Tài khoản", "username", username)
        );

        if (!userDetailUsername.equals(username)) {
            String createdBy = foundUser.getCreatedBy().getUsername();
            if (!createdBy.equals(userDetailUsername)) {
                throw new AccessDeniedException(Constant.ACCESS_DENIED_MESSAGE_DO_NOT_HAVE_PERMISSION_TO_CHANGE_PASSWORD);
            }
        }
        String newPasswordEncode = encoder.encode(newPassword);
        foundUser.setPassword(newPasswordEncode);
        UserDto userDto = mapper.map(foundUser, UserDto.class);
        userDto.setPassword(null);
        return "Tạo mới mật khẩu thành công!";
    }

    @Transactional
    @Override
    public String changePassword(String usernameUserDetail, ChangePasswordRequest request) {
        if (request.getNewPassword() == null || request.getOldPassword() == null) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }
        User foundUser = repository.findByUsername(usernameUserDetail).orElseThrow(
                () -> new ResourceNotFoundException("Tài khoản", "username", usernameUserDetail)
        );
        if (!encoder.matches(request.getOldPassword(), foundUser.getPassword())) {
            throw new InvalidException("Mật khẩu cũ không đúng");
        }
        if (!request.getNewPassword().equals(request.getOldPassword())) {
            String passEncode = encoder.encode(request.getNewPassword());
            foundUser.setPassword(passEncode);
        }
        return "Thay đổi mật khẩu thành công!";
    }


}
