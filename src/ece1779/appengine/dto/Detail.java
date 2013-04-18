package ece1779.appengine.dto;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Detail implements Serializable, Comparable<Detail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8422959252650162293L;
	public static final int ADDRESS = 1;
	public static final int PHONE = 2;
	public static final int EMAIL = 3;
	public static final int URL = 4;
	public static final int OTHER = 5;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
    private Contact contact;

	@Persistent
	private String item;
	@Persistent
	private String value;
	@Persistent
	private int category;
	
	public Detail() {
	    
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItem() {
		return this.item;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public int compareTo(Detail o) {
        Integer category1 = Integer.valueOf(this.getCategory());
        Integer category2 = Integer.valueOf(o.getCategory());
        int comapered = category1.compareTo(category2);
        if (comapered != 0) {
            return comapered;
        } else {
            return this.getItem().compareTo(o.getItem());
        }
    }

	// public boolean equals(Object other) {
	// if (other == null || !(other instanceof Detail)) {
	// return false;
	// }
	//
	// if (this == other) {
	// return true;
	// }
	//
	// Detail d = (Detail) other;
	//
	// if (this.getId() != null && d.getId() != null) {
	// if (this.getId().equals(d.getId())) {
	// return true;
	// }
	// }
	// return false;
	// }

}
