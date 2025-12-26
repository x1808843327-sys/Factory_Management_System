package com.zr.productionplanningservice;

//import com.zr.productionplanningservice.config.LoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

@SpringBootApplication
//@EnableDiscoveryClient
//@LoadBalancerClient(
//        name = "equipment-monitoring-service",
//        configuration = LoadBalancerConfiguration.class
//)
public class ProductionPlanningServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionPlanningServiceApplication.class, args);
    }

}
