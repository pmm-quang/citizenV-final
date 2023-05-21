package com.citizenv.app.component;

import com.citizenv.app.service.DeclarationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeclarationScheduler {
    final private DeclarationService service;

    public DeclarationScheduler(DeclarationService service) {
        this.service = service;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void processExpiredDeclarationRights() {

    }
}
