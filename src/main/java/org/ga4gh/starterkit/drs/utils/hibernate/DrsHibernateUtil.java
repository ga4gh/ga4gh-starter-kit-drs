package org.ga4gh.starterkit.drs.utils.hibernate;

import java.io.Serializable;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class DrsHibernateUtil extends HibernateUtil {

    public DrsObject loadDrsObject(String id, boolean recursiveChildLoad) throws HibernateException {
        Session session = newTransaction();
        DrsObject drsObject = null;
        try {
            drsObject = session.get(DrsObject.class, id);
            if (drsObject != null) {
                drsObject.loadRelations();

                // detach entity so computed fields (size, checksum)
                // aren't saved to db
                session.evict(drsObject);
                if (recursiveChildLoad) {
                    drsObject.setSize(recursiveDrsObjectChildLoad(drsObject));
                }
            }
        } catch (PersistenceException e) {
            throw new HibernateException(e.getMessage());
        } catch (Exception e) {
            throw new HibernateException(e.getMessage());
        } finally {
            endTransaction(session);
        }
        return drsObject;
    }

    private Long recursiveDrsObjectChildLoad(DrsObject parentDrsObject) {
        Long sizeSum = 0L;
        List<DrsObject> childrenDrsObjects = parentDrsObject.getDrsObjectChildren();

        if (childrenDrsObjects != null) {
            if (childrenDrsObjects.size() == 0) {
                sizeSum = parentDrsObject.getSize();
                sizeSum = sizeSum == null ? 0L : sizeSum;
            } else {
                for (int i = 0; i < childrenDrsObjects.size(); i ++) {
                    childrenDrsObjects.get(i).loadRelations();
                    sizeSum += recursiveDrsObjectChildLoad(childrenDrsObjects.get(i));
                }
            }
        }

        return sizeSum;
    }

    public <T extends HibernateEntity<? extends Serializable>> List<T> getEntityList(Class<T> entityClass) {
        Session session = newTransaction();
        List<T> entities = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(entityClass);
            criteria.from(entityClass);
            entities = session.createQuery(criteria).getResultList();
        } finally {
            endTransaction(session);
        }
        return entities;
    }
}
