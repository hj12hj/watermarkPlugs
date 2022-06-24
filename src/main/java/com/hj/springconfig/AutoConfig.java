package com.hj.springconfig;

import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfig {

    @Bean
    public Advisor addWaterMarkAdvisor(){
        return new AddWaterMarkAdvisor();
    }

}
