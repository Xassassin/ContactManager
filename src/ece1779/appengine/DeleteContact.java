package ece1779.appengine;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.PersonDAO;

public class DeleteContact extends HttpServlet {
 
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req,
                      HttpServletResponse resp)
        throws IOException {

//		PrintWriter out = resp.getWriter();
        String index = req.getParameter("index");
        boolean error = false;
        int contactIndex = -1;
		try {
			contactIndex = Integer.parseInt(index);
		} catch (NumberFormatException nfe) {
			// ignore
			error = true;
		} 
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
		if (!error) {
//			try {
				PersonDAO pao = new PersonDAO();
				Person person = pao.getPerson(user.getUserId());
				
				if ((person != null) && (contactIndex >=0) && (contactIndex < person.getContacts().size())) {
//					txn.begin();
//					try {
						person.getContacts().remove(contactIndex);
						//out.println("remove" + contactIndex + "!");
						pao.savePerson(person);
//						em.persist(person);
//						txn.commit();
//					} finally {
//						if (txn.isActive()) {
//							txn.rollback();
//						}
//					}
				}
//			} finally {
//				em.close();
//			}
 
		}
		resp.sendRedirect("HomePage");
    }
}
