package ece1779.appengine.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.KeyFactory;

@Entity(name = "Detail")
public class Detail {
	
	private static final int ADDRESS = 1;
	private static final int PHONE = 2;
	private static final int EMAIL = 3;
	private static final int URL = 4;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Key id;

    private String item;
    private String value;
    private int category;

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
    


}
