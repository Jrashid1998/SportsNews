package com.incrowd.SportsNews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@SpringBootApplication
public class SportsNewsApplication {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication.run(SportsNewsApplication.class, args);

        DatabaseAccess DBA = new DatabaseAccess();

        ApiPoller poller = new ApiPoller(DBA);
        poller.run();

    }

}

@Configuration
@EnableScheduling
class SchedulingConfiguration {

}
