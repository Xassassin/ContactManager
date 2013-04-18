package ece1779.appengine.jpa;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;



public final class EMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private EMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}