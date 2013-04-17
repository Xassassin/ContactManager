package ece1779.appengine.dto;

import java.io.Serializable;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity(name = "Detail")
public class Detail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8422959252650162293L;
	public static final int ADDRESS = 1;
	public static final int PHONE = 2;
	public static final int EMAIL = 3;
	public static final int URL = 4;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	@Persistent
	private String item;
	@Persistent
	private String value;
	@Persistent
	private int category = 0;

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
