package org.ga4gh.starterkit.drs.app;

import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.util.webserver.ServerPropertySetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Contains main method for running a standalone DRS deployment as a Spring Boot
 * application
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.ga4gh.starterkit.drs")
public class DrsServer {

    /**
     * Run the DRS standalone server as a Spring Boot application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        boolean setupSuccess = setup(args);
        if (setupSuccess) {
            SpringApplication.run(DrsServer.class, args);
        } else {
            System.out.println("Setup failure, exiting");
        }
    }

    private static boolean setup(String[] args) {
        Options options = new DrsServerSpringConfig().getCommandLineOptions();
        return ServerPropertySetter.setServerProperties(DrsServerYamlConfigContainer.class, args, options, "config");
    }
}
