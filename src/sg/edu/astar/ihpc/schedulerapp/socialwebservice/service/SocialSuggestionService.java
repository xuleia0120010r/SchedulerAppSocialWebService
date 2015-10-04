package sg.edu.astar.ihpc.schedulerapp.socialwebservice.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOConstant;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.DAOFactory;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DAO.SocialSuggestionDAO;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.UserLastRequestResult;

public class SocialSuggestionService {

	private SocialSuggestionDAO socialSuggestionDAO;

	public SocialSuggestionService() {
		socialSuggestionDAO = DAOFactory
				.getSocialSuggestionDAO(DAOConstant.DAO_HIBERNATE_IMPLEMENTATION);
	}

	public Map<String, List<UserLastRequestResult>> suggestSocialData(
			User user, Set<UserLastRequestResult> listUserLastRequestResult) {
		Map<String, List<UserLastRequestResult>> suggestSocialData = new HashMap<String, List<UserLastRequestResult>>();
		if(listUserLastRequestResult == null || listUserLastRequestResult.size() == 0) {
			return suggestSocialData;
		}
		try {
			// 1. get friend routing tasks with update time constraint.
			List<UserLastRequestResult> selectedRequestsForMatching = socialSuggestionDAO
					.getFriendsTasksWithTimeConstraint(user);
			// 2. process matching algorithm.
			suggestSocialData = processMatchingAlgorithm(listUserLastRequestResult, selectedRequestsForMatching);
			// 3. update(delete and insert) the latest request data
			socialSuggestionDAO.updateLastRequestResult(user, listUserLastRequestResult);
		} catch (Exception e) {
			System.out.println("LOG: SocialSuggestionService-suggestSocialData unexpected error. " + e.getMessage());
		}
		return suggestSocialData;
	}

	private Map<String, List<UserLastRequestResult>> processMatchingAlgorithm(
			Set<UserLastRequestResult> listUserLastRequestResult,
			List<UserLastRequestResult> selectedRequestsForMatching) {
		Map<String, List<UserLastRequestResult>> matchingResult = new HashMap<String, List<UserLastRequestResult>>();
		for (UserLastRequestResult ulrr : listUserLastRequestResult) {
			List<UserLastRequestResult> oneRequestMatchingList = new ArrayList<UserLastRequestResult>();
			for (UserLastRequestResult selectedRequest : selectedRequestsForMatching) {
				// 1. first priority: time
				if(ulrr.getTaskTime() != null && selectedRequest.getTaskTime() != null) {
					// default 2 hours gap
					if(!timeConstraint(ulrr.getTaskTime(), 2, selectedRequest.getTaskTime())) {
						continue;
					}
				}
				// 2. second priority: location
				String[] latlng1 = ulrr.getLatLng().trim().split(",");
				String[] latlng2 = selectedRequest.getLatLng().trim().split(",");
				if(latlng1.length != 2 || latlng2.length != 2) {
					System.out.println("LOG SocialSuggestionService-processMatchingAlgorithm invalid latlng format. " + 
							"latlng1.length: " + latlng1.length + "latlng2.length" + latlng2.length);
					continue;
				}
				try {
					double distance = getDistance(Double.parseDouble(latlng1[1]), Double.parseDouble(latlng1[0]), 
							Double.parseDouble(latlng2[1]), Double.parseDouble(latlng2[0]));
					System.out.println("location1: " + ulrr.getLatLng() + 
							", location2: " + selectedRequest.getLatLng() + 
							"; distance: " + distance + " km.");
					// default in 2 kilometers
					if (distance < 2 && distance > 0) {
						oneRequestMatchingList.add(selectedRequest);
						continue;
					} 
				} catch (NumberFormatException ne) {
					System.out.println("LOG SocialSuggestionService-processMatchingAlgorithm " + 
							"NumberFormatException: " + ne.getMessage());
					continue;
				} catch (Exception e) {
					System.out.println("LOG SocialSuggestionService-processMatchingAlgorithm " + 
							"unexpected exception.");
					continue;
				}
				// 3. third priority: task name
				if (ulrr.getTask().contains(selectedRequest.getTask()) || 
						selectedRequest.getTask().contains(ulrr.getTask())) {
					oneRequestMatchingList.add(selectedRequest);
					continue;
				}
			}
			if(oneRequestMatchingList != null && oneRequestMatchingList.size() > 0) {
				matchingResult.put(ulrr.getTask(), oneRequestMatchingList);
			}
		}
		return matchingResult;
	}
	
	private boolean timeConstraint(Timestamp basicTime, int hoursInterval, Timestamp checkTime) {
		boolean isInTheInterval = false;
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.UK);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(basicTime.getTime());
		cal.add(Calendar.HOUR, -hoursInterval);
		Date intervalStart = cal.getTime();
		cal.setTimeInMillis(basicTime.getTime());
		cal.add(Calendar.HOUR, hoursInterval);
		Date intervalEnd = cal.getTime();
		System.out.println("LOG intervalStart: " + sdfDate.format(intervalStart) + 
				", intervalEnd: " + sdfDate.format(intervalEnd) + 
				", checkTime: " + sdfDate.format(checkTime));
		if(checkTime.after(intervalStart) && checkTime.before(intervalEnd)) {
			isInTheInterval = true;
		}
		return isInTheInterval;
	}

	/**
	 * based on googleMap algorithm to calculate the distance between two points
	 * on the map
	 * @param lon1 first point longitude
	 * @param lat1 first point latitude
	 * @param lon2 second point longitude
	 * @param lat2 second point latitude
	 * @return return the distance£¬unit: kilometer
	 * */
	private double getDistance(double lon1, double lat1, double lon2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lon1) - rad(lon2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) 
				+ Math.cos(radLat1) * Math.cos(radLat2) 
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * ServiceConstant.EARTH_RADIUS;
		s = s / 1000;
		return s;
	}

	// transfer to radian
	private double rad(double d) {
		return d * Math.PI / 180.0;
	}

}
