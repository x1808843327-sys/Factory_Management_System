//package com.zr.productionplanningservice.config;
//
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
//import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
///**
// * 负载均衡配置类
// * 为调用设备监控服务配置随机负载均衡策略
// */
//@Configuration
//public class LoadBalancerConfiguration {
//
//    /**
//     * 配置随机负载均衡器
//     * 只对 equipment-monitoring-service 生效
//     */
//    @Bean
//    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
//            Environment environment,
//            LoadBalancerClientFactory loadBalancerClientFactory) {
//
//        // 获取服务名称
//        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//
//        // 返回随机负载均衡器
//        return new RandomLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
//                name
//        );
//    }
//}
