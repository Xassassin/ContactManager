package ece1779.appengine.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Entity(name = "Person")
public class Person {
    @Id
    Key user_id;
    
    @OneToMany(cascade = CascadeType.ALL)
	private List<Contact> contacts;

    public Person(String user_id) {
        this.user_id = KeyFactory.createKey("Person",user_id);
    }

    public String getUserId() {
        return KeyFactory.keyToString(user_id);
    }

    public void setUserId(com.google.appengine.api.datastore.Key user_id) {
		this.user_id = user_id;
	}

    public void setContact(List<Contact> contacts) {
        this.contacts = contacts;
    }
    public List<Contact> getContact() {
        if (contacts == null) {
        	contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

}
