package com.hj.springconfig;

import com.hj.AddWaterMarkUtil;
import com.hj.core.AddWaterMark;
import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfig {

    @Bean
    AddWaterMark getAddWaterMark() {
        return new AddWaterMarkUtil();
    }

    @Bean
    public Advisor addWaterMarkAdvisor() {
        return new AddWaterMarkAdvisor(getAddWaterMark());
    }



}
