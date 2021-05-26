package org.ga4gh.starterkit.drs.app;

import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.config.DrsServiceProps;
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

/**
 * Contains Spring bean definitions that are to be loaded only during DRS
 * standalone deployments (ie. not when being run as a GA4GH multi-API service)
 * 
 * @see org.ga4gh.starterkit.drs.beanconfig.StarterKitDrsSpringConfig Spring config common to standalone and non-standalone deployments
 */
@Configuration
@ConfigurationProperties
public class DrsStandaloneSpringConfig implements WebMvcConfigurer {

    /* ******************************
     * DRS SERVER CONFIG BEANS
     * ****************************** */

    /**
     * Load command line options object, to enable parsing of program args
     * @return valid command line options to be parsed
     */
    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to DRS YAML config file");
        return options;
    }

    /**
     * Loads an empty DRS config container
     * @return DRS config container with empty properties
     */
    @Bean
    @Scope(DrsStandaloneConstants.PROTOTYPE)
    @Qualifier(DrsStandaloneConstants.EMPTY_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer emptyDrsConfigContainer() {
        return new DrsStandaloneYamlConfigContainer(new DrsStandaloneYamlConfig());
    }

    /**
     * Loads a DRS config container singleton containing all default properties
     * @return DRS config container containing defaults
     */
    @Bean
    @Qualifier(DrsStandaloneConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer defaultDrsConfigContainer() {
        return new DrsStandaloneYamlConfigContainer(new DrsStandaloneYamlConfig());
    }

    /**
     * Loads a DRS config container singleton containing user-specified properties (via config file)
     * @param args command line args
     * @param options valid set of command line options to be parsed
     * @param drsConfigContainer empty DRS config container
     * @return DRS config container singleton containing user-specified properties
     */
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

    /**
     * Loads the final DRS config container singleton containing merged properties
     * between default and user-specified
     * @param defaultContainer contains default properties
     * @param userContainer contains user-specified properties
     * @return contains merged properties
     */
    @Bean
    @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer mergedDrsConfigContainer(
        @Qualifier(DrsStandaloneConstants.DEFAULT_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer defaultContainer,
        @Qualifier(DrsStandaloneConstants.USER_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer userContainer
    ) {
        DeepObjectMerger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    /**
     * Retrieve server props object from merged DRS config container
     * @param drsConfigContainer merged DRS config container
     * @return merged server props
     */
    @Bean
    public ServerProps getServerProps(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServerProps();
    }

    /**
     * Retrieve database props object from merged DRS config container
     * @param annotatedClasses list of hibernate entity classes to be managed by the DRS hibernate util
     * @param drsConfigContainer merged DRS config container
     * @return merged database props
     */
    @Bean
    public DatabaseProps getDatabaseProps(
        @Autowired List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses,
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getDatabaseProps();
    }

    /**
     * Retrieve DRS service info object from merged DRS config container
     * @param drsConfigContainer merged DRS config container
     * @return merged DRS service info 
     */
    @Bean
    public DrsServiceInfo getServiceInfo(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServiceInfo();
    }

    /**
     * Retrieve DRS service properties from merged DRS config container
     * @param drsConfigContainer merged DRS config container
     * @return merged DRS service properties
     */
    @Bean
    public DrsServiceProps getDrsServiceProps(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getDrsServiceProps();
    }
}
