package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.Declaration;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.RoleRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.service.DeclarationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeclarationServiceImpl implements DeclarationService {
    private final ModelMapper mapper;
    private final DeclarationRepository repository;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public DeclarationServiceImpl(ModelMapper mapper, DeclarationRepository repository, UserRepository userRepo, RoleRepository roleRepo) {
        this.mapper = mapper;
        this.repository = repository;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public List<DeclarationDto> getAll() {
        List<Declaration> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DeclarationDto.class)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public DeclarationDto createDeclaration(DeclarationDto declaration) {
        String username = "0101";
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );
//        LocalDateTime startTime = declaration.getStartTime();
//        LocalDateTime endTime  = declaration.getEndTime();
        Timestamp startTime = declaration.getStartTime();
        Timestamp endTime = declaration.getEndTime();
        if (startTime.after(endTime)) {
            throw new InvalidException("Ngay mo khai bao phai truoc ngay dong khai bao");
        }
        Declaration declaration1 = mapper.map(declaration, Declaration.class);
        declaration1.setUser(foundUser);
        declaration1.setId(null);
        Declaration newDeclaration = repository.save(declaration1);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (startTime.before(currentTime) && endTime.after(currentTime)) {
            Role role = roleRepo.findById(Utils.EDITOR).orElseThrow(
                    () -> new ResourceNotFoundException("Role", "role", "EDITOR")
            );
            foundUser.getRoles().add(role);
        }
        return mapper.map(newDeclaration, DeclarationDto.class);
    }

    @Override
    public DeclarationDto updateDeclaration(String username, DeclarationDto declaration) {
        return null;
    }

    @Override
    public boolean hasDeclarationRights(String username) {
        return false;
    }

    @Override
    public void lockDeclarationRights(User user) {

    }

    @Override
    public void lockDeclarationRights() {

    }

    @Override
    public void unlockDeclarationRights(User user) {

    }

    @Override
    public void unlockDeclarationRights() {

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void processExpiredDeclarationRights() {
        List<Declaration> list = repository.findAll();
        for (Declaration dec: list) {
            User user = dec.getUser();
            List<Role> roles = user.getRoles();
            List<Long> roleNames = roles.stream().map(Role::getId).collect(Collectors.toList());
            Timestamp startTime = dec.getStartTime();
            Timestamp endTime = dec.getEndTime();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            for (Role r: roles) {
                if (r.getId().equals(Utils.EDITOR) && currentTime.after(endTime)) {
                    userRepo.deleteUserRole(user.getId(), r.getId());
                    user.getRoles().remove(r);
                }
            }
            if (!roleNames.contains(Utils.EDITOR) && currentTime.after(startTime) && currentTime.before(endTime)) {
                Role role = roleRepo.findById(Utils.EDITOR).orElseThrow(
                        () -> new ResourceNotFoundException("Role", "Id","" + Utils.EDITOR)
                );
                user.getRoles().add(role);
            }

        }
    }

}
