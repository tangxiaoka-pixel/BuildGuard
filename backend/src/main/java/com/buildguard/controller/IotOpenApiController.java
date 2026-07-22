package com.buildguard.controller;

import com.buildguard.dto.ApiResponse;
import com.buildguard.entity.*;
import com.buildguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/open/iot")
@RequiredArgsConstructor
public class IotOpenApiController {
    private final IotDeviceRepository iotDevices;
    private final IotOpenApiLogRepository iotLogs;
    private final TowerCraneRepository towerCranes;
    private final TowerCraneDataRepository towerCraneData;
    private final TowerCraneAlarmRepository towerCraneAlarms;

    @PostMapping("/register")
    ApiResponse<Map<String,Object>> register(@RequestBody Map<String,Object> body, jakarta.servlet.http.HttpServletRequest request){
        String code=text(body.get("deviceCode"));if(code.isBlank())code=text(body.get("device_code"));if(code.isBlank())code="IOT-"+System.currentTimeMillis();
        String deviceCode=code;
        IotDevice device=iotDevices.findFirstByDeviceCodeOrderByIdDesc(deviceCode).orElseGet(()->iotDevices.save(IotDevice.builder().deviceCode(deviceCode).deviceName(or(text(body.get("deviceName")),deviceCode)).deviceType(or(text(body.get("deviceType")),"塔吊监测设备")).vendorName(text(body.get("vendorName"))).apiKey("iot-"+UUID.randomUUID().toString().substring(0,8)).apiSecret(UUID.randomUUID().toString().replace("-","")).status("ONLINE").pendingBind(true).lastOnlineTime(LocalDateTime.now()).lastMessageTime(LocalDateTime.now()).lastIp(ip(request)).build()));
        device.setStatus("ONLINE");device.setLastOnlineTime(LocalDateTime.now());device.setLastMessageTime(LocalDateTime.now());device.setLastIp(ip(request));iotDevices.save(device);
        Map<String,Object> result=Map.of("deviceCode",device.getDeviceCode(),"apiKey",device.getApiKey(),"apiSecret",device.getApiSecret(),"pendingBind",device.getPendingBind());
        log(device,"register",body,result,true,ip(request),"注册成功");return ApiResponse.ok(result);
    }

    @PostMapping("/heartbeat")
    ApiResponse<Map<String,Object>> heartbeat(@RequestBody Map<String,Object> body, jakarta.servlet.http.HttpServletRequest request){
        IotDevice device=resolve(body);device.setStatus("ONLINE");device.setLastOnlineTime(LocalDateTime.now());device.setLastMessageTime(LocalDateTime.now());device.setLastIp(ip(request));iotDevices.save(device);
        Map<String,Object> result=Map.of("receivedAt",LocalDateTime.now().toString(),"pendingBind",device.getPendingBind());log(device,"heartbeat",body,result,true,ip(request),"心跳成功");return ApiResponse.ok(result);
    }

    @PostMapping("/tower-crane/data")
    ApiResponse<Map<String,Object>> towerData(@RequestBody Map<String,Object> body, jakarta.servlet.http.HttpServletRequest request){
        IotDevice device=resolve(body);TowerCrane crane=towerCranes.findByIotDeviceId(device.getId()).orElse(null);Long projectId=crane==null?device.getProjectId():crane.getProjectId();
        TowerCraneData data=towerCraneData.save(TowerCraneData.builder().projectId(projectId).towerCraneId(crane==null?null:crane.getId()).iotDeviceId(device.getId()).deviceCode(device.getDeviceCode()).loadWeight(doubleValue(body.get("loadWeight"))).amplitude(doubleValue(body.get("amplitude"))).height(doubleValue(body.get("height"))).rotationAngle(doubleValue(body.get("rotationAngle"))).tiltAngle(doubleValue(body.get("tiltAngle"))).windSpeed(doubleValue(body.get("windSpeed"))).torquePercent(doubleValue(body.get("torquePercent"))).loadPercent(doubleValue(body.get("loadPercent"))).liftingCount(intValue(body.get("liftingCount"),1)).status(status(body)).collectedAt(LocalDateTime.now()).rawData(body.toString()).build());
        device.setStatus("ONLINE");device.setLastMessageTime(LocalDateTime.now());device.setLastOnlineTime(LocalDateTime.now());device.setLastIp(ip(request));iotDevices.save(device);
        if(projectId!=null&&!"NORMAL".equals(data.getStatus()))towerCraneAlarms.save(TowerCraneAlarm.builder().projectId(projectId).towerCraneId(data.getTowerCraneId()).iotDeviceId(device.getId()).deviceCode(device.getDeviceCode()).alarmType("WARN".equals(data.getStatus())?"运行预警":"运行报警").alarmLevel("WARN".equals(data.getStatus())?"一般":"严重").alarmMessage("塔吊实时数据超过安全阈值").handleStatus("UNHANDLED").alarmTime(LocalDateTime.now()).rawData(body.toString()).build());
        Map<String,Object> result=Map.of("dataId",data.getId(),"matchStatus",projectId==null?"DEVICE_UNBOUND":"MATCHED","status",data.getStatus());log(device,"tower-crane/data",body,result,true,ip(request),"数据接收成功");return ApiResponse.ok(result);
    }

    @PostMapping("/tower-crane/alarm")
    ApiResponse<Map<String,Object>> towerAlarm(@RequestBody Map<String,Object> body, jakarta.servlet.http.HttpServletRequest request){
        IotDevice device=resolve(body);TowerCrane crane=towerCranes.findByIotDeviceId(device.getId()).orElse(null);Long projectId=crane==null?device.getProjectId():crane.getProjectId();
        TowerCraneAlarm alarm=towerCraneAlarms.save(TowerCraneAlarm.builder().projectId(projectId).towerCraneId(crane==null?null:crane.getId()).iotDeviceId(device.getId()).deviceCode(device.getDeviceCode()).alarmType(or(text(body.get("alarmType")),"设备预警")).alarmLevel(or(text(body.get("alarmLevel")),"一般")).alarmMessage(or(text(body.get("alarmMessage")),"设备主动上报预警")).handleStatus("UNHANDLED").alarmTime(LocalDateTime.now()).rawData(body.toString()).build());
        Map<String,Object> result=Map.of("alarmId",alarm.getId(),"matchStatus",projectId==null?"DEVICE_UNBOUND":"MATCHED");log(device,"tower-crane/alarm",body,result,true,ip(request),"预警接收成功");return ApiResponse.ok(result);
    }

    private IotDevice resolve(Map<String,Object> body){String code=or(text(body.get("deviceCode")),text(body.get("device_code")));if(code.isBlank())throw new RuntimeException("deviceCode不能为空");return iotDevices.findFirstByDeviceCodeOrderByIdDesc(code).orElseGet(()->iotDevices.save(IotDevice.builder().deviceCode(code).deviceName(code).deviceType("塔吊监测设备").status("UNKNOWN").pendingBind(true).build()));}
    private void log(IotDevice d,String name,Object requestBody,Object responseBody,boolean ok,String ip,String message){iotLogs.save(IotOpenApiLog.builder().projectId(d.getProjectId()).deviceCode(d.getDeviceCode()).interfaceName(name).requestIp(ip).success(ok).message(message).requestBody(String.valueOf(requestBody)).responseBody(String.valueOf(responseBody)).build());}
    private String status(Map<String,Object> b){double wind=Optional.ofNullable(doubleValue(b.get("windSpeed"))).orElse(0d),load=Optional.ofNullable(doubleValue(b.get("loadPercent"))).orElse(0d),torque=Optional.ofNullable(doubleValue(b.get("torquePercent"))).orElse(0d),tilt=Optional.ofNullable(doubleValue(b.get("tiltAngle"))).orElse(0d);if(wind>10||load>95||torque>95||tilt>3)return "ALARM";if(wind>8||load>85||torque>85||tilt>2)return "WARN";return "NORMAL";}
    private Double doubleValue(Object v){try{return v==null||text(v).isBlank()?null:Double.valueOf(text(v));}catch(Exception e){return null;}}
    private Integer intValue(Object v,int d){try{return v==null||text(v).isBlank()?d:Integer.valueOf(text(v));}catch(Exception e){return d;}}
    private String ip(jakarta.servlet.http.HttpServletRequest r){String forwarded=r.getHeader("X-Forwarded-For");return forwarded==null||forwarded.isBlank()?r.getRemoteAddr():forwarded.split(",")[0].trim();}
    private String text(Object value){return value==null?"":String.valueOf(value).trim();}
    private String or(String v,String d){return v==null||v.isBlank()?d:v;}
}
