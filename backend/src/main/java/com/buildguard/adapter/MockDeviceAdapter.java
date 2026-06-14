package com.buildguard.adapter;

import com.buildguard.entity.Device;
import com.buildguard.entity.Worker;
import org.springframework.stereotype.Component;

@Component
public class MockDeviceAdapter implements DeviceAdapter {
    public SyncResult pushWorkerToDevice(Device device, Worker worker) {
        boolean ok = !"OFFLINE".equals(device.getStatus());
        return new SyncResult(ok, ok ? "模拟设备接收成功" : "设备离线");
    }
    public SyncResult removeWorkerFromDevice(Device device, Worker worker) {
        return new SyncResult(true, "模拟设备权限删除成功");
    }
}
