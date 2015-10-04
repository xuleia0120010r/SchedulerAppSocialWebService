package sg.edu.astar.ihpc.schedulerapp.socialwebservice.service;

import java.util.ArrayList;
import java.util.List;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOConstant;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOFactory;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.FriendsRelationshipDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.UserDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.FriendRequestBuffer;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.exception.StoreDataException;

public class MaintainFriendService {

	private UserDAO userDAO;
	private FriendsRelationshipDAO friendsRelationshipDAO;

	public MaintainFriendService() {
		// userDAO = DAOFactory.getUserDAO(DAOConstant.DAO_JDBC_IMPLEMENTATION);
		userDAO = DAOFactory.getUserDAO(DAOConstant.DAO_HIBERNATE_IMPLEMENTATION);
		friendsRelationshipDAO = DAOFactory.getFriendsRelationshipDAO(DAOConstant.DAO_HIBERNATE_IMPLEMENTATION);
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		try {
			users = userDAO.getAllUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
	// maybe just a part of the whole email string
	public List<User> getUsersByEmail(String email) {
		List<User> users = new ArrayList<User>();
		if(email == null){
			return users;
		}
		try {
			users = userDAO.getUsersByEmail(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

	public List<User> getFriendsByEmail(String email) {
		List<User> friends = new ArrayList<User>();
		if(email == null) {
			return friends;
		}
		try {
			friends = userDAO.getFriendsByEmail(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friends;
	}
	
	public List<User> getReceivedRequests(String email) {
		List<User> receivedRequests = new ArrayList<User>();
		if(email == null) {
			return receivedRequests;
		}
		try {
			List<String> receivedReqEmails = friendsRelationshipDAO.getReceivedReqEmails(email);
			receivedRequests = userDAO.getUsersByEmails(receivedReqEmails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return receivedRequests;
	}
	
	public List<User> getSentRequests(String email) {
		List<User> sentRequests = new ArrayList<User>();
		if(email == null) {
			return sentRequests;
		}
		try {
			List<String> sentReqEmails = friendsRelationshipDAO.getSentReqEmails(email);
			sentRequests = userDAO.getUsersByEmails(sentReqEmails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentRequests;
	}
	
	private User getUserByEmail(String email) throws Exception {
		return userDAO.getUserByEmail(email);
	}
	
	private boolean checkAlreadyFriend(String email, String requestEmail) {
		boolean isFriend = false;
		List<User> friends = getFriendsByEmail(email);
		for(User f : friends){
			if(f.getEmail().trim().equals(requestEmail.trim())){
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}
	
	private boolean checkAlreadyBuffered(FriendRequestBuffer friendRequestBuffer) throws Exception {
		return friendsRelationshipDAO.checkAlreadyBuffered(friendRequestBuffer);
	}
	
	private boolean checkAlreadyBuffered(String sendRequestUserEmail, String receiveRequestUserEmail) throws Exception {
		return friendsRelationshipDAO.checkAlreadyBuffered(sendRequestUserEmail, receiveRequestUserEmail);
	}
	
	public void storeFriendRequestData(FriendRequestBuffer friendRequestBuffer) throws StoreDataException, Exception{
		if(friendRequestBuffer == null) {
			System.out.println("LOG: MaintainFriendService-storeFriendRequestData: friendRequestBuffer = null");
			throw new Exception("Internal Error: friendRequestBuffer = null");
		}
		// 1. check the request email exist 
		if(getUserByEmail(friendRequestBuffer.getReceiveRequestUserEmail()) != null) {
			//2. check if already friends
			if(checkAlreadyFriend(friendRequestBuffer.getSendRequestUserEmail(), friendRequestBuffer.getReceiveRequestUserEmail())){
				System.out.println("LOG: MaintainFriendService-storeFriendRequestData: friends already!");
				throw new StoreDataException(ServiceConstant.MSG_FRIENDS_ALREADY_EXCEPTION);
			}
			//3. check if exist in buffer (both direction)
			if(checkAlreadyBuffered(friendRequestBuffer)){
				System.out.println("LOG: MaintainFriendService-storeFriendRequestData: buffered already!");
				throw new StoreDataException(ServiceConstant.MSG_BUFFERED_ALREADY_EXCEPTION);
			}
			//4. store data into database
			try {
				friendsRelationshipDAO.storeFriendRequestData(friendRequestBuffer);
			} catch (Exception e) {
				System.out.println("LOG: MaintainFriendService-storeFriendRequestData: friend request fail because of DAO Exception!");
				e.printStackTrace();
				throw e;
			}
		} else {
			System.out.println("LOG: MaintainFriendService-storeFriendRequestData: friend request fail! requestEmail: " + 
					friendRequestBuffer.getReceiveRequestUserEmail() + " not exist in USER table");
			throw new StoreDataException(ServiceConstant.MSG_USER_NOT_EXIST_EXCEPTION);
		}
	}
	
	public void acknowledge(String myEmail, String requestEmail, String acknowledge) throws StoreDataException, Exception {
		System.out.println("LOG: MaintainFriendService-acknowledge: " + acknowledge);
		// check if buffered
		if(checkAlreadyBuffered(requestEmail, myEmail)) {
			if("accept".equalsIgnoreCase(acknowledge)) {
				friendsRelationshipDAO.clearBufferAndStoreRelationship(myEmail, requestEmail);
			} else if("deny".equalsIgnoreCase(acknowledge)) {
				friendsRelationshipDAO.clearBuffer(myEmail, requestEmail);
			} else {
				System.out.println("LOG: MaintainFriendService-acknowledge: invalid acknowledge!");
				throw new StoreDataException(ServiceConstant.MSG_ACKNOWLEDGE_EXCEPTION);
			}
		} else {
			System.out.println("LOG: MaintainFriendService-acknowledge: not buffered!");
			throw new StoreDataException(ServiceConstant.MSG_NOT_BUFFERED_EXCEPTION); 
		}
	}
	
	public void delete(String myEmail, String requestEmail)  throws StoreDataException, Exception {
		if(checkAlreadyFriend(myEmail, requestEmail)) {
			friendsRelationshipDAO.delete(myEmail, requestEmail);
		} else {
			System.out.println("LOG: MaintainFriendService-delete: not friends!");
			throw new StoreDataException(ServiceConstant.MSG_NOT_FRIENDS_EXCEPTION);
		}
	}

}
