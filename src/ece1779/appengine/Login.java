package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp)
        throws IOException {

    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
    
        if (user == null) {
        	out.println("<html>");
        	out.println("<head>");
        	out.println("	<title>vCard Manager - Login</title>");
        	out.println("	<style type='text/css'>");
        	out.println("	td#hover:hover{background-color:#819FF7;}");
        	out.println("	td:hover#highlight{background-color:#819AA7;}");
        	out.println("	td:hover#highlight2{background-color:#819AA7;}");
        	out.println("	div.hidden {visibility:hidden;}");
        	out.println("	table");
        	out.println("		{");
        	out.println("			border: 0px solid white;");
        	out.println("			table-layout:fixed;");
        	out.println("			border-spacing:0;");
        	out.println("			border-collapse:collapse;");
        	out.println("		}");
        	out.println("		td");
        	out.println("		{");
        	out.println("			border: 0px solid #202020;");
        	out.println("		}");
        	out.println("		td#highlight, td#highlight3");
        	out.println("		{");
        	out.println("			border-style: solid;");
        	out.println("			border-top-width:2px;");
        	out.println("			border-bottom-width:2px;");
        	out.println("			border-left-width:0px;");
        	out.println("			border-right-width:0px;");
        	out.println("			border-top-color:black;");
        	out.println("			border-bottom-color:black;");
        	out.println("		}");
        	out.println("	body");
        	out.println("	{");
        	out.println("		text-align: center;");
        	out.println("		font-family: 'Lucida Grande', 'Lucida Sans Unicode', Verdana, Helvetica, Arial, sans-serif;");
        	out.println("		background-color: #202020;");
        	out.println("		color: #444;");
        	out.println("		font-size: 75%;");
        	out.println("		margin:0px; ");
        	out.println("		padding:0px;");
        	out.println("	}");
        	out.println("	h1,h2{font-family: 'Trebuchet MS', Verdana, sans-serif; margin: 0 0 10px 0; letter-spacing:-1px; padding: 0;}");
        	out.println("	</style>");
        	out.println("</head>");
        	out.println("<body>");
        	out.println("<table height=600 width=900 align='center'>");
        	out.println("		<tr>");
        	out.println("			<td height=500 vertical-align=middle colspan=25 bgcolor='white' style='padding-left:25px;  padding-top:20px;'>");
        	out.println("				<img src='http://bizcard.proongo.com/b/images/PNGs/vcard.png' align='left' height='240'>");
        	out.println("				<h1><font color='black'><b>vCard Manager</b></font></h1>");
        	out.println("				<h2>An mobile contact management app</h2>");       
        	out.println("			</td>");
        	out.println("		</tr>");
        	out.println("	<tr>");
        	out.println("		<td colspan=25 bgcolor='white' valign='middle'  style='padding-top:20px; padding-bottom:0px; padding-left:25px;' id='highlight3'>");	
        	out.println("<p>Welcome! <a href=\"" + userService.createLoginURL("/") + "\">Sign in or register</a> to customize.</p>");
        	out.println("		</td>");
        	out.println("	</tr>");
        	out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } else {
        	resp.sendRedirect("home.jsp");

        }
    }
}
