package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.UserRepository;
import com.citizenv.app.service.DeclarationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public String setCompleted(String username) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Tài khoản", "username", username)
        );
        List<Long> roleId = foundUser.getRoles().stream().map(Role::getId).collect(Collectors.toList());

        if (!foundUser.getDeclaration().getStatus().equals(Constant.DECLARATION_STATUS_DECLARING) ||
                !roleId.contains(Constant.EDITOR_ROLE_ID)) {
            throw new InvalidException("Không thể thực hiện hành động này");
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
        return "Đã hoàn thành khai báo!";
    }

    @Transactional
    @Override
    public String openDeclaration(String username, DeclarationDto declaration) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Tài khoản", "username", username)
        );
        if (foundUser.getDeclaration().getStatus().equals(Constant.DECLARATION_STATUS_DECLARING)) {
            throw new InvalidException("Đã mở quyền khai báo, không thể thực hiện hành động này");
        }
        LocalDate startDateDto = declaration.getStartTime();
        LocalDate endDateDto = declaration.getEndTime();
        LocalDateTime startTime = LocalDateTime.of(startDateDto, LocalTime.of(0, 0, 0));
        LocalDateTime endTime  = LocalDateTime.of(endDateDto, LocalTime.of(23, 59, 59));
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now) || (endTime.isBefore(startTime))) {
            throw new InvalidException("Ngày bắt đầu hoặc ngày kết thúc không hợp lệ");
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
        return "Đã cài đặt thời gian khai báo thành công!";
    }

    @Transactional
    @Override
    public String lockDeclaration(String username) {
        User foundUser = userRepo.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException("Tài khoản", "username", username)
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
        return "Đã khóa quyền khai báo thành công!";
    }

}
