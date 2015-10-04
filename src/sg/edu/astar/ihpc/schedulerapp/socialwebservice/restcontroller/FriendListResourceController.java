package sg.edu.astar.ihpc.schedulerapp.socialwebservice.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.MaintainFriendService;

@Path("friends")
public class FriendListResourceController {

	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;
	@Context 
	HttpServletRequest httpRequest;
	
	private MaintainFriendService maintainFriendService = new MaintainFriendService();
	
	private User checkAuthenticationStatus() {
		User user = (User)httpRequest.getSession().getAttribute(RestControllerConstant.USER_SESSION_NAME);
		System.out.println("--- checkAuthenticationStatus ---");
		if(user != null) {
			System.out.println("username: " + user.getName());
			System.out.println("email: " + user.getEmail());
			return user;
		}else{
			System.out.println("user = null");
			return null;
		}
	}

	// URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/all
	@GET
	@Path("all")
	@Produces( { MediaType.APPLICATION_JSON }) // MediaType.APPLICATION_XML
	public List<User> getAllUsers() {
		User user = checkAuthenticationStatus();
		if(user != null){
			return maintainFriendService.getAllUsers();
		} else {
			System.out.println("test return null");
			return new ArrayList<User>();
		}
	}
	
	// URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends?email=*
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	public List<User> getUsersByEmail(@QueryParam("email") String email){
		User user = checkAuthenticationStatus();
		if(user != null){
			if (email != null && !"".equals(email.trim())){
				return maintainFriendService.getUsersByEmail(email.trim());
			} else {
				System.out.println("email input invalid");
				return new ArrayList<User>();
			}
		} else {
			System.out.println("authentication issue or session time out.");
			return new ArrayList<User>();
		}
	}

	// URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/count
	@GET
	@Path("count")
	@Produces( MediaType.APPLICATION_JSON )
	public String getCount() {
		int count = maintainFriendService.getAllUsers().size();
		return String.valueOf(count);
	}

	/*@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newFriend(@FormParam("friendEmail") String friendEmail,
			@FormParam("userEmail") String userEmail,
			@Context HttpServletResponse servletResponse) throws IOException {
		// build friends relationship
		
		URI uri = uriInfo.getAbsolutePathBuilder().path(userEmail).build();
		System.out.println("POST newFriend, uri path: " + uri.getPath());
		Response.created(uri).build();
		//servletResponse.sendRedirect("../pages/new_contact.html");
		System.out.println("create friends success!");
		servletResponse.sendRedirect("../new_friend.html");
	}*/

	// URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends
	@Path("myfriends")//{email}
	public FriendResourceController getFriendsByEmail() {//@PathParam("email") String email
		User user = checkAuthenticationStatus();
		if(user != null){
			return new FriendResourceController(uriInfo, request, user.getEmail(), maintainFriendService);
		} else {
			System.out.println("authentication issue or session time out.");
			return new FriendResourceController(uriInfo, request, null, maintainFriendService);
		}
		
	}
	
}
