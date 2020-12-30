package com.group8.meetingall.config;


import com.itmuch.lightsecurity.enums.HttpMethod;
import com.itmuch.lightsecurity.spec.SpecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LightSecurityConfigurtion {
    @Bean
    public SpecRegistry specRegistry() {
        return new SpecRegistry()
                .add(HttpMethod.ANY, "/user/**", "anon()")
                .add(HttpMethod.ANY, "/test/**", "anon()")
                .add(HttpMethod.ANY, "/error", "anon()")
                .add(HttpMethod.ANY, "/meetingRooms", "anon()")
                .add(HttpMethod.ANY, "/myMeeting/**", "hasLogin()");
    }
}

