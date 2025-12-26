package com.zr.equipmentmonitoringservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EquipmentMonitoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipmentMonitoringServiceApplication.class, args);
    }

}
