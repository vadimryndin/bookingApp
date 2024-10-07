package pl.coherentsolutions.bookingapp.service.audit;

import pl.coherentsolutions.bookingapp.model.entity.AuditLogEntity;

public interface AuditLogService {
    void save(AuditLogEntity auditLogEntity);
}
