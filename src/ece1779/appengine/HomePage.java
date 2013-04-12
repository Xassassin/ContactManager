package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.*;

import a_vcard.android.syncml.pim.VNode;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.EMF;

@SuppressWarnings("serial")
public class HomePage extends HttpServlet {
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp)
        throws IOException {

    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
		  
       
    	out.println("<html>");
    	out.println("<head>");
    	out.println("	<title>vCard Manager - Main</title>");
    	out.println("	<style type='text/css'>");
    	out.println("	</style>");
    	out.println("</head>");
    	out.println("<body>");        	
    	out.println("<table width=1200 align='center'>");
    	out.println("	<tr>");
    	out.println("		<td vertical-align=top bgcolor='black' >");
    	out.println("			<img src='http://bizcard.proongo.com/b/images/PNGs/vcard.png' align='right' height='40'>");
    	out.println("			<font color='white'><b>vCard Manager</b></font>");        	
    	out.println("		</td>");
    	out.println("	</tr>");
    	out.println("	<tr>");
    	out.println("		<td vertical-align=top bgcolor='white' >");
    	out.println("			<font color='black'><b>Upload a vCard</b></font>");
    	out.println("			<form action='FileUpload' enctype='multipart/form-data' method='post'>");
    	out.println("				<input type='file' name='theFile'>");
    	out.println("				<input type='submit' value='Send'>");
    	out.println("				<input type='reset'></form>");                    	
    	out.println("		</td>");
    	out.println("	</tr>");        	
    	out.println("	<tr>");
    	out.println("		<td bgcolor='white' valign='middle'>");	
    	out.println("			<p>Welcome, " + user.getNickname() + "! You can <a href=\"" + userService.createLogoutURL("/") + "\">sign out</a>.</p>");
    	out.println("		</td>");
    	out.println("	</tr>");        	
    	out.println("	<tr>");
    	out.println("		<td vertical-align=top bgcolor='white'>");          	
        EntityManager em = EMF.get().createEntityManager();	
        try {
        	Person person = em.find(Person.class, user.getNickname());
        	if (person != null) {
        		List<Contact> Contacts = person.getContacts();
        		for (Contact contact : Contacts) {
        			out.println("contact name is " + contact.getName() + "!");
        		}
        	}
        } finally {
			  em.close();
		}
    	out.println("		</td>");
    	out.println("	</tr>");
    	out.println("</table>");        	
        out.println("</body>");
        out.println("</html>");
    }
}
