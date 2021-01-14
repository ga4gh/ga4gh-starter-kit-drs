package org.ga4gh.drs;

import org.ga4gh.drs.configuration.DataSourceRegistry;
import org.ga4gh.drs.configuration.DrsConfig;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.model.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties
public class AppConfig implements WebMvcConfigurer {

    /* ******************************
     * CONFIG BEANS
     * ****************************** */

    @Bean
    @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer defaultDrsConfigContainer() {

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId("org.ga4gh.drs.server");
        serviceInfo.setName("foobar");

        DataSourceRegistry dataSourceRegistry = new DataSourceRegistry();
        
        DrsConfigContainer drsConfigContainer = new DrsConfigContainer(
            new DrsConfig(serviceInfo, dataSourceRegistry));
        return drsConfigContainer;
    }

}