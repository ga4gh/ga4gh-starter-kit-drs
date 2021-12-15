package org.ga4gh.starterkit.drs.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsConnector;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsFilter;
import org.ga4gh.starterkit.common.util.webserver.CorsFilterBuilder;
import org.ga4gh.starterkit.common.util.webserver.TomcatMultiConnectorServletWebServerFactoryCustomizer;
import org.apache.catalina.connector.Connector;
import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.drs.config.DrsServiceProps;
import org.ga4gh.starterkit.drs.exception.DrsCustomExceptionHandling;
import org.ga4gh.starterkit.drs.model.AwsS3AccessObject;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;
import org.ga4gh.starterkit.drs.model.FileAccessObject;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.FileStreamRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.CorsFilter;

/**
 * Contains Spring bean definitions that are to be loaded for the DRS service
 * under all deployment contexts (ie as part of both standalone and GA4GH
 * multi-API service deployments)
 * 
 * @see org.ga4gh.starterkit.drs.app.DrsStandaloneSpringConfig Spring config beans used only during standalone deployments
 */
@Configuration
@ConfigurationProperties
public class DrsServerSpringConfig {

    /* ******************************
     * TOMCAT SERVER
     * ****************************** */

    @Value("${server.admin.port:4501}")
    private String serverAdminPort;

    @Bean
    public WebServerFactoryCustomizer servletContainer() {
        Connector[] additionalConnectors = AdminEndpointsConnector.additionalConnector(serverAdminPort);
        ServerProperties serverProperties = new ServerProperties();
        return new TomcatMultiConnectorServletWebServerFactoryCustomizer(serverProperties, additionalConnectors);
    }

    @Bean
    public FilterRegistrationBean<AdminEndpointsFilter> adminEndpointsFilter() {
        return new FilterRegistrationBean<AdminEndpointsFilter>(new AdminEndpointsFilter(Integer.valueOf(serverAdminPort)));
    }
    
    @Bean
    public DrsCustomExceptionHandling customExceptionHandling() {
        return new DrsCustomExceptionHandling();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(
        @Autowired ServerProps serverProps
    ) {
        return new CorsFilterBuilder(serverProps).buildFilter();
    }

    /* ******************************
     * YAML CONFIG
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
    @Scope(DrsServerConstants.PROTOTYPE)
    @Qualifier(DrsServerConstants.EMPTY_DRS_CONFIG_CONTAINER)
    public DrsServerYamlConfigContainer emptyDrsConfigContainer() {
        return new DrsServerYamlConfigContainer(new DrsServerYamlConfig());
    }

    /**
     * Loads a DRS config container singleton containing all default properties
     * @return DRS config container containing defaults
     */
    @Bean
    @Qualifier(DrsServerConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsServerYamlConfigContainer defaultDrsConfigContainer() {
        return new DrsServerYamlConfigContainer(new DrsServerYamlConfig());
    }

    /**
     * Loads a DRS config container singleton containing user-specified properties (via config file)
     * @param args command line args
     * @param options valid set of command line options to be parsed
     * @param drsConfigContainer empty DRS config container
     * @return DRS config container singleton containing user-specified properties
     */
    @Bean
    @Qualifier(DrsServerConstants.USER_DRS_CONFIG_CONTAINER)
    public DrsServerYamlConfigContainer runtimeDrsConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(DrsServerConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer drsConfigContainer
    ) {
        DrsServerYamlConfigContainer userConfigContainer = CliYamlConfigLoader.load(DrsServerYamlConfigContainer.class, args, options, "config");
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
    @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER)
    public DrsServerYamlConfigContainer mergedDrsConfigContainer(
        @Qualifier(DrsServerConstants.DEFAULT_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer defaultContainer,
        @Qualifier(DrsServerConstants.USER_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer userContainer
    ) {
        DeepObjectMerger merger = new DeepObjectMerger();
        merger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    /**
     * Retrieve server props object from merged DRS config container
     * @param drsConfigContainer merged DRS config container
     * @return merged server props
     */
    @Bean
    public ServerProps getServerProps(
        @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer drsConfigContainer
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
        @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer drsConfigContainer
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
        @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer drsConfigContainer
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
        @Qualifier(DrsServerConstants.FINAL_DRS_CONFIG_CONTAINER) DrsServerYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getDrsServiceProps();
    }

    /* ******************************
     * LOGGING
     * ****************************** */

    @Bean
    public LoggingUtil loggingUtil() {
        return new LoggingUtil();
    }

    /* ******************************
     * HIBERNATE CONFIG
     * ****************************** */

    /**
     * List of hibernate entity classes to be managed by the Drs hibernate util
     * @return list of DRS-related managed entity classes
     */
    @Bean
    public List<Class<? extends HibernateEntity<? extends Serializable>>> getAnnotatedClasses() {
        List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(DrsObject.class);
        annotatedClasses.add(Checksum.class);
        annotatedClasses.add(FileAccessObject.class);
        annotatedClasses.add(AwsS3AccessObject.class);
        return annotatedClasses;
    }

    /**
     * Loads/retrieves the hibernate util singleton providing access to DRS-related database tables
     * @param annotatedClasses list of DRS-related entities to be managed
     * @param databaseProps database properties from configuration
     * @return loaded hibernate util singleton managing DRS entities
     */
    @Bean
    public DrsHibernateUtil getDrsHibernateUtil(
        @Autowired List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses,
        @Autowired DatabaseProps databaseProps
    ) {
        DrsHibernateUtil hibernateUtil = new DrsHibernateUtil();
        hibernateUtil.setAnnotatedClasses(annotatedClasses);
        hibernateUtil.setDatabaseProps(databaseProps);
        return hibernateUtil;
    }

    /* ******************************
     * REQUEST HANDLER
     * ****************************** */

    /**
     * Get new request handler facilitating access to a DRSObject
     * @return drs object request handler
     */
    @Bean
    @RequestScope
    public ObjectRequestHandler objectRequestHandler() {
        return new ObjectRequestHandler();
    }

    /**
     * Get new request handler facilitating the 'access' endpoint, i.e. provides
     * an AccessURL for a given object_id and access_id
     * @return access URL request handler
     */
    @Bean
    @RequestScope
    public AccessRequestHandler accessRequestHandler() {
        return new AccessRequestHandler();
    }

    /**
     * Get new request handler facilitating streaming of a local file over http(s)
     * @return streaming request handler
     */
    @Bean
    @RequestScope
    public FileStreamRequestHandler fileStreamRequestHandler() {
        return new FileStreamRequestHandler();
    }

    /* ******************************
     * OTHER UTILS
     * ****************************** */

    /**
     * Get cache singleton, storing object_id and access_id mappings
     * @return object_id, access_id cache
     */
    @Bean
    public AccessCache accessCache() {
        return new AccessCache();
    }
}
