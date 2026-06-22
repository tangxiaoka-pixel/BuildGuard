package com.buildguard.controller;

import com.buildguard.service.DeviceOpenApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DeviceOpenApiController {
    private final DeviceOpenApiService service;

    @PostMapping({"/open/cwr/Signin", "/CWRService/Signin"})
    Map<String, Object> signin(HttpServletRequest request, @RequestBody(required = false) String body) {
        return service.signin(request, body);
    }

    @PostMapping({"/open/cwr/KeepAlive", "/CWRService/KeepAlive"})
    Map<String, Object> keepAlive(HttpServletRequest request, @RequestBody(required = false) String body) {
        return service.keepAlive(request, body);
    }

    @PostMapping({"/open/cwr/UploadPassedLog", "/CWRService/UploadPassedLog"})
    Map<String, Object> uploadPassedLog(HttpServletRequest request, @RequestBody(required = false) String body) {
        return service.uploadPassedLog(request, body);
    }

    @PostMapping({
            "/open/cwr/QueryEmpListHash", "/CWRService/QueryEmpListHash",
            "/open/cwr/QueryEmployeeList", "/CWRService/QueryEmployeeList",
            "/open/cwr/QueryEmployeeInfo", "/CWRService/QueryEmployeeInfo",
            "/open/cwr/QueryEmployeeIdInfo", "/CWRService/QueryEmployeeIdInfo"
    })
    Map<String, Object> reserved(HttpServletRequest request, @RequestBody(required = false) String body) {
        String uri = request.getRequestURI();
        return service.reserved(request, body, uri.substring(uri.lastIndexOf('/') + 1));
    }
}
