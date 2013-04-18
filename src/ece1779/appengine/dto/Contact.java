package ece1779.appengine.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Contact implements Serializable, Comparable<Contact> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6640702544946967268L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String Name;
	
	@Persistent
	private Person person;
	
	@Persistent(mappedBy = "contact")
    @Element(dependent = "true")
	private List<Detail> details;

	public Contact() {
	      
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getName() {
		return this.Name;
	}

	public void setDetail(List<Detail> details) {
		this.details = details;
	}

	public List<Detail> getDetail() {
		if (details == null) {
			details = new ArrayList<Detail>();
		}
		return details;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof Contact)) {
			return false;
		}

		if (this == other) {
			return true;
		}

		Contact c = (Contact) other;

		if (this.getId() != null && c.getId() != null) {
			if (this.getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

    @Override
    public int compareTo(Contact o) {
        return this.getName().compareTo(o.getName());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
