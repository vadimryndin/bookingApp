package pl.coherentsolutions.bookingapp.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.entity.AuditLogEntity;
import pl.coherentsolutions.bookingapp.service.audit.AuditLogService;

import java.util.Arrays;

@Aspect
@Component
@AllArgsConstructor
public class AuditAspect {
    private final AuditLogService auditLogService;

    @Around("@annotation(pl.coherentsolutions.bookingapp.annotation.Audit)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());

        AuditLogEntity auditLog = new AuditLogEntity();
        auditLog.setMethodName(methodName);
        auditLog.setArguments(arguments);

        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed();
            auditLog.setReturnValue(returnValue != null ? returnValue.toString() : "null");
        } catch (Throwable throwable) {
            auditLog.setError(throwable.getMessage());
            throw throwable;
        } finally {
            auditLogService.save(auditLog);
        }

        return returnValue;
    }
}
