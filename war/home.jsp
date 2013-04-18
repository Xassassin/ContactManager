<%@page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="ece1779.appengine.dto.Contact"%>
<%@ page import="ece1779.appengine.dto.Detail"%>
<%@ page import="ece1779.appengine.dto.Person"%>
<%@ page import="ece1779.appengine.jpa.PersonDAO"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<title>vCard Manager - Main</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
	<%
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null) {
	        pageContext.setAttribute("user", user);
	%>
	<div>
		<div id="header" style="background-color: black; height: 40px">
			<div class="appTitle">
				<span
					style="color: white; font-family: sans-serif; font-weight: bold; text-align: left;">vCard
					Manager</span>
			</div>
			<img src='http://bizcard.proongo.com/b/images/PNGs/vcard.png'
				align='right' height='40' />
		</div>
		<div id="mainMessage">
			<div id="welcomeMessage" style="float: left; height: 50px;">
				<span> Hello, ${fn:escapeXml(user.nickname)}!</span>
			</div>
			<div id="signoutMessage" style="float: right; height: 50px;">
				<span><a
					href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
						out</a></span>
			</div>
		</div>
		<div id="upload" class="uploadForm">
			<span>You can upload a vCard here</span>
			<form action='FileUpload' enctype='multipart/form-data' method='post'>
				<input type='file' name='theFile' required='true'> <input
					type='submit' value='Send'> <input type='reset'>

			</form>
		</div>
		<div id="merge" class="mergeForm">
			<form action='merge' method='post'>
				<span>Click the button if you want to merge your contacts.</span> <input
					type='submit' value='Merge'>
			</form>
		</div>
		<div id="contacts">
			<%
			    PersonDAO pao = new PersonDAO();

			        Person person = pao.getPerson(user.getUserId());
			        if (person == null) {
			            person = new Person(user.getUserId());
			            pao.savePerson(person);
			        }

			        if (person != null) {

			            List<Contact> contacts = person.getContacts();
			            Collections.sort(contacts);
			            int contactSize = contacts.size();
			            if (contactSize == 0) {
			%>
			<span>You have not uploaded any contacts, please use the form
				above to start, click the add button below.</span>
			<%
			    } else {
			%>
			<span>Contacts</span>
			<ul>
				<%
				    for (int i = 0; i < contactSize; i++) {
				                    Contact currentContact = contacts.get(i);
				%>
				<li><span><form action='showDetails.jsp' method='post'>
							<input type="hidden" name="contactId"
								value="<%=KeyFactory.keyToString(currentContact.getId())%>"/><input type="submit"
								value="<%=currentContact.getName()%>">
						</form></span></li>
				<%
				    }
				%>
			</ul>
			<%
			    }
			        }
			%>
		</div>
	</div>

	<%
	    } else {
	        response.sendRedirect("Login");
	    }
	%>
</body>
</html>