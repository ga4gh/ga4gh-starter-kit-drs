package org.ga4gh.drs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.*;
import org.ga4gh.drs.configuration.DataSourceRegistry;
import org.ga4gh.drs.configuration.DrsConfig;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.constant.DataSourceDefaults;
import org.ga4gh.drs.constant.ServiceInfoDefaults;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.ServiceInfo;
import org.ga4gh.drs.utils.DataSourceLookup;
import org.ga4gh.drs.utils.DeepObjectMerger;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoaderFactory;
import org.ga4gh.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


@Configuration
@ConfigurationProperties
public class AppConfig implements WebMvcConfigurer {

    /* ******************************
     * CONFIG BEANS
     * ****************************** */

    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to YAML config file");
        return options;
    }

    @Bean
    @Scope(AppConfigConstants.PROTOTYPE)
    @Qualifier(AppConfigConstants.EMPTY_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer emptyDrsConfigContainer() {
        ServiceInfo serviceInfo = new ServiceInfo();
        DataSourceRegistry dataSourceRegistry = new DataSourceRegistry();
        return new DrsConfigContainer(new DrsConfig(serviceInfo, dataSourceRegistry));
    }

    @Bean
    @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer defaultDrsConfigContainer(
        @Qualifier(AppConfigConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsConfigContainer drsConfigContainer
    ) {
        ServiceInfo serviceInfo = drsConfigContainer.getDrsConfig().getServiceInfo();
        serviceInfo.setId(ServiceInfoDefaults.ID);
        serviceInfo.setName(ServiceInfoDefaults.NAME);
        serviceInfo.setDescription(ServiceInfoDefaults.DESCRIPTION);
        serviceInfo.setContactUrl(ServiceInfoDefaults.CONTACT_URL);
        serviceInfo.setDocumentationUrl(ServiceInfoDefaults.DOCUMENTATION_URL);
        serviceInfo.setCreatedAt(ServiceInfoDefaults.CREATED_AT);
        serviceInfo.setUpdatedAt(ServiceInfoDefaults.UPDATED_AT);
        serviceInfo.setEnvironment(ServiceInfoDefaults.ENVIRONMENT);
        serviceInfo.setVersion(ServiceInfoDefaults.VERSION);
        serviceInfo.getOrganization().setName(ServiceInfoDefaults.ORGANIZATION_NAME);
        serviceInfo.getOrganization().setUrl(ServiceInfoDefaults.ORGANIZATION_URL);

        serviceInfo.getType().setArtifact(ServiceInfoDefaults.SERVICE_TYPE_ARTIFACT);
        serviceInfo.getType().setGroup(ServiceInfoDefaults.SERVICE_TYPE_GROUP);
        serviceInfo.getType().setVersion(ServiceInfoDefaults.SERVICE_TYPE_VERSION);

        DataSourceRegistry dataSourceRegistry = drsConfigContainer.getDrsConfig().getDataSourceRegistry();
        dataSourceRegistry.setDataSources(DataSourceDefaults.REGISTRY);

        return drsConfigContainer;
    }

    @Bean
    @Qualifier(AppConfigConstants.RUNTIME_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer runtimeDrsConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(AppConfigConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsConfigContainer drsConfigContainer
    ) {

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args.getSourceArgs());
            String configFilePath = cmd.getOptionValue("config");
            if (configFilePath != null) {
                File configFile = new File(configFilePath);
                if (configFile.exists() && !configFile.isDirectory()) {
                    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                    drsConfigContainer = mapper.readValue(configFile, DrsConfigContainer.class);
                } else {
                    throw new FileNotFoundException();
                }
            }
        } catch (ParseException e) {
            System.out.println("ERROR: problem encountered setting config, config not set");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: problem encountered setting config, config file not found");
        } catch (IOException e) {
            System.out.println("ERROR: problem encountered setting config, config YAML could not be parsed");
            System.out.println(e);
        }

        return drsConfigContainer;
    }

    @Bean
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer mergedDrsConfigContainer(
        @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER) DrsConfigContainer defaultContainer,
        @Qualifier(AppConfigConstants.RUNTIME_DRS_CONFIG_CONTAINER) DrsConfigContainer runtimeContainer
    ) {
        DeepObjectMerger.merge(runtimeContainer, defaultContainer);
        return defaultContainer;
    }

    /* ******************************
     * REQUEST HANDLER BEANS
     * ****************************** */

    @Bean
    @RequestScope
    public ObjectRequestHandler objectRequestHandler() {
        return new ObjectRequestHandler();
    }

    /* ******************************
     * OTHER UTILS BEANS
     * ****************************** */

    @Bean
    public DataSourceLookup dataSourceLookup() {
        return new DataSourceLookup();
    }

    @Bean
    public DrsObjectLoaderFactory drsObjectLoaderFactory() {
        return new DrsObjectLoaderFactory();
    }
}
