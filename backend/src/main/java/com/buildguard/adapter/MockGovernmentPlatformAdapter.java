package com.buildguard.adapter;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;
@Component
public class MockGovernmentPlatformAdapter implements GovernmentPlatformAdapter {
    public SubmitResult submit(String businessType, Long businessId) {
        boolean success = ThreadLocalRandom.current().nextInt(10) != 0;
        return new SubmitResult(success, success ? "模拟上报成功" : "模拟接口超时");
    }
}
