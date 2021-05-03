package org.ga4gh.starterkit.drs.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.ga4gh.starterkit.drs.configuration.ServerProps;
import org.ga4gh.starterkit.drs.constant.DrsServerPropsDefaults;
import org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults;
import org.ga4gh.starterkit.drs.model.DrsServiceInfo;
import org.ga4gh.starterkit.common.model.ServiceInfo;
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
import org.ga4gh.starterkit.common.hibernate.HibernateProps;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public DrsStandaloneYamlConfigContainer defaultDrsConfigContainer(
        @Qualifier(DrsStandaloneConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        ServerProps serverProps = drsConfigContainer.getDrs().getServerProps();
        serverProps.setHostname(DrsServerPropsDefaults.HOSTNAME);

        ServiceInfo serviceInfo = drsConfigContainer.getDrs().getServiceInfo();
        serviceInfo.setId(DrsServiceInfoDefaults.ID);
        serviceInfo.setName(DrsServiceInfoDefaults.NAME);
        serviceInfo.setDescription(DrsServiceInfoDefaults.DESCRIPTION);
        serviceInfo.setContactUrl(DrsServiceInfoDefaults.CONTACT_URL);
        serviceInfo.setDocumentationUrl(DrsServiceInfoDefaults.DOCUMENTATION_URL);
        serviceInfo.setCreatedAt(DrsServiceInfoDefaults.CREATED_AT);
        serviceInfo.setUpdatedAt(DrsServiceInfoDefaults.UPDATED_AT);
        serviceInfo.setEnvironment(DrsServiceInfoDefaults.ENVIRONMENT);
        serviceInfo.setVersion(DrsServiceInfoDefaults.VERSION);
        serviceInfo.getOrganization().setName(DrsServiceInfoDefaults.ORGANIZATION_NAME);
        serviceInfo.getOrganization().setUrl(DrsServiceInfoDefaults.ORGANIZATION_URL);
        serviceInfo.getType().setArtifact(DrsServiceInfoDefaults.SERVICE_TYPE_ARTIFACT);
        serviceInfo.getType().setGroup(DrsServiceInfoDefaults.SERVICE_TYPE_GROUP);
        serviceInfo.getType().setVersion(DrsServiceInfoDefaults.SERVICE_TYPE_VERSION);

        return drsConfigContainer;
    }

    @Bean
    @Qualifier(DrsStandaloneConstants.USER_DRS_CONFIG_CONTAINER)
    public DrsStandaloneYamlConfigContainer runtimeDrsConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(DrsStandaloneConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args.getSourceArgs());
            String configFilePath = cmd.getOptionValue("config");
            if (configFilePath != null) {
                File configFile = new File(configFilePath);
                if (configFile.exists() && !configFile.isDirectory()) {
                    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                    drsConfigContainer = mapper.readValue(configFile, DrsStandaloneYamlConfigContainer.class);
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
    public HibernateProps gethibernateProps(
        @Autowired List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses,
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getHibernateProps();
    }

    @Bean
    public ServerProps getServerProps(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServerProps();
    }

    @Bean
    public DrsServiceInfo getServiceInfo(
        @Qualifier(DrsStandaloneConstants.FINAL_DRS_CONFIG_CONTAINER) DrsStandaloneYamlConfigContainer drsConfigContainer
    ) {
        return drsConfigContainer.getDrs().getServiceInfo();
    }
}
