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
            try {
                SpringApplication.run(DrsServer.class, args);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Application failed at initial setup phase, this is likely an error in the YAML config file. Exiting");
        }
    }

    private static boolean setup(String[] args) {
        Options options = new DrsServerSpringConfig().getCommandLineOptions();
        ServerPropertySetter setter = new ServerPropertySetter();
        return setter.setServerProperties(DrsServerYamlConfigContainer.class, args, options, "config");
    }
}
