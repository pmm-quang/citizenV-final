package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.entity.UserRole;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.RoleRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository repository;
    @Autowired
    private AdministrativeDivisionRepository divisionRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder encoder;

    Logger logger = LogManager.getRootLogger();

    @Override
    public List<UserDto> getAll() {
        List<User> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(String userId) {
        User province = repository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        return mapper.map(province, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
//        User user = ((CustomUserDetail)SecurityContextHolder.getContext().getAuthentication()).getUser();
        String newUsername = userDto.getUsername();
        String divisionCode = userDto.getDivision().getCode();
        if (newUsername.equals(divisionCode)) {
            repository.findByUsername(newUsername).ifPresent(user1 -> {
                throw new ResourceFoundException("User", "username",newUsername);
            });
            AdministrativeDivision division = divisionRepo.findByCode(divisionCode).orElseThrow(
                    () -> new ResourceNotFoundException("division", "divisionCode", divisionCode)
            );
            User newUser = new User();
            newUser.setUsername(newUsername);
            newUser.setPassword(encoder.encode(userDto.getPassword()));
            newUser.setDivision(division);
            newUser.setIsActive(true);
            UserRole userRole = new UserRole();
            userRole.setUser(newUser);
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
            userRole.setRole(role);
            newUser.getUserRoles().add(userRole);
            repository.save(newUser);

        }
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }

    public UserDto createUser(Map<String, Object> JSONInfoAsMap) {

        return null;
    }

    public UserDto updateUser(Map<String, Object> JSONInfoAsMap) {

        return null;
    }

    public String deleteById(String userId) {
        return "Deleted";
    }
}
