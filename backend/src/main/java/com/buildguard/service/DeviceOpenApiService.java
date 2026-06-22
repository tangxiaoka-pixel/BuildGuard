package com.buildguard.service;

import com.buildguard.entity.*;
import com.buildguard.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DeviceOpenApiService {
    private static final DateTimeFormatter SECOND_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String PLATFORM = "HUMAN_SOCIAL_PLATFORM";
    private final OpenApiClientRepository clients;
    private final DeviceRepository devices;
    private final ProjectRepository projects;
    private final ProjectAreaRepository areas;
    private final ParticipantUnitRepository units;
    private final TeamRepository teams;
    private final WorkerRepository workers;
    private final AttendanceRecordRepository attendance;
    private final ExternalWorkerMappingRepository mappings;
    private final DeviceOpenApiLogRepository logs;
    private final ObjectMapper mapper;

    @Transactional
    public Map<String, Object> signin(HttpServletRequest request, String body) {
        return handle(request, body, "Signin", () -> {
            Query query = query(request);
            Device device = resolveDevice(query, requestIp(request));
            device.setLastSigninTime(LocalDateTime.now());
            touchDevice(device, requestIp(request), false);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("server_timestamp", SECOND_FORMAT.format(LocalDateTime.now()));
            data.put("emp_syncflag", "N");
            data.put("device_status", device.getPendingConfirm() ? "PENDING_CONFIRM" : device.getStatus());
            data.put("project_info", projectInfo(device));
            return ok(data);
        });
    }

    @Transactional
    public Map<String, Object> keepAlive(HttpServletRequest request, String body) {
        return handle(request, body, "KeepAlive", () -> {
            Device device = resolveDevice(query(request), requestIp(request));
            device.setLastHeartbeatTime(LocalDateTime.now());
            touchDevice(device, requestIp(request), false);
            return ok(Map.of("server_timestamp", SECOND_FORMAT.format(LocalDateTime.now())));
        });
    }

    @Transactional
    public Map<String, Object> uploadPassedLog(HttpServletRequest request, String body) {
        return handle(request, body, "UploadPassedLog", () -> {
            Query q = query(request);
            Device device = resolveDevice(q, requestIp(request));
            device.setLastAttendanceTime(LocalDateTime.now());
            touchDevice(device, requestIp(request), false);
            List<Map<String, Object>> rows = normalizeAttendanceBody(body);
            if (rows.isEmpty()) throw new OpenApiException("40", "考勤数据不能为空");
            List<Map<String, Object>> saved = new ArrayList<>();
            for (Map<String, Object> row : rows) saved.add(saveAttendance(device, row, body));
            return ok(Map.of("accepted_count", saved.size(), "records", saved));
        });
    }

    @Transactional
    public Map<String, Object> reserved(HttpServletRequest request, String body, String interfaceName) {
        return logAndReturn(request, body, interfaceName, error("501", "接口已预留，第一期暂不开放"));
    }

    private Map<String, Object> handle(HttpServletRequest request, String body, String interfaceName, Handler handler) {
        Map<String, Object> response;
        try {
            verify(request, body == null ? "" : body);
            response = handler.run();
        } catch (OpenApiException e) {
            response = error(e.code, e.getMessage());
        } catch (Exception e) {
            response = error("43", e.getMessage() == null ? "接口处理失败" : e.getMessage());
        }
        return logAndReturn(request, body, interfaceName, response);
    }

    private Map<String, Object> logAndReturn(HttpServletRequest request, String body, String interfaceName, Map<String, Object> response) {
        Query q = query(request);
        logs.save(DeviceOpenApiLog.builder()
                .deviceNo(q.clientSerial())
                .apiKey(q.apiKey())
                .interfaceName(interfaceName)
                .requestQuery(request.getQueryString())
                .requestBody(body)
                .responseBody(toJson(response))
                .success("00".equals(String.valueOf(response.get("code"))))
                .errorCode(String.valueOf(response.get("code")))
                .errorMessage(String.valueOf(response.get("message")))
                .requestIp(requestIp(request))
                .build());
        return response;
    }

    private void verify(HttpServletRequest request, String body) {
        Query q = query(request);
        if (blank(q.apiKey()) || blank(q.clientSerial()) || blank(q.signature())) throw new OpenApiException("24", "缺少签名参数");
        if (blank(q.timestamp())) throw new OpenApiException("30", "缺少 timestamp");
        if (blank(q.apiVersion())) throw new OpenApiException("32", "缺少 api_version");
        OpenApiClient client = clients.findByApiKey(q.apiKey()).orElseThrow(() -> new OpenApiException("29", "无效 api_key"));
        if (!"ACTIVE".equals(client.getStatus())) throw new OpenApiException("29", "接入账号已停用");
        LocalDateTime clientTime;
        try {
            clientTime = LocalDateTime.parse(q.timestamp(), SECOND_FORMAT);
        } catch (Exception e) {
            throw new OpenApiException("31", "timestamp 格式应为 yyyy-MM-dd HH:mm:ss");
        }
        if (Math.abs(Duration.between(clientTime, LocalDateTime.now()).toMinutes()) > 10) throw new OpenApiException("31", "timestamp 超出允许时间范围");
        String sign = sign(client.getApiSecret(), q, body == null ? "" : body);
        if (!sign.equalsIgnoreCase(q.signature())) throw new OpenApiException("25", "签名错误");
    }

    private String sign(String secret, Query q, String body) {
        String source = secret + "api_key" + q.apiKey() + "api_version" + q.apiVersion() + "body" + body + "client_serial" + q.clientSerial() + "timestamp" + q.timestamp() + secret;
        return md5(source).toUpperCase(Locale.ROOT);
    }

    private Device resolveDevice(Query q, String ip) {
        OpenApiClient client = clients.findByApiKey(q.apiKey()).orElse(null);
        return devices.findByDeviceNo(q.clientSerial()).map(device -> {
            if (blank(device.getApiKey())) device.setApiKey(q.apiKey());
            if (blank(device.getVendorName()) && client != null) device.setVendorName(client.getVendorName());
            device.setLastIp(ip);
            return devices.save(device);
        }).orElseGet(() -> devices.save(Device.builder()
                .deviceNo(q.clientSerial())
                .deviceName("待确认设备-" + q.clientSerial())
                .vendorName(client == null ? null : client.getVendorName())
                .deviceType("人脸考勤机")
                .apiKey(q.apiKey())
                .status("PENDING_CONFIRM")
                .pendingConfirm(true)
                .lastIp(ip)
                .build()));
    }

    private void touchDevice(Device device, String ip, boolean attendanceTime) {
        device.setLastIp(ip);
        if (!"DISABLED".equals(device.getStatus())) device.setStatus(Boolean.TRUE.equals(device.getPendingConfirm()) ? "PENDING_CONFIRM" : "ONLINE");
        device.setLastOnlineTime(LocalDateTime.now());
        if (attendanceTime) device.setLastAttendanceTime(LocalDateTime.now());
        devices.save(device);
    }

    private Map<String, Object> saveAttendance(Device device, Map<String, Object> row, String rawBody) {
        String name = first(row, "emp_name", "person_name", "name");
        String externalEmpId = first(row, "emp_id", "person_id", "employee_id");
        String idCardNo = first(row, "id_code", "idCardNo", "id_card_no");
        String direction = normalizeDirection(first(row, "pass_direction", "direction"));
        String verifyType = first(row, "verify_type", "way");
        LocalDateTime passTime = parseTime(first(row, "pass_time", "passed_time", "attendance_time"));
        Double score = number(first(row, "score"));
        ProjectArea area = device.getAreaId() == null ? null : areas.findById(device.getAreaId()).orElse(null);
        Worker worker = null;
        String matchStatus = "UNMATCHED";
        String abnormalReason = null;

        if (device.getProjectId() == null) {
            matchStatus = "DEVICE_UNBOUND";
            abnormalReason = "设备未绑定项目";
        } else {
            worker = matchWorker(device.getProjectId(), externalEmpId, idCardNo, name, first(row, "emp_company"), first(row, "work_typename"));
            if (worker == null && !blank(idCardNo) && !blank(name)) {
                worker = autoCreateWorker(device.getProjectId(), externalEmpId, idCardNo, name, row);
                matchStatus = "AUTO_CREATED";
            } else if (worker != null) {
                matchStatus = "MATCHED";
                upsertMapping(device, worker, externalEmpId, idCardNo);
            } else {
                abnormalReason = "无法匹配实名制人员";
            }
        }

        AttendanceRecord record = attendance.save(AttendanceRecord.builder()
                .workerId(worker == null ? null : worker.getId())
                .workerName(worker == null ? name : worker.getName())
                .projectId(device.getProjectId())
                .areaId(device.getAreaId())
                .deviceId(device.getId())
                .deviceName(device.getDeviceName())
                .areaName(area == null ? "未绑定区域" : area.getAreaName())
                .direction(direction)
                .verifyType(blank(verifyType) ? "DEVICE" : verifyType)
                .score(score)
                .attendanceTime(passTime == null ? LocalDateTime.now() : passTime)
                .accessResult("DEVICE_UNBOUND".equals(matchStatus) || "UNMATCHED".equals(matchStatus) ? "DENY" : "ALLOW")
                .denyReason(abnormalReason)
                .reportStatus("PENDING")
                .externalEmpId(externalEmpId)
                .matchStatus(matchStatus)
                .rawData(toJson(row))
                .abnormal(!"MATCHED".equals(matchStatus) && !"AUTO_CREATED".equals(matchStatus))
                .abnormalReason(abnormalReason)
                .build());
        if (worker != null) {
            worker.setLastAttendanceTime(record.getAttendanceTime());
            workers.save(worker);
        }
        device.setLastAttendanceTime(record.getAttendanceTime());
        devices.save(device);
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("attendanceId", record.getId());
        view.put("workerId", record.getWorkerId());
        view.put("matchStatus", matchStatus);
        view.put("abnormalReason", abnormalReason);
        return view;
    }

    private Worker matchWorker(Long projectId, String externalEmpId, String idCardNo, String name, String company, String workType) {
        if (!blank(idCardNo)) {
            Optional<Worker> byCard = workers.findByIdCardNo(idCardNo).filter(w -> Objects.equals(w.getProjectId(), projectId));
            if (byCard.isPresent()) return byCard.get();
        }
        if (!blank(externalEmpId)) {
            Optional<Worker> byExternal = workers.findByProjectIdAndExternalEmpId(projectId, externalEmpId);
            if (byExternal.isPresent()) return byExternal.get();
            Optional<ExternalWorkerMapping> mapping = mappings.findByProjectIdAndExternalEmpId(projectId, externalEmpId);
            if (mapping.isPresent()) return workers.findById(mapping.get().getWorkerId()).orElse(null);
        }
        if (!blank(name)) {
            List<Worker> sameName = workers.findByProjectIdAndName(projectId, name);
            if (!blank(workType)) sameName = sameName.stream().filter(w -> workType.equals(w.getWorkType())).toList();
            if (!blank(company)) {
                Set<Long> unitIds = units.findByProjectId(projectId).stream().filter(u -> company.equals(u.getUnitName())).map(ParticipantUnit::getId).collect(java.util.stream.Collectors.toSet());
                if (!unitIds.isEmpty()) sameName = sameName.stream().filter(w -> unitIds.contains(w.getParticipantUnitId())).toList();
            }
            if (sameName.size() == 1) return sameName.get(0);
        }
        return null;
    }

    private Worker autoCreateWorker(Long projectId, String externalEmpId, String idCardNo, String name, Map<String, Object> row) {
        Worker worker = Worker.builder()
                .name(name)
                .idCardNo(idCardNo)
                .phone(first(row, "phone", "mobile"))
                .gender(first(row, "gender"))
                .workType(first(row, "work_typename", "workType"))
                .projectId(projectId)
                .participantUnitId(matchUnit(projectId, first(row, "emp_company")))
                .teamId(matchTeam(projectId, first(row, "team_name")))
                .personCode("EXT" + System.currentTimeMillis())
                .workerType("劳务工人")
                .status("PENDING_CONFIRM")
                .safetyEducationStatus("FAILED")
                .accessStatus("ACCESS_DISABLED")
                .deviceSyncStatus("NOT_SYNCED")
                .qrToken(UUID.randomUUID().toString().replace("-", ""))
                .realNameReportStatus("NOT_SUBMITTED")
                .source("DEVICE_SYNC")
                .externalPlatform(PLATFORM)
                .externalEmpId(externalEmpId)
                .autoCreated(true)
                .confirmed(false)
                .build();
        Worker saved = workers.save(worker);
        upsertMapping(devices.findAll().stream().filter(d -> Objects.equals(d.getProjectId(), projectId)).findFirst().orElse(null), saved, externalEmpId, idCardNo);
        return saved;
    }

    private void upsertMapping(Device device, Worker worker, String externalEmpId, String idCardNo) {
        if (blank(externalEmpId) || worker.getProjectId() == null) return;
        ExternalWorkerMapping mapping = mappings.findByProjectIdAndExternalEmpId(worker.getProjectId(), externalEmpId).orElseGet(ExternalWorkerMapping::new);
        mapping.setProjectId(worker.getProjectId());
        mapping.setPlatformType(PLATFORM);
        mapping.setExternalEmpId(externalEmpId);
        mapping.setWorkerId(worker.getId());
        mapping.setIdCardNo(idCardNo);
        mapping.setWorkerName(worker.getName());
        mapping.setDeviceNo(device == null ? null : device.getDeviceNo());
        mapping.setSource("DEVICE_SYNC");
        mappings.save(mapping);
    }

    private Long matchUnit(Long projectId, String unitName) {
        if (blank(unitName)) return null;
        return units.findByProjectId(projectId).stream().filter(u -> unitName.equals(u.getUnitName())).map(ParticipantUnit::getId).findFirst().orElse(null);
    }

    private Long matchTeam(Long projectId, String teamName) {
        if (blank(teamName)) return null;
        return teams.findByProjectId(projectId).stream().filter(t -> teamName.equals(t.getTeamName())).map(Team::getId).findFirst().orElse(null);
    }

    private List<Map<String, Object>> normalizeAttendanceBody(String body) {
        Map<String, Object> payload = parseMap(body);
        Object list = payload.get("passedlog_list");
        if (list instanceof List<?> rows) return rows.stream().filter(Map.class::isInstance).map(x -> (Map<String, Object>) x).toList();
        return List.of(payload);
    }

    private Map<String, Object> projectInfo(Device device) {
        Map<String, Object> info = new LinkedHashMap<>();
        if (device.getProjectId() == null) return info;
        projects.findById(device.getProjectId()).ifPresent(p -> {
            info.put("project_id", p.getId());
            info.put("project_name", p.getProjectName());
            info.put("project_address", p.getAddress());
            info.put("manager_name", p.getManagerName());
        });
        info.put("area_name", device.getAreaId() == null ? null : areas.findById(device.getAreaId()).map(ProjectArea::getAreaName).orElse(null));
        info.put("work_typename", teams.findByProjectId(device.getProjectId()).stream().map(Team::getWorkType).filter(s -> !blank(s)).distinct().toList());
        return info;
    }

    private Query query(HttpServletRequest request) {
        return new Query(
                request.getParameter("api_key"),
                request.getParameter("api_version"),
                request.getParameter("client_serial"),
                request.getParameter("timestamp"),
                request.getParameter("signature")
        );
    }

    private Map<String, Object> parseMap(String body) {
        if (blank(body)) return new LinkedHashMap<>();
        try {
            return mapper.readValue(body, new TypeReference<LinkedHashMap<String, Object>>() {});
        } catch (Exception e) {
            throw new OpenApiException("47", "JSON 报文解析失败");
        }
    }

    private String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    private String first(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            Object value = row.get(key);
            if (value != null && !String.valueOf(value).isBlank()) return String.valueOf(value);
        }
        return null;
    }

    private String normalizeDirection(String value) {
        if (blank(value)) return "IN";
        String v = value.trim().toLowerCase(Locale.ROOT);
        if (Set.of("out", "出", "出场", "下班", "2").contains(v)) return "OUT";
        return "IN";
    }

    private LocalDateTime parseTime(String value) {
        if (blank(value)) return null;
        try {
            return LocalDateTime.parse(value, SECOND_FORMAT);
        } catch (Exception ignored) {
            try {
                return LocalDateTime.parse(value);
            } catch (Exception e) {
                throw new OpenApiException("41", "考勤时间格式错误");
            }
        }
    }

    private Double number(String value) {
        if (blank(value)) return null;
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String requestIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (!blank(forwarded)) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) builder.append(String.format("%02x", b));
            return builder.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, Object> ok(Object resultData) {
        Map<String, Object> response = base("true", 200, "00", "");
        response.put("result_data", resultData == null ? Map.of() : resultData);
        return response;
    }

    private Map<String, Object> error(String code, String message) {
        Map<String, Object> response = base("false", 200, code, message);
        response.put("result_data", Map.of());
        return response;
    }

    private Map<String, Object> base(String result, int status, String code, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", result);
        response.put("status", status);
        response.put("code", code);
        response.put("message", message);
        response.put("detail_message", message);
        return response;
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private record Query(String apiKey, String apiVersion, String clientSerial, String timestamp, String signature) {}
    private interface Handler { Map<String, Object> run(); }
    private static class OpenApiException extends RuntimeException {
        private final String code;
        private OpenApiException(String code, String message) { super(message); this.code = code; }
    }
}
