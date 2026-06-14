package com.buildguard.adapter;
public interface GovernmentPlatformAdapter {
    SubmitResult submit(String businessType, Long businessId);

    record SubmitResult(boolean success, String message) {}
}
