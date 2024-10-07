package pl.coherentsolutions.bookingapp.service.audit.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coherentsolutions.bookingapp.model.entity.AuditLogEntity;
import pl.coherentsolutions.bookingapp.repository.AuditLogRepository;
import pl.coherentsolutions.bookingapp.service.audit.AuditLogService;

@Service
@AllArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public void save(AuditLogEntity auditLogEntity) {
        this.auditLogRepository.save(auditLogEntity);
    }
}
