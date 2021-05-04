package org.ga4gh.starterkit.drs.app;

import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import java.io.Serializable;
import java.util.List;

@Configuration
@ConfigurationProperties
public class DrsStandaloneSpringConfig implements WebMvcConfigurer {

    /* ******************************
     * DRS SERVER CONFIG BEANS
     * ****************************** */

    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to DRS YAML config file");
        return options;
    }

    @Bean
    @Scope(DrsStandaloneConstants.PROTOTYPE)
    @Qualifier(DrsStandaloneConstants.EMPTY_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer emptyDrsConfigContainer() {
        return new DrsStandaloneYamlConfigContainer(new DrsStandaloneYamlConfig());
    }

    @Bean
    @Qualifier(DrsStandaloneConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer defaultDrsConfigContainer() {
        return new DrsStandaloneYamlConfigContainer(new DrsStandaloneYamlConfig());
    }

    @Bean
    @Qualifier(DrsStandaloneConstants.USER_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer runtimeDrsConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(DrsStandaloneConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        DrsStandaloneYamlConfigContainer userConfigContainer = CliYamlConfigLoader.load(DrsStandaloneYamlConfigContainer.class, args, options, "config");
        if (userConfigContainer != null) {
            return userConfigContainer;
        }
        return drsConfigContainer;
    }

    @Bean
    @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer mergedDrsConfigContainer(
        @Qualifier(DrsStandaloneConstants.DEFAULT_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer defaultContainer,
        @Qualifier(DrsStandaloneConstants.USER_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer userContainer
    ) {
        DeepObjectMerger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    @Bean
    public ServerProps getServerProps(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServerProps();
    }

    @Bean
    public DatabaseProps getDatabaseProps(
        @Autowired List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses,
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getDatabaseProps();
    }

    @Bean
    public DrsServiceInfo getServiceInfo(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServiceInfo();
    }
}
