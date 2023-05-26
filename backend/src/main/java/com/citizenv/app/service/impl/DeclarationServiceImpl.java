package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.Declaration;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.service.DeclarationService;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeclarationServiceImpl implements DeclarationService {

    private final ModelMapper mapper;
    private final DeclarationRepository repository;
    private final UserRepository userRepo;

    public DeclarationServiceImpl(ModelMapper mapper, DeclarationRepository repository, UserRepository userRepo) {
        this.mapper = mapper;
        this.repository = repository;
        this.userRepo = userRepo;
    }

    @Override
    public List<DeclarationDto> getAll() {
//        List<Declaration> entities = repository.findAll();
//        List<DeclarationDto> dtos = new ArrayList<>();
//        for (Declaration dec: entities) {
//            DeclarationDto declarationDto = new DeclarationDto();
//            declarationDto.setStatus(dec.getStatus());
//            declarationDto.setEndTime(dec.getEndTime().toLocalDateTime().toLocalDate());
//            declarationDto.setStartTime(dec.getStartTime().toLocalDateTime().toLocalDate());
//        }
//        return entities.stream().map(l-> mapper.map(l, DeclarationDto.class)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<DeclarationDto> getAllByCreatedBy(Long userDetailId) {
        List<Object[]> list = repository.findAllDeclarationByCreatedBy(userDetailId);
        List<DeclarationDto> dtos = new ArrayList<>();
        for (Object[] l: list) {
            DeclarationDto dto = new DeclarationDto();
            if (l[0] != null) {
                dto.setStartTime(LocalDate.parse(l[0].toString()));
            }
            if (l[1] != null) {
                dto.setEndTime(LocalDate.parse(l[1].toString()));
            }
            dto.setStatus(l[2].toString());
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    @Override
    public DeclarationDto createDeclaration(DeclarationDto declaration) {
        return null;
    }

    @Transactional
    @Override
    public DeclarationDto updateDeclaration(String username, DeclarationDto declaration) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );
        String status = declaration.getStatus();
        LocalDate startDateDto = declaration.getStartTime();
        LocalDate endDateDto = declaration.getEndTime();
        LocalDateTime startTime = LocalDateTime.of(startDateDto, LocalTime.of(0, 0, 0));
        LocalDateTime endTime  = LocalDateTime.of(endDateDto, LocalTime.of(23, 59, 59));
        LocalDateTime now = LocalDateTime.now();
        foundUser.getDeclaration().setStartTime(Timestamp.valueOf(startTime));
        foundUser.getDeclaration().setEndTime(Timestamp.valueOf(endTime));
        List<Role> roles = foundUser.getRoles();

        List<Long> roleId = roles.stream().map(Role::getId).collect(Collectors.toList());
        if (!roleId.contains(Utils.EDITOR) && startTime.isBefore(now) && endTime.isAfter(now)) {
            userRepo.insertUserRole(foundUser.getId(), Utils.EDITOR);
            foundUser.getDeclaration().setStatus("Đang khai báo");
        }
        for (Role r: foundUser.getRoles()) {
            if (r.getId().equals(Utils.EDITOR) && (endTime.isBefore(now) || status.equals("Đã hoàn thành"))) {
                userRepo.deleteUserRole(foundUser.getId(), Utils.EDITOR);
                foundUser.getRoles().remove(r);
                if (status.equals("Đã hoàn thành")) {
                    foundUser.getDeclaration().setStatus("Đã hoàn thành");
                } else {
                    foundUser.getDeclaration().setStatus("Không có quyền khai báo");
                }
            }
        }
        DeclarationDto dto = new DeclarationDto();
        dto.setEndTime(declaration.getEndTime());
        dto.setStartTime(declaration.getStartTime());
        dto.setStatus(foundUser.getDeclaration().getStatus());
        return dto;
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
    @Override
    public DeclarationDto setCompleted(String username) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "Username", username)
        );
        List<Long> roleId = foundUser.getRoles().stream().map(Role::getId).collect(Collectors.toList());

        if (!foundUser.getDeclaration().getStatus().equals(Constant.DECLARATION_STATUS_DECLARING) ||
                !roleId.contains(Constant.EDITOR_ROLE_ID)) {
            throw new InvalidException("Unable to perform this action");
        }
        foundUser.getDeclaration().setStatus(Constant.DECLARATION_STATUS_COMPLETED);
        userRepo.deleteUserRole(foundUser.getId(), Constant.EDITOR_ROLE_ID);

        //Khóa quyền khai báo của các hamlet cấp dưới trực thuộc
        List<User> subUserList = userRepo.findAllSubordinateAccounts(foundUser.getUsername());
        if (subUserList!= null) {
            for (User user : userRepo.findAllSubordinateAccounts(foundUser.getUsername())) {
                repository.setDeclarationStatusToCompleteByUserId(user.getId());
                userRepo.deleteUserRole(user.getId(), Constant.EDITOR_ROLE_ID);
            }
        }
//        String districtCode = foundUser.getDivision().getCode().substring(0,4);
        //Kiểm tra các user cấp trên, nếu toàn bộ user cấp dưới đều hoàn thành
        // khai báo thì cấp trên cũng được xét là đã hoàn thành khai báo
        Long createByIdOfUser = foundUser.getCreatedBy().getId();
        Long countSubUserAreDeclaring = userRepo.countSubUserAreDeclaring(createByIdOfUser);
        //đếm nếu tất cả tài khoản cấp dưới của 1 districtUser đều đã hoàn thành khai báo
        if (countSubUserAreDeclaring == null) {
            //set trạng thái cho districtUser đó là đã hoàn thành và khóa quyền khai báo
            repository.setDeclarationStatusToCompleteByUserId(createByIdOfUser);
            userRepo.deleteUserRole(createByIdOfUser, Constant.EDITOR_ROLE_ID);
            createByIdOfUser = userRepo.getCreateByOfUserByUserId(createByIdOfUser).orElse(null);
            if (createByIdOfUser != null) {
                countSubUserAreDeclaring = userRepo.countSubUserAreDeclaring(createByIdOfUser);
                //đếm nếu tất cả tài khoản cấp dưới của 1 provinceUser đều đã hoàn thành khai báo
                if (countSubUserAreDeclaring == null) {
                    //set trạng thái cho provinceUser đó là đã hoàn thành và khóa quyền khai báo
                    repository.setDeclarationStatusToCompleteByUserId(createByIdOfUser);
                    userRepo.deleteUserRole(createByIdOfUser, Constant.EDITOR_ROLE_ID);
                }
            }
        }

        return null;
    }

    @Transactional
    @Override
    public DeclarationDto openDeclaration(String username, DeclarationDto declaration) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "Username", username)
        );
        if (foundUser.getDeclaration().getStatus().equals(Constant.DECLARATION_STATUS_DECLARING)) {
            throw new InvalidException("Account has been granted permission to declare, cannot perform this action");
        }
        LocalDate startDateDto = declaration.getStartTime();
        LocalDate endDateDto = declaration.getEndTime();
        LocalDateTime startTime = LocalDateTime.of(startDateDto, LocalTime.of(0, 0, 0));
        LocalDateTime endTime  = LocalDateTime.of(endDateDto, LocalTime.of(23, 59, 59));
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now) || (endTime.isBefore(startTime))) {
            throw new InvalidException("The start and end dates are incorrect");
        }
        foundUser.getDeclaration().setStartTime(Timestamp.valueOf(startTime));
        foundUser.getDeclaration().setEndTime(Timestamp.valueOf(endTime));
        declaration.setStatus(Constant.DECLARATION_STATUS_NOT_OPEN);
        if (startTime.isBefore(now) && endTime.isAfter(now)) {
            userRepo.insertUserRole(foundUser.getId(), Constant.EDITOR_ROLE_ID);
            foundUser.getDeclaration().setStatus(Constant.DECLARATION_STATUS_DECLARING);
            declaration.setStatus(Constant.DECLARATION_STATUS_DECLARING);
        } else {
            foundUser.getDeclaration().setStatus(Constant.DECLARATION_STATUS_NOT_OPEN);
        }
        return declaration;
    }

    @Transactional
    @Override
    public void lockDeclaration(String username) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException("User", "Username", username)
        );
        foundUser.getDeclaration().setStatus(Constant.DECLARATION_STATUS_LOCKED);
        userRepo.deleteUserRole(foundUser.getId(), Constant.EDITOR_ROLE_ID);
        List<User> subUserList = userRepo.findAllSubordinateAccounts(username);

        if (subUserList != null) {
            for (User user : subUserList) {
                user.getDeclaration().setStatus(Constant.DECLARATION_STATUS_LOCKED);
                userRepo.deleteUserRole(user.getId(), Constant.EDITOR_ROLE_ID);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void processExpiredDeclarationRights() {
        List<Declaration> list = repository.findAll();
//        List<User> users = userRepo.findAll();

        for (Declaration dec: list) {
            LocalDateTime endTime  = dec.getEndTime().toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            String status = dec.getStatus();
            if (endTime.isBefore(now) && status.equals(Constant.DECLARATION_STATUS_DECLARING)) {
                dec.setStatus(Constant.DECLARATION_STATUS_OUT_OF_DATE);
                userRepo.deleteUserRole(dec.getUser().getId(), Constant.EDITOR_ROLE_ID);
            }
        }
//        for (Declaration dec: list) {
//            User user = dec.getUser();
//            List<Role> roles = user.getRoles();
//            List<Long> roleNames = roles.stream().map(Role::getId).collect(Collectors.toList());
//            Timestamp startTime = dec.getStartTime();
//            Timestamp endTime = dec.getEndTime();
//            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//            for (Role r: roles) {
//                if (r.getId().equals(Utils.EDITOR) && currentTime.after(endTime)) {
//                    userRepo.deleteUserRole(user.getId(), r.getId());
//                    user.getRoles().remove(r);
//                    if (!dec.getStatus().equals(Constant.DECLARATION_STATUS_COMPLETED)) {
//                        dec.setStatus("Đã khóa khai báo");
//                    }
//                }
//            }
//            if (!roleNames.contains(Utils.EDITOR) && currentTime.after(startTime) && currentTime.before(endTime)) {
//                userRepo.insertUserRole(user.getId(), Utils.EDITOR);
//                dec.setStatus(Constant.DECLARATION_STATUS_DECLARING);
//            }
//
//        }
    }

}
