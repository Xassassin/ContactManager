package ece1779.appengine.jpa;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import ece1779.appengine.dto.Contact;

public class ContactDAO {
	
	public ContactDAO() {
		
	}
	
//	public Contact getContact(Key contactId) {
//		String cacheKey = getCacheKeyForContact(contactId.toString());
//		Contact contact;
//		try {
//			MemcacheService memcache = MemcacheServiceFactory
//					.getMemcacheService();
//			if (memcache.contains(cacheKey)) {
//				contact = (Contact) memcache.get(cacheKey);
//				return contact;
//			}
//			System.out.println("CACHE MISS: " + cacheKey);
//			// If the UserPrefs object isn't in memcache,
//			// fall through to the datastore.
//		} catch (MemcacheServiceException e) {
//			// If there is a problem with the cache,
//			// fall through to the datastore.
//		}
//
//		PersistenceManager em = EMF.get().getPersistenceManager();
//		try {
//			contact = em.find(Contact.class, contactId);
//			if (contact == null) {
//				contact = new Contact();
//				saveContact(contact);
//			} else {
//				cacheSet(contact);
//			}
//		} finally {
//			em.close();
//		}
//		return contact;
//	}
	
	public Contact getContact(String keyString) {
        String cacheKey = getCacheKeyForContact(keyString);
        Contact contact = null;
        try {
            MemcacheService memcache = MemcacheServiceFactory
                    .getMemcacheService();
            if (memcache.contains(cacheKey)) {
                contact = (Contact) memcache.get(cacheKey);
                return contact;
            }
            System.out.println("CACHE MISS: " + cacheKey);
            // If the UserPrefs object isn't in memcache,
            // fall through to the datastore.
        } catch (MemcacheServiceException e) {
            // If there is a problem with the cache,
            // fall through to the datastore.
        }

        PersistenceManager em = EMF.get().getPersistenceManager();
        Key k = KeyFactory.stringToKey(keyString);
        
        try {
            try {
            contact = em.getObjectById(Contact.class, k);
            }  catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(q.toString());
//            List<Contact> contacts = (List<Contact>) q.execute();
//            System.out.println("Found " + contacts.size() + " contacts");
//            if (!contacts.isEmpty()) {
//                if (contacts.size() == 1) {
//                    contact = contacts.get(0);
//                }
//            }
            if (contact != null) {
//            } else {
                cacheSet(contact);
                contact = em.detachCopy(contact);
            }
            
        } finally {
            em.close();
        }
        return contact;
    }
	
	

	public void saveContact(Contact contact) {
	    PersistenceManager em = EMF.get().getPersistenceManager();
		try {
			em.makePersistent(contact);
			cacheDelete(contact);
		} finally {
			em.close();
		}
	}

	public void cacheSet(Contact contact) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForContact(contact.getId().toString());
			memcache.put(cacheKey, contact);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}
	
//	public void deleteContact(Contact contact) {
//		EntityManager em = EMF.get().createEntityManager();
//		try {
//			em.remove(contact);
//			cacheDelete(contact);
//		} finally {
//			em.close();
//		}
//	}

	public void cacheDelete(Contact contact) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForContact(contact.getId().toString());
			memcache.delete(cacheKey);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}

	public String getCacheKeyForContact(String userId) {
		return "Person:" + userId;
	}

}
