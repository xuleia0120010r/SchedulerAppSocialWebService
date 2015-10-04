package sg.edu.astar.ihpc.schedulerapp.socialwebservice.junittest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.UserLastRequestResult;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.MaintainFriendService;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.SocialSuggestionService;

public class SocialSuggestionServiceJunitTest {

	private SocialSuggestionService socialSuggestionService = null;
	private MaintainFriendService maintainFriendService = null;
//	String myTasks1 = null;
//	String friendTasks1 = null;
//	String myTask2 = null;
	User user = null;
	User friend = null;
	Set<UserLastRequestResult> myUserLastRequestResults1 = null;
	Set<UserLastRequestResult> friendUserLastRequestResults1 = null;
	Set<UserLastRequestResult> myUserLastRequestResults2 = null;
	
	@Before
	public void setUp() throws Exception {
		socialSuggestionService = new SocialSuggestionService();
		maintainFriendService = new MaintainFriendService();
//		myTasks1 = "gaint|gaint|NA|vivocity|NA|1.264339,103.822308|HarbourFront|NA|100|NA";
//		friendTasks1 = "gaint|gaint|NA|vivocity|NA|1.264339,103.822308|HarbourFront|NA|100|NA";
//		myTask2 = "veg salad|rezeed|NA|NUS BIZ|NA|1.291249,103.774918|Pasir Panjang|NA|1|02-04-2015 09:00";
		user = maintainFriendService.getUsersByEmail("a0120010@nus.edu.sg").get(0);
		myUserLastRequestResults1 = new HashSet<UserLastRequestResult>();
		UserLastRequestResult userLastRequestResult1 = new UserLastRequestResult();
		userLastRequestResult1.setTask("gaint");
		userLastRequestResult1.setCompanyName("gaint");
		userLastRequestResult1.setAddress("vivocity");
		userLastRequestResult1.setLatLng("1.264339,103.822308");
		userLastRequestResult1.setSuburb("HarbourFront");
		userLastRequestResult1.setNumberOfReviewer(100);
		myUserLastRequestResults1.add(userLastRequestResult1);
		
		friend = maintainFriendService.getUsersByEmail("a0120528@nus.edu.sg").get(0);
		friendUserLastRequestResults1 = new HashSet<UserLastRequestResult>();
		UserLastRequestResult friendLastRequestResult1 = new UserLastRequestResult();
		friendLastRequestResult1.setTask("gaint");
		friendLastRequestResult1.setCompanyName("gaint");
		friendLastRequestResult1.setAddress("vivocity");
		friendLastRequestResult1.setLatLng("1.264339,103.822308");
		friendLastRequestResult1.setSuburb("HarbourFront");
		friendLastRequestResult1.setNumberOfReviewer(100);
		friendUserLastRequestResults1.add(friendLastRequestResult1);
		
		myUserLastRequestResults2 = new HashSet<UserLastRequestResult>();
		UserLastRequestResult userLastRequestResult2 = new UserLastRequestResult();
		userLastRequestResult2.setTask("veg salad");
		userLastRequestResult2.setCompanyName("rezeed");
		userLastRequestResult2.setAddress("NUS BIZ");
		userLastRequestResult2.setLatLng("1.291249,103.774918");
		userLastRequestResult2.setSuburb("Pasir Panjang");
		userLastRequestResult2.setNumberOfReviewer(1);
		myUserLastRequestResults2.add(userLastRequestResult2);
	}

	@After
	public void tearDown() throws Exception {
		socialSuggestionService = null;
		maintainFriendService = null;
//		myTasks1 = null;
//		friendTasks1 = null;
//		myTask2 = null;
		user = null;
		friend = null;
		myUserLastRequestResults1 = null;
		friendUserLastRequestResults1 = null;
		myUserLastRequestResults2 = null;
	}
	
	@Test
	public void suggestSocialData_legal1() {
		socialSuggestionService.suggestSocialData(friend, friendUserLastRequestResults1);
		assertTrue(socialSuggestionService.suggestSocialData(user, myUserLastRequestResults1).get("gaint").size()>0);
	}
	@Test
	public void suggestSocialData_legal2() {
		assertTrue(socialSuggestionService.suggestSocialData(user, myUserLastRequestResults2).keySet().size()==0);
		assertNull(socialSuggestionService.suggestSocialData(user, myUserLastRequestResults2).get("veg salad"));
	}
	@Test
	public void suggestSocialData_null() {
		assertTrue(socialSuggestionService.suggestSocialData(user, null).keySet().size()==0);
	}
	@Test
	public void suggestSocialData_illegal() {
		assertTrue(socialSuggestionService.suggestSocialData(user, new HashSet<UserLastRequestResult>()).keySet().size()==0);
	}

}
