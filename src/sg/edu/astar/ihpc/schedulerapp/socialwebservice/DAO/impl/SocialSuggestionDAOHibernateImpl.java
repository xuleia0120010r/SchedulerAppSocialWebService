package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.SocialSuggestionDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.UserLastRequestResult;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility.HibernateUtil;

public class SocialSuggestionDAOHibernateImpl implements SocialSuggestionDAO {

	public List<UserLastRequestResult> getFriendsTasksWithTimeConstraint(User user)
			throws Exception {
		List<UserLastRequestResult> result = new ArrayList<UserLastRequestResult>();
		List<UserLastRequestResult> buffer = new ArrayList<UserLastRequestResult>();
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where id=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, user.getId());
		User userPersist = (User) query.uniqueResult();
		for(User friend : userPersist.getFriends()) {
			for(UserLastRequestResult ulrr : friend.getUserLastRequests()) {
				// avoid lazy-loading
				ulrr.getUser().getName();
			}
			buffer.addAll(friend.getUserLastRequests());
		}
		tr.commit();
		session.close();
		for (UserLastRequestResult ulrr : buffer) {
			if(timeConstraintSameDay(ulrr.getLastUpdateTime())) {
				dataFilter(ulrr);
				result.add(ulrr);
			} 
		}
		return result;
	}
	
	private boolean timeConstraintSameDay(Timestamp lastUpdateTime) {
		boolean isInSameDay = false;
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
		Date date = new Date();
		String currentDateStr = sdfDate.format(date);
		String lastUpdateTimeStr = sdfDate.format(new Date(lastUpdateTime.getTime()));
		System.out.println("LOG SocialSuggestionDAOHibernateImpl current time: " + currentDateStr + 
				", lastUpdateTime: " + lastUpdateTimeStr);
		if (currentDateStr.equals(lastUpdateTimeStr)) {
			isInSameDay = true;
		}
		return isInSameDay;
	}
	
	private boolean timeConstraint(Timestamp lastUpdateTime) {
		boolean isWithTimeConstraint = false;
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.UK);
		Date date = new Date();
		Timestamp currentTime = new Timestamp(date.getTime());
		// System.out.println("LOG current time: " + sdfDate.format(date));
		Calendar cal = Calendar.getInstance();
		// default 24 hours back
		cal.add(Calendar.HOUR, -24);
		Timestamp constraintTime = new Timestamp(cal.getTime().getTime());
		System.out.println("LOG SocialSuggestionDAOHibernateImpl current time: " + sdfDate.format(date) + 
				", constraint time: " + sdfDate.format(cal.getTime()) + 
				", last update time: " + sdfDate.format(lastUpdateTime));
		if(lastUpdateTime.after(constraintTime) && lastUpdateTime.before(currentTime)) {
			isWithTimeConstraint = true;
		}
		return isWithTimeConstraint;
	}
	
	// avoid lazy-load when return JSON result
	private void dataFilter(UserLastRequestResult ulrr) {
		userFilterForPrivate(ulrr.getUser());
	}
	
	private void userFilterForPrivate(User u){
		// avoid lazy-loading loop
		u.setFriends(null);
		u.setUserLastRequests(null);
		
		u.setPassword(null);
		u.setCreateTime(null);
		u.setUpdateTime(null);
	}

	public void updateLastRequestResult(User user,
			Set<UserLastRequestResult> listUserLastRequestResult)
			throws Exception {
		// 1. delete all the record for the current user.
		delete(user);
		// 2. save all the record
		save(user, listUserLastRequestResult);
		// userPersist.setUserLastRequests(listUserLastRequestResult);
		// inverse=true
		// getUserLastRequestResultByUser(user);
	}

	private void delete(User user) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where id=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, user.getId());
		User userPersist = (User) query.uniqueResult();
		for (UserLastRequestResult ulrr : userPersist.getUserLastRequests()) {
			// userPersist.getUserLastRequests().remove(ulrr);
			// ulrr.setUser(null);
			session.delete(ulrr);
		}
		userPersist.setUserLastRequests(new HashSet<UserLastRequestResult>());
		tr.commit();
		session.close();
	}

	private void save(User user,
			Set<UserLastRequestResult> listUserLastRequestResult)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where id=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, user.getId());
		User userPersist = (User) query.uniqueResult();
		for (UserLastRequestResult ulrr : listUserLastRequestResult) {
			ulrr.setUser(userPersist);
			session.save(ulrr);
		}
		tr.commit();
		session.close();
	}

	// for testing
	private Set<UserLastRequestResult> getUserLastRequestResultByUser(User user)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tr = session.beginTransaction();
		String hql = "from User where id=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, user.getId());
		User userPersist = (User) query.uniqueResult();
		System.out.println("userPersist.getUserLastRequests().size(): "
				+ userPersist.getUserLastRequests().size());
		System.out
				.println("--------userPersist.getUserLastRequests() start--------");
		for (UserLastRequestResult ulrr : userPersist.getUserLastRequests()) {
			System.out.println(ulrr.getTask());
		}
		System.out
				.println("--------userPersist.getUserLastRequests() end--------");
		tr.commit();
		session.close();
		return (Set<UserLastRequestResult>) userPersist.getUserLastRequests();
	}

}
