package com.buildguard.dto;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
public record AttendancePushRequest(
        @NotBlank String deviceNo, String personCode, String idCardNo, String personName,
        LocalDateTime attendanceTime, String direction, String verifyType, Double score) {}
