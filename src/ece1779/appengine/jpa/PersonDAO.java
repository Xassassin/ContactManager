package ece1779.appengine.jpa;

import javax.persistence.EntityManager;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;

import ece1779.appengine.dto.Person;

public class PersonDAO {

	public PersonDAO() {

	}
	
	public Person getPerson(User user) {
		return getPerson(user.getUserId());
		
	}

	public Person getPerson(String userId) {
		String cacheKey = getCacheKeyForUser(userId);
		Person person;
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			System.out.println("Item Count:" + memcache.getStatistics().getItemCount());
			if (memcache.contains(cacheKey)) {
				person = (Person) memcache.get(cacheKey);
//				if (person != null) {
				return person;
//				}
			}
			System.out.println("CACHE MISS: " + cacheKey);
			// If the UserPrefs object isn't in memcache,
			// fall through to the datastore.
		} catch (MemcacheServiceException e) {
			e.printStackTrace();
			// If there is a problem with the cache,
			// fall through to the datastore.
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			person = em.find(Person.class, userId);
			if (person == null) {
				System.out.println("Did not find Person in datastore.");
//				person = new Person(userId);
//				savePerson(person);
				return null;
			} else {
				cacheSet(person);
			}
		} finally {
			em.close();
		}
		return person;
	}

	public void savePerson(Person person) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.merge(person);
			cacheDelete(person);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void cacheSet(Person person) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForUser(person.getUser());
			System.out.println("CACHE FILL: " + cacheKey);
			memcache.put(cacheKey, person);
			System.out.println("Item Count:" + memcache.getStatistics().getItemCount());
		} catch (MemcacheServiceException e) {
			e.printStackTrace();
		}
	}

	public void cacheDelete(Person person) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForUser(person.getUser());
			memcache.delete(cacheKey);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}

	public String getCacheKeyForUser(String userId) {
		return "Person:" + userId;
	}

}
