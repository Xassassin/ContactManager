package ece1779.appengine.jpa;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Detail;

public class DetailDAO {
	
	public DetailDAO() {
		
	}
	
	public Detail getDetail(Key detailId) {
		String cacheKey = getCacheKeyForDetail(detailId.toString());
		Detail detail;
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			if (memcache.contains(cacheKey)) {
				detail = (Detail) memcache.get(cacheKey);
				return detail;
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
			detail = em.find(Detail.class, detailId);
			if (detail == null) {
				detail = new Detail();
				saveDetail(detail);
			} else {
				cacheSet(detail);
			}
		} finally {
			em.close();
		}
		return null;
	}

	public void saveDetail(Detail detail) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.merge(detail);
			cacheDelete(detail);
		} finally {
			em.close();
		}
	}

	public void cacheSet(Detail detail) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForDetail(detail.getId().toString());
			memcache.put(cacheKey, this);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}
	
	public void deleteDetail(Detail detail) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.remove(detail);
			cacheDelete(detail);
		} finally {
			em.close();
		}
	}

	public void cacheDelete(Detail detail) {
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			String cacheKey = getCacheKeyForDetail(detail.getId().toString());
			memcache.delete(cacheKey);
		} catch (MemcacheServiceException e) {
			// Ignore cache problems, nothing we can do.
		}
	}

	public String getCacheKeyForDetail(String userId) {
		return "Person:" + userId;
	}

}
