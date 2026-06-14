package com.buildguard.adapter;

import com.buildguard.entity.Device;
import com.buildguard.entity.Worker;

public interface DeviceAdapter {
    SyncResult pushWorkerToDevice(Device device, Worker worker);
    SyncResult removeWorkerFromDevice(Device device, Worker worker);

    record SyncResult(boolean success, String message) {}
}
