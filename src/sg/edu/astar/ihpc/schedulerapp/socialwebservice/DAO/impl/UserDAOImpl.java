package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOConstant;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.UserDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility.DBUtility;

public class UserDAOImpl implements UserDAO {

	private static final String SQL_GET_ALL_USERS = "select ID, ORG_ID, NAME, GENDER, "
			+ "FAMILY_NAME, GIVEN_NAME, IMAGE_URL, EMAIL, LOCALE, LINK, SOURCE "
			+ "from USER";
	private static final String SQL_GET_FRIENDS_BY_EMAIL = "select ID, ORG_ID, NAME, LOGIN_TIME, GENDER, "
			+ "FAMILY_NAME, GIVEN_NAME, IMAGE_URL, BIRTHDAY, EMAIL, LOCALE, LINK, SOURCE "
			+ "from USER "
			+ "where ID in (select FRIEND_ID "
			+ "from USER_RELATIONSHIP "
			+ "where USER_ID = (select ID "
			+ "from USER " + "where email=?))";
	private static final String SQL_GET_USER_BY_NAME_AND_PASSWORD = "select ID, ORG_ID, NAME, LOGIN_TIME, GENDER, "
			+ "FAMILY_NAME, GIVEN_NAME, IMAGE_URL, BIRTHDAY, EMAIL, LOCALE, LINK, SOURCE "
			+ "from USER " + "where NAME=? and PASSWORD=?";
	private static final String SQL_UPDATE_USER_LOGIN_TIME = "update USER set LOGIN_TIME=? " 
			+ "where NAME=?";

	public List<User> getAllUsers() throws Exception {
		List<User> users = new ArrayList<User>();
		Connection conn = null;
		try {
			conn = DBUtility.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL_GET_ALL_USERS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				users.add(toUser(rs, DAOConstant.TO_USER));
			}
		} catch (SQLException e) {
			System.out.println("UserDAOImpl getAllUsers SQLException Error! SQLException");
			throw new SQLException();
		} catch (Exception e) {
			if (!(e instanceof SQLException)){
				System.out.println("UserDAOImpl getAllUsers SQLException Error!");
			}
			throw new Exception();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}

	public List<User> getFriendsByEmail(String email)
			throws Exception {
		List<User> friends = new ArrayList<User>();
		Connection conn = null;
		try {
			conn = DBUtility.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement(SQL_GET_FRIENDS_BY_EMAIL);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				friends.add(toUser(rs, DAOConstant.TO_FRIEND));
			}
		} catch (SQLException e) {
			System.out.println("UserDAOImpl getFriendsByEmail Error! SQLException");
			throw new SQLException();
		} catch (Exception e) {
			if (!(e instanceof SQLException)){
				System.out.println("UserDAOImpl getFriendsByEmail Error!");
			}
			throw new Exception();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return friends;
	}

	public User authenticate(String username, String password)
			throws Exception {
		User user = null;
		Connection conn = null;
		try {
			conn = DBUtility.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement(SQL_GET_USER_BY_NAME_AND_PASSWORD);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user = toUser(rs, DAOConstant.TO_SELF);
			}
		} catch (SQLException e) {
			System.out.println("UserDAOImpl authenticate SQLException Error! SQLException");
			throw new SQLException();
		} catch (Exception e) {
			if (!(e instanceof SQLException)){
				System.out.println("UserDAOImpl authenticate SQLException Error!");
			}
			throw new Exception();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}

	public void updateUserLoginTime(String username, Timestamp timestamp)
			throws Exception {
		Connection conn = null;
		try {
			conn = DBUtility.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement(SQL_UPDATE_USER_LOGIN_TIME);
			stmt.setTimestamp(1, timestamp);
			stmt.setString(2, username);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("UserDAOImpl updateUserLoginTime Error! SQLException");
			throw new SQLException();
		} catch (Exception e) {
			if (!(e instanceof SQLException)){
				System.out.println("UserDAOImpl updateUserLoginTime Error!");
			}
			throw new Exception();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<User> getUsersByEmail(String email) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public User getUserByEmail(String email) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<User> getUsersByEmails(List<String> emails) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private User toUser(ResultSet rs, int userType) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("ID"));
		user.setOrgId(rs.getString("ORG_ID"));
		user.setName(rs.getString("NAME"));
		user.setGender(rs.getString("GENDER"));
		user.setFamilyName(rs.getString("FAMILY_NAME"));
		user.setGivenName(rs.getString("GIVEN_NAME"));
		user.setImageUrl(rs.getString("IMAGE_URL"));
		user.setEmail(rs.getString("EMAIL"));
		user.setLocale(rs.getString("LOCALE"));
		user.setLink(rs.getString("LINK"));
		user.setSource(rs.getString("SOURCE"));
		if (userType == DAOConstant.TO_FRIEND
				|| userType == DAOConstant.TO_SELF) {
			user.setLoginTime(rs.getTimestamp("LOGIN_TIME"));
			user.setBirthday(rs.getDate("BIRTHDAY"));
		}
		return user;
	}

}
