package org.ga4gh.starterkit.drs.beanconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.drs.model.AwsS3AccessObject;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.FileAccessObject;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.ga4gh.starterkit.drs.utils.requesthandler.AccessRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.FileStreamRequestHandler;
import org.ga4gh.starterkit.drs.utils.requesthandler.ObjectRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Contains Spring bean definitions that are to be loaded for the DRS service
 * under all deployment contexts (ie as part of both standalone and GA4GH
 * multi-API service deployments)
 * 
 * @see org.ga4gh.starterkit.drs.app.DrsStandaloneSpringConfig Spring config beans used only during standalone deployments
 */
@Configuration
@ConfigurationProperties
public class StarterKitDrsSpringConfig {

    /* ******************************
     * HIBERNATE CONFIG BEANS
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
     * REQUEST HANDLER BEANS
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
     * OTHER UTILS BEANS
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
