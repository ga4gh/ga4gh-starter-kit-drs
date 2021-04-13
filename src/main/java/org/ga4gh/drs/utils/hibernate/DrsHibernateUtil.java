package org.ga4gh.drs.utils.hibernate;

import java.util.List;
import javax.persistence.PersistenceException;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class DrsHibernateUtil extends HibernateUtil {

    public DrsObject loadDrsObject(String id) throws HibernateException {
        Session session = newTransaction();
        DrsObject drsObject = null;
        try {
            drsObject = session.get(DrsObject.class, id);
            if (drsObject != null) {
                drsObject.loadRelations();
                drsObject.setSize(recursiveDrsObjectChildLoad(drsObject));
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

        if (childrenDrsObjects.size() == 0) {
            sizeSum = parentDrsObject.getSize();
        } else {
            for (int i = 0; i < childrenDrsObjects.size(); i ++) {
                sizeSum += recursiveDrsObjectChildLoad(childrenDrsObjects.get(i));
            }
        }

        return sizeSum;
    }
}
