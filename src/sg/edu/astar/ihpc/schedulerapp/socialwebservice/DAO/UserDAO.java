package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO;

import java.sql.Timestamp;
import java.util.List;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;

public interface UserDAO {

	public List<User> getAllUsers() throws Exception;
	public List<User> getUsersByEmail(String email) throws Exception;
	public List<User> getFriendsByEmail(String email) throws Exception;
	public List<User> getUsersByEmails(List<String> emails) throws Exception;
	public User getUserByEmail(String email) throws Exception;
	public User authenticate(String username, String password) throws Exception;
	public void updateUserLoginTime(String username, Timestamp timestamp) throws Exception;
	
}
