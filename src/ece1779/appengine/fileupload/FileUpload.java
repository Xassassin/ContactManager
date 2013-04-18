package ece1779.appengine.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Detail;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.PersonDAO;

public class FileUpload extends HttpServlet {

    private static final long   serialVersionUID = 1L;
    private static final Logger log              = Logger.getLogger(FileUpload.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (item.isFormField()) {
                    log.warning("Got a form field: " + item.getFieldName());
                } else {
                    log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());

                    VCardParser parser = new VCardParser();
                    VDataBuilder builder = new VDataBuilder();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(stream, writer);
                    String vcardString = writer.toString();
                    // parse the string

                    boolean parsed = parser.parse(vcardString, "UTF-8", builder);

                    if (!parsed) {
                        throw new VCardException("Could not parse vCard file: " + item.getName());
                    }

                    // get all parsed contacts
                    List<VNode> pimContacts = builder.vNodeList;
                    PersonDAO pao = new PersonDAO();
                    // try {

                    Person person = pao.getPerson(user.getUserId());

                    // if (person == null) {
                    // person = new Person(user.getNickname());
                    // em.persist(person);
                    // }

                    if (person != null) {
                        // txn.begin();
                        // try {
                        // person = new Person(user.getNickname());
                        for (VNode contact : pimContacts) {
                            ArrayList<PropertyNode> props = contact.propList;
                            Contact a_contact = new Contact();
                            for (PropertyNode prop : props) {
                                if ("FN".equals(prop.propName)) {
                                    a_contact.setName(prop.propValue);
                                } else if ("TEL".equals(prop.propName)) {
                                    Detail detail1 = new Detail();
                                    detail1.setItem("TEL");
                                    detail1.setCategory(Detail.PHONE);
                                    detail1.setValue(prop.propValue);
                                    a_contact.getDetail().add(detail1);
                                } else if ("ADR".equals(prop.propName)) {
                                    Detail detail2 = new Detail();
                                    detail2.setItem("ADR");
                                    detail2.setCategory(Detail.ADDRESS);
                                    detail2.setValue(prop.propValue);
                                    a_contact.getDetail().add(detail2);
                                } else {
                                    Detail detail2 = new Detail();
                                    detail2.setItem(prop.propName);
                                    detail2.setCategory(Detail.OTHER);
                                    detail2.setValue(prop.propValue);
                                    a_contact.getDetail().add(detail2);
                                }
                            }
                            person.getContacts().add(a_contact);
                        }

                        pao.savePerson(person);
                        pao.getPerson(user.getUserId());

                    }

                }
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        resp.sendRedirect("home.jsp");
    }
}