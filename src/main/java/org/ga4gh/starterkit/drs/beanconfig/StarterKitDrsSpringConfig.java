package org.ga4gh.starterkit.drs.beanconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateProps;
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

@Configuration
@ConfigurationProperties
public class StarterKitDrsSpringConfig {

    /* ******************************
     * HIBERNATE CONFIG BEANS
     * ****************************** */

    @Bean
    public List<Class<? extends HibernateEntity<? extends Serializable>>> getAnnotatedClasses() {
        List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(DrsObject.class);
        annotatedClasses.add(Checksum.class);
        annotatedClasses.add(FileAccessObject.class);
        annotatedClasses.add(AwsS3AccessObject.class);
        return annotatedClasses;
    }

    @Bean
    public DrsHibernateUtil getDrsHibernateUtil(
        @Autowired List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses,
        @Autowired HibernateProps hibernateProps
    ) {
        DrsHibernateUtil hibernateUtil = new DrsHibernateUtil();
        hibernateUtil.setAnnotatedClasses(annotatedClasses);
        hibernateUtil.setHibernateProps(hibernateProps);
        return hibernateUtil;
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
