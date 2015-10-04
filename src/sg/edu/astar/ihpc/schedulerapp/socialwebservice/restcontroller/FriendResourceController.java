package sg.edu.astar.ihpc.schedulerapp.socialwebservice.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.FriendRequestBuffer;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.exception.StoreDataException;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.MaintainFriendService;

public class FriendResourceController {

	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	private String email;

	private MaintainFriendService maintainFriendService;

	public FriendResourceController(UriInfo uriInfo, Request request,
			String email, MaintainFriendService maintainFriendServic) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.email = email;
		this.maintainFriendService = maintainFriendServic;
	}

	// URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends
	@GET
	@Produces( { MediaType.APPLICATION_JSON }) // MediaType.APPLICATION_XML
	public List<User> getFriendsByEmail() {
		System.out.println("LOG: "
				+ "FriendResourceController getFriendsByEmail(), email -- "
				+ email);
		if(email != null && !"".equals(email.trim())) {
			return maintainFriendService.getFriendsByEmail(email.trim());
		} else {
			return new ArrayList<User>();
		}
	}
	
	// URI: http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends/receivedRequests
	@Path("receivedRequests")
	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	public List<User> getReceivedRequests() {
		if(email != null && !"".equals(email.trim())) {
			return maintainFriendService.getReceivedRequests(email.trim());
		} else {
			return new ArrayList<User>();
		}
	}
	
	// URI: http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends/sentRequests
	@Path("sentRequests")
	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	public List<User> getSentRequests() {
		if(email != null && !"".equals(email.trim())) {
			return maintainFriendService.getSentRequests(email.trim());
		} else {
			return new ArrayList<User>();
		}
	}
	
	// URI: http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends/sendFriendRequest?requestEmail=*
	@Path("sendFriendRequest")
	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	public String sendFriendRequest(@QueryParam("requestEmail") String requestEmail){
		if(email != null && !"".equals(email.trim())) {
			System.out.println("LOG: send friend request!!!" + " my email: " + email.trim());
		} else {
			System.out.println("LOG: authentication fail! ");
			return RestControllerConstant.MSG_AUTHENTICATION_FAIL;
		}
		if(requestEmail != null && !"".equals(requestEmail.trim())) {
			System.out.println("LOG: send friend request!!!" + " requestEmail: " + requestEmail.trim());
			if(email.trim().equalsIgnoreCase(requestEmail.trim())) {
				System.out.println("LOG: invalid request email!" + 
						" sending request email and receiving request email should not be the same.");
				return RestControllerConstant.MSG_SELF_ADDING_ERROR;
			}
			FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
			friendRequestBuffer.setSendRequestUserEmail(email.trim());
			friendRequestBuffer.setReceiveRequestUserEmail(requestEmail.trim());
			try {
				maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			} catch (StoreDataException e) {
				System.out.println("LOG: StoreDataException: " + e.getMessage());
				return e.getMessage();
			} catch (Exception e) {
				System.out.println("LOG: sendFriendRequest unexpected error.");
				return RestControllerConstant.MSG_FAIL;
			}
			return RestControllerConstant.MSG_SUCCESS;
		} else {
			System.out.println("LOG: invalid request email!" + " requestEmail: " + requestEmail.trim());
			return RestControllerConstant.MSG_REQUEST_EMAIL_ERROR;
		}
	}
	
	// URI: http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends/acknowledge?requestEmail=*&acknowledge=accept(deny)
	@Path("acknowledge")
	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	public String acknowledge(@QueryParam("requestEmail") String requestEmail, @QueryParam("acknowledge") String acknowledge){
		if(email != null && !"".equals(email.trim())) {
			System.out.println("LOG: acknowledge!!!" + " my email: " + email.trim());
		} else {
			System.out.println("LOG: authentication fail! ");
			return RestControllerConstant.MSG_AUTHENTICATION_FAIL;
		}
		if (requestEmail == null || "".equals(requestEmail.trim())) {
			return RestControllerConstant.MSG_REQUEST_EMAIL_ERROR;
		}
		System.out.println("LOG: acknowledge!!!" + " requestEmail: " + requestEmail.trim());
		if (!"accept".equals(acknowledge.trim()) && !"deny".equals(acknowledge.trim())) {
			return RestControllerConstant.MSG_ACKNOWLEDGE_INFO_ERROR;
		}
		try {
			maintainFriendService.acknowledge(email.trim(), requestEmail.trim(), acknowledge.trim());
		} catch (StoreDataException e) {
			System.out.println("LOG: StoreDataException: " + e.getMessage());
			return e.getMessage();
		} catch (Exception e) {
			System.out.println("LOG: acknowledge unexpected error.");
			return RestControllerConstant.MSG_FAIL;
		}
		return RestControllerConstant.MSG_SUCCESS;
	}
	
	// URI: http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/friends/myfriends/delete?requestEmail=*
	@Path("delete")
	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	public String delete(@QueryParam("requestEmail") String requestEmail) {
		if(email != null && !"".equals(email.trim())) {
			System.out.println("LOG: acknowledge!!!" + " my email: " + email.trim());
		} else {
			System.out.println("LOG: authentication fail! ");
			return RestControllerConstant.MSG_AUTHENTICATION_FAIL;
		}
		if (requestEmail == null || "".equals(requestEmail.trim())) {
			return RestControllerConstant.MSG_REQUEST_EMAIL_ERROR;
		}
		System.out.println("LOG: delete!!!" + " requestEmail: " + requestEmail.trim());
		try {
			maintainFriendService.delete(email.trim(), requestEmail.trim());
		} catch (StoreDataException e) {
			System.out.println("LOG: StoreDataException: " + e.getMessage());
			return e.getMessage();
		} catch (Exception e) {
			System.out.println("LOG: delete unexpected error.");
			return RestControllerConstant.MSG_FAIL;
		}
		return RestControllerConstant.MSG_SUCCESS;
	}

//	 @PUT
//	 @Consumes(MediaType.APPLICATION_XML)
//	 public Response putFriend(JAXBElement<Friend> jaxbFriend) {
//		 Friend f = jaxbFriend.getValue();
//		 System.out.println("LOG: "
//				 + "FriendResource putContact(), friendID -- " + f.getEmail()
//				 + ", friendName -- " + f.getName());
//		 return putAndGetResponse(f);
//	 }

	/*
	 * @PUT public Response putContact(@Context HttpHeaders herders, byte[] in)
	 * { Map<String, String> params = ParamUtil.parse(new String(in)); Contact c
	 * = new Contact(params.get("id"), params.get("name"), new
	 * ArrayList<Address>()); return putAndGetResponse(c); }
	 */

	// private Response putAndGetResponse(Friend f) {
	// Response res;
	// if (ContactStore.getStore().containsKey(f.getEmail())) {
	// res = Response.noContent().build();
	// } else {
	// res = Response.created(uriInfo.getAbsolutePath()).build();
	// }
	// ContactStore.getStore().put(f.getEmail(), f);
	// return res;
	// }

	// @DELETE
	// public void deleteContact() {
	// System.out.println("LOG: " +
	// "FriendsResource deleteContact(), friendID -- " + friend);
	// Friend f = ContactStore.getStore().remove(friend);
	// if (f == null)
	// throw new NotFoundException("DELETE No such Friend with id: "
	// + friend + ".");
	// }

}
