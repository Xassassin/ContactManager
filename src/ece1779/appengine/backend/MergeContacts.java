package ece1779.appengine.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apphosting.api.DeadlineExceededException;

import ece1779.appengine.dto.Contact;
import ece1779.appengine.dto.Detail;
import ece1779.appengine.dto.Person;
import ece1779.appengine.jpa.PersonDAO;

@SuppressWarnings("serial")
public class MergeContacts extends HttpServlet {

	private static final Logger log = Logger.getLogger(MergeContacts.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (req.getParameter("person") == null) {
			log.info("Task ran, no person passed in.");
		} else {
			String userId = req.getParameter("person");
			log.info("Meging Contacts for " + userId);
			PersonDAO pao = new PersonDAO();
			Person person = pao.getPerson(userId);
			if (person != null) {
				List<Contact> contacts = person.getContacts();
				List<Contact> removedContacts = new ArrayList<Contact>();

				if (contacts != null && !contacts.isEmpty()) {
					for (Contact contact : contacts) {
						for (Contact contact2 : contacts) {
							if (contact.getName() == contact2.getName()) {
								if (contact.getId() != contact.getId()) {
									mergeContacts(contact, contact2);
									removedContacts.add(contact2);
								}
							}

						}
					}
					
					person.getContacts().removeAll(removedContacts);
					pao.savePerson(person);

				} else {
					log.info("User " + userId
							+ " does not have contacts. Merge is finished.");
				}
			} else {
				log.info("User " + userId + " does not exist.");
			}

		}
	}

	private void mergeContacts(Contact contact1, Contact contact2) {
		List<Detail> newDetails = new ArrayList<Detail>();
		for (Detail detail1 : contact1.getDetail()) {
			int count = 0;
			for (Detail detail2 : contact2.getDetail()) {
				if (detail1.getItem().equalsIgnoreCase(detail2.getItem())) {
					if (detail1.getCategory() == detail2.getCategory()) {
						if (!detail1.getValue().equalsIgnoreCase(
								detail2.getValue())) {
							count++;
							Detail newDetail = new Detail();
							newDetail.setCategory(detail2.getCategory());
							newDetail.setItem(detail2.getItem() + count);
							newDetail.setValue(detail2.getValue());
							newDetails.add(newDetail);
						}
					}
				}
			}
		}
		
		for (Detail detail : contact2.getDetail()) {
			if (!newDetails.contains(detail)) {
				Detail newDetail = new Detail();
				newDetail.setCategory(detail.getCategory());
				newDetail.setItem(detail.getItem());
				newDetail.setValue(detail.getValue());
				newDetails.add(newDetail);
			}
		}
		
		contact1.getDetail().addAll(newDetails);
	}

}
