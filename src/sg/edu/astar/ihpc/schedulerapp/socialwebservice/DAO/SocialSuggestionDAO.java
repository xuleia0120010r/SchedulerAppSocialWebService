package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO;

import java.util.List;
import java.util.Set;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.UserLastRequestResult;

public interface SocialSuggestionDAO {

	public void updateLastRequestResult(User user, Set<UserLastRequestResult> listUserLastRequestResult) throws Exception;
	public List<UserLastRequestResult> getFriendsTasksWithTimeConstraint(User user) throws Exception;
	
}
