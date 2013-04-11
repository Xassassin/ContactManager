package ece1779.appengine.fileupload;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Detail;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.EMF;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUpload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FileUpload.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
					log.warning("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());

					VCardParser parser = new VCardParser();
					VDataBuilder builder = new VDataBuilder();

					StringWriter writer = new StringWriter();
					IOUtils.copy(stream, writer);
					String vcardString = writer.toString();
					// parse the string

					boolean parsed = parser
							.parse(vcardString, "UTF-8", builder);

					if (!parsed) {
						throw new VCardException("Could not parse vCard file: "
								+ item.getName());
					}

					// get all parsed contacts
					List<VNode> pimContacts = builder.vNodeList;
					EntityManager em = EMF.get().createEntityManager();
					EntityTransaction txn = em.getTransaction();
					try {

						Person person = em.find(Person.class, user.getNickname());
						
						if (person == null) {
							person = new Person(user.getNickname());
							em.persist(person);
						}
						
						if (person != null) {
							txn.begin();
							try {
								person = new Person(user.getNickname());
								System.out.println("New Person Created: "
										+ user.getNickname());
								for (VNode contact : pimContacts) {
									ArrayList<PropertyNode> props = contact.propList;
									// contact name - FN property
									Contact a_contact = new Contact();
									String firstName = null;
									String lastName = null;
									String tel = null;
									String addr = null;
									for (PropertyNode prop : props) {
										System.out.println("Prop Name: "
												+ prop.propName);
										if ("FN".equals(prop.propName)) {
											firstName = prop.propValue;
										}
										if ("N".equals(prop.propName)) {
											lastName = prop.propValue;
										}
										if ("TEL".equals(prop.propName)) {
											tel = prop.propValue;
										}
										if ("ADR".equals(prop.propName)) {
											addr = prop.propValue;
										}
									}
									if (firstName != null) {
										System.out.println("Contact Name: "
												+ firstName);
										a_contact.setName(firstName);
									}

									if (lastName != null) {
										System.out
												.println("Contact Last Name: "
														+ lastName);
									}
									if (tel != null) {
										Detail detail1 = new Detail();
										detail1.setItem("TEL");
										detail1.setValue(tel);
										a_contact.getDetail().add(detail1);
										System.out.println("TEL: " + tel);
//										em.persist(detail1);
									}

									if (addr != null) {
										Detail detail2 = new Detail();
										detail2.setItem("ADR");
										detail2.setValue(addr);
										System.out.println("ADR: " + addr);
										a_contact.getDetail().add(detail2);
//										em.persist(detail2);
									}
//									em.persist(contact);
									person.getContact().add(a_contact);
								}

								em.persist(person);
								txn.commit();
							} catch (Exception exc) {
								exc.printStackTrace();
							} finally {
								if (txn.isActive()) {
									txn.rollback();
								}
							}
						}
					} finally {
						em.close();
					}

				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
		resp.sendRedirect("HomePage");
	}
}