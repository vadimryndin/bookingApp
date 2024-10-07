package pl.coherentsolutions.bookingapp.listener;

import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.Map;

@Component
public class ResetMocksTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Service.class);
        for (Object bean : beans.values()) {
            if (Mockito.mockingDetails(bean).isMock()) {
                Mockito.reset(bean);
            }
        }
    }
}
