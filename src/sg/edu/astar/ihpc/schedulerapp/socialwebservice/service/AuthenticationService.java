package sg.edu.astar.ihpc.schedulerapp.socialwebservice.service;

import java.sql.Timestamp;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOConstant;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOFactory;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.UserDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;

public class AuthenticationService {

	private UserDAO userDAO;

	public AuthenticationService() {
		// userDAO = DAOFactory.getUserDAO(DAOConstant.DAO_JDBC_IMPLEMENTATION);
		userDAO = DAOFactory.getUserDAO(DAOConstant.DAO_HIBERNATE_IMPLEMENTATION);
	}
	
	public User authenticate(String username, String password) {
		User user = null;
		try {
			//1. login authentication
			user = getUserByNameAndPassword(username, password);
//			if(user != null){
//				updateUserLoginTime(user.getName());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	private User getUserByNameAndPassword(String username, String password) throws Exception {
		if((username == null || "".equals(username)) || (password == null || "".equals(password))){
			return null;
		}
		User user = null;
		try {
			user = userDAO.authenticate(username, password);
		} catch (Exception e) {
			System.out.println("1. login authentication Error!");
			throw new Exception();
		}
		return user;
	}
	
	private void updateUserLoginTime(String username) throws Exception {
		if(username == null || "".equals(username)) {
			return;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			userDAO.updateUserLoginTime(username, timestamp);
		} catch (Exception e) {
			System.out.println("2. update LOGIN_TIME Error!");
			throw new Exception();
		}
	}

}
