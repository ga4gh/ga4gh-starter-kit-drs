package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.drs.beanconfig.StarterKitDrsSpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({
    DrsStandaloneSpringConfig.class,
    StarterKitDrsSpringConfig.class
})
@ComponentScan(basePackages = {
    "org.ga4gh.starterkit.drs.controller"
})
public class DrsStandaloneServer {

    public static void main(String[] args) {
        SpringApplication.run(DrsStandaloneServer.class, args);
    }
}
