package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.coherentsolutions.bookingapp.model.entity.AuditLogEntity;

public interface AuditLogRepository extends CrudRepository<AuditLogEntity, Integer> {
}
