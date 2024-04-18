package com.sd.emsperson.external.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalDeviceService {
    private final DeviceRestClient deviceRestClient;

    public HttpStatus deleteDevice(UUID clientId) {
        try{return deviceRestClient.deleteDevice(clientId);
        }
        catch(Exception e){
            log.error("Error at deleteDevice: " + e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
