package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.UserDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility.HibernateUtil;

public class UserDAOHibernateImpl implements UserDAO {

	/*
	 * private static final String HQL_USER_PUBLIC_INFO =
	 * "select u.id, u.orgId, u.name, u.gender, " +
	 * "u.familyName, u.givenName, u.imageUrl, u.email, u.locale, u.link " +
	 * "from User u"; 
	 * private static final String HQL_USER_PRIVATE_INFO =
	 * "select u.id, u.orgId, u.name, u.loginName, u.gender, " +
	 * "u.familyName, u.givenName, u.imageUrl, u.birthday, u.email, u.locale, u.link, u.source "
	 * + "from User u";
	 */

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() throws Exception {
		List<User> users = new ArrayList<User>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User";
		Query query = session.createQuery(hql);
		List<User> usersQuery = query.list();
		tr.commit();		
		for (User u : usersQuery) {
			// avoid lazy-loading
			// u.setFriends(null);
			userFilerForPublic(u);
			users.add(u);
		}
		session.close();
		return users;
	}
	
	public List<User> getUsersByEmail(String email) throws Exception {
		List<User> users = new ArrayList<User>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where email like '%" + email + "%'";
		Query query = session.createQuery(hql);
		List<User> usersQuery = query.list();
		tr.commit();		
		for (User u : usersQuery) {
			// avoid lazy-loading
			// u.setFriends(null);
			userFilerForPublic(u);
			users.add(u);
		}
		session.close();
		return users;
	}

	public List<User> getFriendsByEmail(String email) throws Exception {
		List<User> myFriends = new ArrayList<User>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where email=?";
		Query query = session.createQuery(hql);
		query.setString(0, email);
		User user = (User) query.uniqueResult();
		if(user != null) {
			// avoid lazy-loading
			for (User f : user.getFriends()) {
				f.getName();
			}
		}
		tr.commit();
		session.close();
		if(user != null) {
			for (User f : user.getFriends()) {
				// avoid lazy-loading loop
				// f.setFriends(null);
				userFilterForPrivate(f);
				myFriends.add(f);
			}
		}
		return myFriends;
	}
	
	public List<User> getUsersByEmails(List<String> emails) throws Exception {
		if(emails == null || emails.size() <= 0) {
			return new ArrayList<User>();
		}
		// from User where 1=2 or email=? or email=? or email=?
		StringBuilder hql = new StringBuilder("from User ");
		hql.append("where 1=2 ");
		for(String email : emails) {
			hql.append("or email=? ");
		}
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		Query query = session.createQuery(hql.toString());
		for(int i=0; i<emails.size(); i++) {
			query.setString(i, emails.get(i));
		}
		List<User> users = query.list();
		tr.commit();
		session.close();
		for(User u : users) {
			userFilerForPublic(u);
		}
		return users;
	}

	public User authenticate(String username, String password) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where name=? and password=?";
		Query query = session.createQuery(hql);
		query.setString(0, username);
		query.setString(1, password);
		User user = (User) query.uniqueResult();
		tr.commit();
		session.close();
		if(user != null){
			userFilterForPrivate(user);
		}
		return user;
	}

	public void updateUserLoginTime(String username, Timestamp timestamp)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where name=?";
		Query query = session.createQuery(hql);
		query.setString(0, username);
		User user = (User) query.uniqueResult();
		user.setLoginTime(timestamp);
		session.update(user);
		tr.commit();
		session.close();
	}
	
	public User getUserByEmail(String email) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where email=?";
		Query query = session.createQuery(hql);
		query.setString(0, email);
		User user = (User) query.uniqueResult();
		tr.commit();
		session.close();
		if(user != null) {
			userFilerForPublic(user);
		}
		return user;
	}
	
//	public void storeFriendRequestData(FriendRequestBuffer friendRequestBuffer)
//			throws Exception {
//		Session session = HibernateUtil.getSession();
//		Transaction tr = session.beginTransaction();
//		session.save(friendRequestBuffer);
//		tr.commit();
//		session.close();
//	}

	private void userFilterForPrivate(User u){
		// avoid lazy-loading loop
		u.setFriends(null);
		u.setUserLastRequests(null);
		
		u.setPassword(null);
		u.setCreateTime(null);
		u.setUpdateTime(null);
	}
	
	private void userFilerForPublic(User u){
		userFilterForPrivate(u);
		u.setLoginTime(null);
		u.setBirthday(null);
		u.setSource(null);
	}

}
