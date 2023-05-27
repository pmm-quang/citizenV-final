package com.citizenv.app.component;

import com.citizenv.app.entity.Declaration;
import com.citizenv.app.repository.DeclarationRepository;
import com.citizenv.app.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Scheduler {
    private final DeclarationRepository declarationRepo;
    private final UserRepository userRepo;

    public Scheduler(DeclarationRepository declarationRepo, UserRepository userRepo) {
        this.declarationRepo = declarationRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    @Scheduled(cron = "0 27 1 * * ?")
    public void processExpiredDeclarationRights() {
        System.out.println("Starting....");
        List<Declaration> list = declarationRepo.findAll();
//        List<User> users = userRepo.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Khoa khai bao cac user_id: ");
        for (Declaration dec : list) {
            if (dec.getEndTime() != null) {
                LocalDateTime endTime = dec.getEndTime().toLocalDateTime();
                LocalDateTime now = LocalDateTime.now();
                String status = dec.getStatus();
                if (endTime.isBefore(now) && status.equals(Constant.DECLARATION_STATUS_DECLARING)) {
                    dec.setStatus(Constant.DECLARATION_STATUS_OUT_OF_DATE);
                    userRepo.deleteUserRole(dec.getUser().getId(), Constant.EDITOR_ROLE_ID);
                    sb.append(dec.getUser().getId()).append("-");
                    System.out.println("Deleted!");
                }
            }
        }
        System.out.println(sb);
        System.out.println(".....End!!!");
    }
}
