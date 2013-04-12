package ece1779.appengine.jpa;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import ece1779.appengine.dto.Contact;

public class ContactDAO {
	
	public ContactDAO() {
		
	}
	
	public Contact getContact(Key contactId) {
		String cacheKey = getCacheKeyForContact(contactId.toString());
		Contact contact;
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

		EntityManager em = EMF.get().createEntityManager();
		try {
			contact = em.find(Contact.class, contactId);
			if (contact == null) {
				contact = new Contact();
				saveContact(contact);
			} else {
				cacheSet(contact);
			}
		} finally {
			em.close();
		}
		return null;
	}

	public void saveContact(Contact contact) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.merge(contact);
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
			memcache.put(cacheKey, this);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}
	
	public void deleteContact(Contact contact) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.remove(contact);
			cacheDelete(contact);
		} finally {
			em.close();
		}
	}

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
