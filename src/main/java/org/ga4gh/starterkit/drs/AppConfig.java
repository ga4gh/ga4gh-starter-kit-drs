package org.ga4gh.starterkit.drs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.ga4gh.starterkit.drs.configuration.DrsConfig;
import org.ga4gh.starterkit.drs.configuration.DrsConfigContainer;
import org.ga4gh.starterkit.drs.configuration.ServerProps;
import org.ga4gh.starterkit.drs.constant.DrsServerPropsDefaults;
import org.ga4gh.starterkit.drs.constant.DrsServiceInfoDefaults;
import org.ga4gh.starterkit.drs.model.AwsS3AccessObject;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.FileAccessObject;
import org.ga4gh.starterkit.common.model.ServiceInfo;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.FileStreamRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties
public class AppConfig implements WebMvcConfigurer {

    /* ******************************
     * DRS SERVER CONFIG BEANS
     * ****************************** */

    @Bean
    @Scope(AppConfigConstants.PROTOTYPE)
    @Qualifier(AppConfigConstants.EMPTY_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer emptyDrsConfigContainer() {
        return new DrsConfigContainer(new DrsConfig());
    }

    @Bean
    @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer defaultDrsConfigContainer(
        @Qualifier(AppConfigConstants.EMPTY_DRS_CONFIG_CONTAINER) DrsConfigContainer drsConfigContainer
    ) {
        ServerProps serverProps = drsConfigContainer.getDrsConfig().getServerProps();
        serverProps.setHostname(DrsServerPropsDefaults.HOSTNAME);

        ServiceInfo serviceInfo = drsConfigContainer.getDrsConfig().getServiceInfo();
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
    @Qualifier(AppConfigConstants.USER_DRS_CONFIG_CONTAINER)
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
        }

        return drsConfigContainer;
    }

    @Bean
    @Qualifier(AppConfigConstants.FINAL_DRS_CONFIG_CONTAINER)
    public DrsConfigContainer mergedDrsConfigContainer(
        @Qualifier(AppConfigConstants.DEFAULT_DRS_CONFIG_CONTAINER) DrsConfigContainer defaultContainer,
        @Qualifier(AppConfigConstants.USER_DRS_CONFIG_CONTAINER) DrsConfigContainer userContainer
    ) {
        DeepObjectMerger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    /* ******************************
     * HIBERNATE CONFIG BEANS
     * ****************************** */

    @Bean List<Class<? extends HibernateEntity>> getAnnotatedClasses() {
        List<Class<? extends HibernateEntity>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(DrsObject.class);
        annotatedClasses.add(Checksum.class);
        annotatedClasses.add(FileAccessObject.class);
        annotatedClasses.add(AwsS3AccessObject.class);
        return annotatedClasses;
    }

    @Bean
    public DrsHibernateUtil getDrsHibernateUtil(
        @Autowired List<Class<? extends HibernateEntity>> annotatedClasses,
        @Qualifier(AppConfigConstants.FINAL_DRS_CONFIG_CONTAINER) DrsConfigContainer drsConfigContainer
    ) {
        DrsHibernateUtil hibernateUtil = new DrsHibernateUtil();
        hibernateUtil.setAnnotatedClasses(annotatedClasses);
        hibernateUtil.setHibernateProps(drsConfigContainer.getDrsConfig().getHibernateProps());
        return hibernateUtil;
    }

    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to YAML config file");
        return options;
    }

    /* ******************************
     * REQUEST HANDLER BEANS
     * ****************************** */

    @Bean
    @RequestScope
    public ObjectRequestHandler objectRequestHandler() {
        return new ObjectRequestHandler();
    }

    @Bean
    @RequestScope
    public AccessRequestHandler accessRequestHandler() {
        return new AccessRequestHandler();
    }

    @Bean
    @RequestScope
    public FileStreamRequestHandler fileStreamRequestHandler() {
        return new FileStreamRequestHandler();
    }

    /* ******************************
     * OTHER UTILS BEANS
     * ****************************** */

    @Bean
    public AccessCache accessCache() {
        return new AccessCache();
    }
}
