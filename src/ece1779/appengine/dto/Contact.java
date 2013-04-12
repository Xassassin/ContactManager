package ece1779.appengine.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity(name = "Contact")
public class Contact implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6640702544946967268L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Key id;

    private String Name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Detail> details;
    
    public Contact() {
    }
    
    public void setName(String Name) {
        this.Name = Name;
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

}
