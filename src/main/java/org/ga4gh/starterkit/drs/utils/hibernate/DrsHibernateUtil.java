package org.ga4gh.starterkit.drs.utils.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
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

    /*
    @Override
    public <I extends Serializable, T extends HibernateEntity<I>> void createEntityObject(Class<T> entityClass, T newObject) throws EntityExistsException {
        System.out.println("CHILD CREATE ENTITY METHOD");
        Session session = newTransaction();
        T existingObject = session.get(entityClass, newObject.getId());
        if (existingObject != null) {
            endTransaction(session);
            throw new EntityExistsException("A(n) " + entityClass.getSimpleName() + " already exists at id " + newObject.getId());
        }
        session.saveOrUpdate(newObject);
        endTransaction(session);
    }
    */
}
