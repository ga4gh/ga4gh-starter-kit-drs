package org.ga4gh.starterkit.drs.app;

import org.ga4gh.starterkit.drs.beanconfig.StarterKitDrsSpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Contains main method for running a standalone DRS deployment as a Spring Boot
 * application
 */
@Configuration
@EnableAutoConfiguration
@Import({
    DrsStandaloneSpringConfig.class,
    StarterKitDrsSpringConfig.class
})
@ComponentScan(basePackages = {
    "org.ga4gh.starterkit.drs.controller",
    "org.ga4gh.starterkit.drs.utils.response.header"
})
public class DrsStandaloneServer {

    /**
     * Run the DRS standalone server as a Spring Boot application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DrsStandaloneServer.class, args);
    }
}
