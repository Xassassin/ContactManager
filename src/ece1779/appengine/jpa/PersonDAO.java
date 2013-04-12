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
		String cacheKey = getCacheKeyForUser(user.getUserId());
		Person person;
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			if (memcache.contains(cacheKey)) {
				person = (Person) memcache.get(cacheKey);
				return person;
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
			person = em.find(Person.class, user.getUserId());
			if (person == null) {
				person = new Person(user.getUserId());
				savePerson(person);
			} else {
				cacheSet(person);
			}
		} finally {
			em.close();
		}
		return null;
	}

	public void savePerson(Person person) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.merge(person);
			cacheDelete(person);
		} finally {
			em.close();
		}
	}

	public void cacheSet(Person person) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForUser(person.getUserId());
			memcache.put(cacheKey, this);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}

	public void cacheDelete(Person person) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForUser(person.getUserId());
			memcache.delete(cacheKey);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}

	public String getCacheKeyForUser(String userId) {
		return "Person:" + userId;
	}

}