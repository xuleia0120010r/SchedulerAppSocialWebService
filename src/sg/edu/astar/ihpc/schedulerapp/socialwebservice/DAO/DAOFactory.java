package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl.FriendsRelationshipDAOHibernateImpl;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl.SocialSuggestionDAOHibernateImpl;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl.UserDAOHibernateImpl;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.impl.UserDAOImpl;

public class DAOFactory {

	public static UserDAO getUserDAO(int type) {
		switch (type) {
			case DAOConstant.DAO_JDBC_IMPLEMENTATION: return new UserDAOImpl(); 
			case DAOConstant.DAO_HIBERNATE_IMPLEMENTATION: return new UserDAOHibernateImpl();
			default: return new UserDAOHibernateImpl();
		}
	}
	
	public static FriendsRelationshipDAO getFriendsRelationshipDAO(int type) {
		switch (type) {
			//case DAOConstant.DAO_JDBC_IMPLEMENTATION: 
			case DAOConstant.DAO_HIBERNATE_IMPLEMENTATION: return new FriendsRelationshipDAOHibernateImpl();
			default: return new FriendsRelationshipDAOHibernateImpl();
		}
	}
	
	public static SocialSuggestionDAO getSocialSuggestionDAO(int type) {
		switch (type) {
			case DAOConstant.DAO_HIBERNATE_IMPLEMENTATION: return new SocialSuggestionDAOHibernateImpl();
			default: return new SocialSuggestionDAOHibernateImpl();
		}
	}

}
