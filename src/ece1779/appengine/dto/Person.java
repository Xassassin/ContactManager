package ece1779.appengine.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Person implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7881276867113228172L;

	@PrimaryKey
    Key user_id;
	
	@Persistent
	private String user;
    
	@Persistent(mappedBy = "person")
	@Element(dependent = "true")
	private List<Contact> contacts;
    
    public Person(User user) {
        this.user_id = KeyFactory.createKey("Person",user.getUserId());
    }

    public Person(String user_id) {
        this.user_id = KeyFactory.createKey("Person",user_id);
        this.user = user_id;
    }

    public String getUserId() {
        return KeyFactory.keyToString(user_id);
    }

    public void setUserId(Key user_id) {
		this.user_id = user_id;
	}

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
    public List<Contact> getContacts() {
        if (contacts == null) {
        	contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
