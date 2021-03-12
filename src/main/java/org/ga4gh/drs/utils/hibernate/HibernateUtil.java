package org.ga4gh.drs.utils.hibernate;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.model.DrsEntity;
import org.ga4gh.drs.model.DrsObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HibernateUtil {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    private boolean configured;
    private SessionFactory sessionFactory;

    public HibernateUtil() {
        configured = false;
    }

    @PostConstruct
    public void buildSessionFactory() {
        try {
            SessionFactory sessionFactory =
                new Configuration()
                .setProperties(drsConfigContainer.getDrsConfig().getHibernateProps().getAllProperties())
                .addAnnotatedClass(DrsObject.class)
                .buildSessionFactory();
            setSessionFactory(sessionFactory);
            setConfigured(configured);
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public DrsEntity getSingleEntityObject(Class<? extends DrsEntity> entityClass, String id) throws HibernateException {
        Session session = newTransaction();
        DrsEntity object = null;
        try {
            object = (DrsEntity) session.get(entityClass, id);
            if (object != null) {
                object.lazyLoad();
            }
            endTransaction(session);
        } catch (PersistenceException e) {
            throw new HibernateException(e.getMessage());
        } catch (Exception e) {
            throw new HibernateException(e.getMessage());
        } finally {
            endTransaction(session);
        }
        return object;
    }

    private Session newTransaction() {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        return session;
    }

    private void endTransaction(Session session) {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void shutdown() {
        getSessionFactory().close();
    }

    /* SETTERS AND GETTERS */

    private void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean getConfigured() {
        return configured;
    }

    private void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
