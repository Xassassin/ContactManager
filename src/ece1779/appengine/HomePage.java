package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
    	out.println("</table>");

    	out.println("<p>Welcome, " + user.getNickname() + "! You can <a href=\"" + userService.createLogoutURL("/") + "\">sign out</a>.</p>");


    	out.println("<font color='black'><b>Upload a vCard</b></font>");
    	out.println("<form action='FileUpload' enctype='multipart/form-data' method='post'>");
    	out.println("	<input type='file' name='theFile'>");
    	out.println("	<input type='submit' value='Send'>");
    	out.println("	<input type='reset'></form>");                    	

    	out.println("<hr/>");

    	out.println("<form action='deleteContact' method='post'>");
    	out.println("Enter a contact index to delete : <input type='text' name='index' align='right'>");
    	out.println("		<input type='submit' value='delete'>");
    	out.println("</form>");            	
    	out.println("<hr/>"); 
    	
    	out.println("<form action='merge' method='post'>");    	
    	out.println("Click to merge conatcs. Note, merging will happen in the background. Refresh to see changes.");    	
    	out.println("		<input type='submit' value='merge'>");    	
    	out.println("</form>");    	
    	out.println("<hr/>");
    	
        PersonDAO pao = new PersonDAO();
        
        	Person person = pao.getPerson(user.getUserId());
        	if (person == null) {
        		person = new Person(user.getUserId());
        		pao.savePerson(person);
        	}
        	
        	if (person != null) {
        		
        		List<Contact> Contacts = person.getContacts();
        		int contactSize = Contacts.size();
        		if (contactSize == 0) {
        			out.println("You have no contact. Would you like to upload a vCard file?");
        		} else {
        	    	out.println("<table border = 1 width = 900");
        	    	out.println("<tr><th>Index</th><th> Name </th><th> Phone </th><th>Address</th></tr>");
        	    	for (int i=0;i<contactSize;i++) {
            	    	Contact currentContact = Contacts.get(i);
            	    	out.println("<tr>");
            	    	out.println("	<th>"+ i +"</th>");
            	    	out.println("	<th>" + currentContact.getName() + "</th>");
        	    		List<Detail> Details = currentContact.getDetail();
        	    		if (Details.size() == 2) {
        	    			out.println("	<th>" + Details.get(0).getValue() + "</th>");
        	    			out.println("	<th>" + Details.get(1).getValue() + "</th>");
        	    		}
        	    		for (int j=2; j<Details.size();j=j+2) {
        	    			out.println("	<th></th>");
        	    			out.println("	<th></th>");
        	    			out.println("	<th>" + Details.get(j).getValue() + "</th>");
        	    			out.println("	<th>" + Details.get(j+1).getValue() + "</th>");        	    			
        	    		}
            	    	out.println("</tr>");             	    		

        	    	}
    	            out.println("</table>");	
        		}
        	}

        out.println("</body>");
        out.println("</html>");
    }
}
