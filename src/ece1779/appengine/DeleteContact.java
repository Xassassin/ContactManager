package ece1779.appengine;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.ContactDAO;
import ece1779.appengine.jpa.PersonDAO;

public class DeleteContact extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            // PrintWriter out = resp.getWriter();
            String index = req.getParameter("index");
            ContactDAO cao = new ContactDAO();
            Contact contact = cao.getContact(index);

            PersonDAO pao = new PersonDAO();
            Person person = pao.getPerson(user.getUserId());

            if ((person != null) && (contact != null)) {
                // txn.begin();
                // try {
                int indexOfContact = person.getContacts().indexOf(contact);
                person.getContacts().remove(indexOfContact);
                pao.savePerson(person);
            }

            resp.sendRedirect("home.jsp");
        } else {
            resp.sendRedirect("Login");
        }
    }
}
