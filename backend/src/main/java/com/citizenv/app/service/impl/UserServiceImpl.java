package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.Declaration;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AdministrativeDivisionDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.RoleRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return entities.stream().map(l-> mapper.map(l, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(String userId) {
        User province = repository.findByUsername(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        return mapper.map(province, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto createUser(CustomUserDetail userDetail, UserDto userDto) {
        AdministrativeDivision divisionOfUser = userDetail.getUser().getDivision();
        String newUsername = userDto.getUsername();
        AdministrativeDivisionDto divisionOfNewUser = userDto.getDivision();
        String divisionCodeOfNewUser = divisionOfNewUser.getCode();
        boolean checkAuth = true;

        if (divisionOfNewUser == null || newUsername == null) {
            throw new InvalidException("Chua nhap du thong tin can thiet");
        }
        if (divisionOfUser == null) {
            checkAuth = (Utils.AdministrativeUnitsLv1.containsKey(divisionOfNewUser.getAdministrativeUnit().getId())
                    && (divisionCodeOfNewUser.length() == 2));

        } else if (divisionOfUser.getCode().length() == 2) {
            checkAuth = ((Utils.AdministrativeUnitsLv2.containsKey(divisionOfNewUser.getAdministrativeUnit().getId()))
                    && (divisionOfNewUser.getCode().indexOf(divisionOfUser.getCode()) == 0)
                    && (divisionCodeOfNewUser.length() == 4));

        } else if (divisionOfUser.getCode().length() == 4) {
            checkAuth = ((Utils.AdministrativeUnitsLv3.containsKey(divisionOfNewUser.getAdministrativeUnit().getId()))
                    && (divisionOfNewUser.getCode().indexOf(divisionOfUser.getCode()) == 0)
                    && (divisionCodeOfNewUser.length() == 6));

        } else if (divisionOfUser.getCode().length() == 6) {
            checkAuth = ((Utils.AdministrativeUnitsLv4.containsKey(divisionOfNewUser.getAdministrativeUnit().getId()))
                    && (divisionOfNewUser.getCode().indexOf(divisionOfUser.getCode()) == 0)
                    && (divisionCodeOfNewUser.length() == 8));
        }
        if (!checkAuth) {
            throw new AccessDeniedException("Khong co quyen tao tai khoan cho don vi co ma : " + divisionOfNewUser.getCode());
        }

        if (newUsername.equals(divisionOfNewUser.getCode())) {
            repository.findByUsername(newUsername).ifPresent(user1 -> {
                throw new ResourceFoundException("User", "username",newUsername);
            });
            AdministrativeDivision division = divisionRepo.findByCode(divisionOfNewUser.getCode()).orElseThrow(
                    () -> new ResourceNotFoundException("division", "divisionCode", divisionOfNewUser.getCode())
            );
            User newUser = new User();
            newUser.setUsername(newUsername);
            newUser.setPassword(encoder.encode(userDto.getPassword()));
            newUser.setDivision(division);
            newUser.setIsActive(true);
            Long roleId = 0L;
            switch (newUsername.length()) {
                case 2: roleId = Utils.A2;break;
                case 4: roleId = Utils.A3; break;
                case 6: roleId = Utils.B1;break;
                case 8: roleId = Utils.B2; break;
            }
            Long finalRoleId = roleId;
            Role role = roleRepo.findById(finalRoleId).orElseThrow(
                    () -> new ResourceNotFoundException("Role", "roleId", String.valueOf(finalRoleId))
            );
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            newUser.setRoles(roles);
            Declaration declaration = new Declaration();
            declaration.setStatus("Chưa");
            User createUser = repository.save(newUser);
            createUser.setDeclaration(declaration); // Thiết lập quan hệ giữa User và Declaration
            User savedUser = repository.save(createUser); // Lưu lại đối tượng User để cập nhật quan hệ với Declaration
            declaration.setUser(savedUser); // Thiết lập quan hệ giữa Declaration và User
            declarationRepo.save(declaration);
            return mapper.map(createUser, UserDto.class);
        } else {
            throw  new InvalidException("username va ma don vi khong trung khop");
        }
    }

    @Override
    public UserDto updateUser(String username, UserDto userDto) {
        return null;
    }

    @Transactional
    @Override
    public UserDto changePassword(String userDetailUsername, String username, String newPassword) {
        User foundUser = repository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );
        if (!userDetailUsername.equals(username)) {
            String createdBy = foundUser.getCreatedBy().getUsername();
            if (!createdBy.equals(userDetailUsername)) {
                throw new AccessDeniedException("Khong co quyen thay doi mat khau tai khoan nay");
            }
        }
        String newPasswordEncode = encoder.encode(newPassword);
        foundUser.setPassword(newPasswordEncode);
        UserDto userDto = mapper.map(foundUser, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

}
