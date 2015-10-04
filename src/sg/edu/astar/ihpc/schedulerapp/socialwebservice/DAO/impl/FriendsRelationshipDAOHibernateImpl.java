package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.FriendsRelationshipDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.FriendRequestBuffer;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility.HibernateUtil;

public class FriendsRelationshipDAOHibernateImpl implements
		FriendsRelationshipDAO {

	public boolean checkAlreadyBuffered(FriendRequestBuffer friendRequestBuffer)
			throws Exception {
		return checkAlreadyBuffered(friendRequestBuffer
				.getSendRequestUserEmail(), friendRequestBuffer
				.getReceiveRequestUserEmail())
				|| checkAlreadyBuffered(friendRequestBuffer
						.getReceiveRequestUserEmail(), friendRequestBuffer
						.getSendRequestUserEmail());
	}

	public boolean checkAlreadyBuffered(String sendRequestUserEmail,
			String receiveRequestUserEmail) throws Exception {
		boolean isAlreadyFriend = false;
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		FriendRequestBuffer friendRequest = getFriendRequestBuffer(session, sendRequestUserEmail, receiveRequestUserEmail);
		tr.commit();
		session.close();
		if (friendRequest != null) {
			isAlreadyFriend = true;
		}
		return isAlreadyFriend;
	}
	
	private FriendRequestBuffer getFriendRequestBuffer(Session session,  
			String sendRequestUserEmail, String receiveRequestUserEmail) throws Exception {
		String hql = "from FriendRequestBuffer where sendRequestUserEmail=? and receiveRequestUserEmail=?";
		Query query = session.createQuery(hql);
		query.setString(0, sendRequestUserEmail);
		query.setString(1, receiveRequestUserEmail);
		FriendRequestBuffer friendRequest = (FriendRequestBuffer) query
				.uniqueResult();
		return friendRequest;
	}

	public void storeFriendRequestData(FriendRequestBuffer friendRequestBuffer)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		session.save(friendRequestBuffer);
		tr.commit();
		session.close();
	}

	public List<String> getReceivedReqEmails(String email) throws Exception {
		List<String> receivedReqEmails = new ArrayList<String>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from FriendRequestBuffer where receiveRequestUserEmail=?";
		Query query = session.createQuery(hql);
		query.setString(0, email);
		List<FriendRequestBuffer> friendRequestBuffer = (List<FriendRequestBuffer>) query.list();
		tr.commit();
		session.close();
		for(FriendRequestBuffer fb : friendRequestBuffer) {
			receivedReqEmails.add(fb.getSendRequestUserEmail());
		}
		return receivedReqEmails;
	}

	public List<String> getSentReqEmails(String email) throws Exception {
		List<String> sentReqEmails = new ArrayList<String>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from FriendRequestBuffer where sendRequestUserEmail=?";
		Query query = session.createQuery(hql);
		query.setString(0, email);
		List<FriendRequestBuffer> friendRequestBuffer = (List<FriendRequestBuffer>) query.list();
		tr.commit();
		session.close();
		for(FriendRequestBuffer fb : friendRequestBuffer) {
			sentReqEmails.add(fb.getReceiveRequestUserEmail());
		}
		return sentReqEmails;
	}
	
	private void clearBuffer(Session session,  
			String requestEmail, String myEmail) throws Exception {
		FriendRequestBuffer friendRequest = getFriendRequestBuffer(session, requestEmail, myEmail);
		session.delete(friendRequest);
	}

	public void clearBuffer(String myEmail, String requestEmail)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		clearBuffer(session, requestEmail, myEmail);
		tr.commit();
		session.close();
	}
	
	private void storeRelationship(Session session, 
			String myEmail, String requestEmail) throws Exception {
		String hql = "from User where email=?";
		Query querySelf = session.createQuery(hql);
		querySelf.setString(0, myEmail);
		User self = (User) querySelf.uniqueResult();
		Query queryRequest = session.createQuery(hql);
		queryRequest.setString(0, requestEmail);
		User request = (User) queryRequest.uniqueResult();
		self.getFriends().add(request);
		request.getFriends().add(self);
		session.saveOrUpdate(self);
		session.saveOrUpdate(request);
	}

	public void clearBufferAndStoreRelationship(String myEmail,
			String requestEmail) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		// 1. store friend relationship
		storeRelationship(session, myEmail, requestEmail);
		// 2. clear friend request buffer
		clearBuffer(session, requestEmail, myEmail);
		tr.commit();
		session.close();
	}

	public void delete(String myEmail, String requestEmail) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where email=?";
		Query querySelf = session.createQuery(hql);
		querySelf.setString(0, myEmail);
		User self = (User) querySelf.uniqueResult();
		Query queryRequest = session.createQuery(hql);
		queryRequest.setString(0, requestEmail);
		User request = (User) queryRequest.uniqueResult();
		for(User f : self.getFriends()) {
			if(requestEmail.equalsIgnoreCase(f.getEmail())) {
				self.getFriends().remove(f);
				break;
			}
		}
		for(User f : request.getFriends()) {
			if(myEmail.equalsIgnoreCase(f.getEmail())) {
				request.getFriends().remove(f);
				break;
			}
		}
		session.saveOrUpdate(self);
		session.saveOrUpdate(request);
		tr.commit();
		session.close();
	}

}
