package org.ga4gh.drs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {

        System.out.println("---\n---");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
        System.out.println("---\n---");

        SpringApplication.run(App.class, args);
    }
}
