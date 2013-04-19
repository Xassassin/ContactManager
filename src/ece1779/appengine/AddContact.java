package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Detail;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.PersonDAO;

public class AddContact extends HttpServlet {


	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");
            String email = req.getParameter("email");
            //PrintWriter out = resp.getWriter();
            //out.println("name is " + name);
            if (name != null) {
                PersonDAO pao = new PersonDAO();
                Person person = pao.getPerson(user.getUserId());
                
            	if (person != null) {
            		Contact a_contact = new Contact();
            		a_contact.setName(name);
            		
                    Detail detail1 = new Detail();
                    detail1.setItem("TEL");
                    detail1.setCategory(Detail.PHONE);
                    if (phone != null) {
                    	detail1.setValue(phone);
                    } else {
                    	detail1.setValue(" ");
                    }
                    a_contact.getDetail().add(detail1);
                    
                    Detail detail2 = new Detail();
                    detail2.setItem("ADR");
                    detail2.setCategory(Detail.ADDRESS);
                    if (address != null) {
                    	detail2.setValue(address);
                    } else {
                    	detail2.setValue(" ");
                    }
                    a_contact.getDetail().add(detail2);
                    
                    Detail detail3 = new Detail();
                    detail3.setItem("Email");
                    detail3.setCategory(Detail.OTHER);
                    if (email != null) {
                    	detail3.setValue(email);
                    } else {
                    	detail3.setValue(" ");
                    }
                    a_contact.getDetail().add(detail3);
                    
                    person.getContacts().add(a_contact);
            	
                    pao.savePerson(person);
            	}
            }

            resp.sendRedirect("home.jsp");
        } else {
            resp.sendRedirect("Login");
        }
    }
}
