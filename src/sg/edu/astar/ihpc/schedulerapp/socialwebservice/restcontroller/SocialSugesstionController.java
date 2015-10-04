package sg.edu.astar.ihpc.schedulerapp.socialwebservice.restcontroller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.User;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.UserLastRequestResult;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.exception.DataTransitionException;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.SocialSuggestionService;

@Path("socialsuggestion")
public class SocialSugesstionController {

	@Context
    private UriInfo uriInfo;
    @Context
    private Request request;
    @Context 
	HttpServletRequest httpRequest;

    private SocialSuggestionService socialSuggestionService = new SocialSuggestionService();
    
    private User checkAuthenticationStatus() {
		User user = (User)httpRequest.getSession().getAttribute(RestControllerConstant.USER_SESSION_NAME);
		System.out.println("--- checkAuthenticationStatus ---");
		if(user != null) {
			System.out.println("username: " + user.getName());
			System.out.println("email: " + user.getEmail());
			return user;
		}else{
			System.out.println("user = null");
			return null;
		}
	}
    
    // URI:  http://localhost:8080/SchedulerAppSocialWebService/rest/socialwebservice/socialsuggestion?userScheduleData=*
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Map<String, List<UserLastRequestResult>> suggestSocialData(@QueryParam("userScheduleData") String userScheduleData) {
    	User self = checkAuthenticationStatus();
    	if(self != null) {
    		// task(not null)|companyName(not null)|category|address(not null)|postalCode|latLng(not null)
    		// |suburb(not null)|ratingValue|numberOfReviewer|taskTime@...
        	// any field is empty, use 'NA' instead
    		System.out.println("LOG: SocialSugesstionController-suggestSocialData, userScheduleData: " + userScheduleData.trim());
    		if(userScheduleData != null && !"".equals(userScheduleData.trim())) {
    			Set<UserLastRequestResult> inputTasks = new HashSet<UserLastRequestResult>();
    			String[] tasks = userScheduleData.trim().split("@");
    			for(String task : tasks) {
    				String[] fields = task.trim().split("\\|");
    				if(fields.length != 10) {
    					System.out.println("invalid field number: " + fields.length);
    					// fault tolerance
    					continue;
    				}
    				UserLastRequestResult userLastRequest = toUserLastRequestResult(fields);
    				// fault tolerance
    				if(userLastRequest != null) {
    					inputTasks.add(userLastRequest);
    				}
    			}
    			if(inputTasks.size() > 0) {
    				return socialSuggestionService.suggestSocialData(self, inputTasks);
    			} else {
    				return new HashMap<String, List<UserLastRequestResult>>();
    			}
    		} else {
    			System.out.println("empty userScheduleData. userScheduleData: " + userScheduleData);
    			return new HashMap<String, List<UserLastRequestResult>>();
    		}
    	} else {
    		System.out.println("authentication issue or session time out.");
    		return new HashMap<String, List<UserLastRequestResult>>();
    	}
    }
    
    // task(not null)|companyName(not null)|category|address(not null)|postalCode|latLng(not null)
    // |suburb(not null)|ratingValue|numberOfReviewer
    // |taskTime(if the user not set the time, do not send, format: dd-MM-yyyy HH:mm)
    // any field is empty, use 'NA' instead
    private UserLastRequestResult toUserLastRequestResult(String[] fields) {
    	UserLastRequestResult userLastRequestResult = new UserLastRequestResult();
    	for(int i=0; i<fields.length; i++) {
    		if(i == 0 || i == 1 || i == 3 || i == 5 || i == 6) {
    			if(fields[i] == null || "".equals(fields[i].trim()) || "NA".equals(fields[i].trim())) {
    				return null;
    			}
    		}
    		try{
    			switch(i){
    				case 0: userLastRequestResult.setTask(fields[i].trim()); break;
    				case 1: userLastRequestResult.setCompanyName(fields[i].trim()); break;
    				case 2: 
    					if(fields[i] != null && !"".equals(fields[i].trim()) && !"NA".equals(fields[i].trim())) {
    						userLastRequestResult.setCategory(fields[i].trim());
    					}
    					break;
    				case 3: userLastRequestResult.setAddress(fields[i].trim()); break;
    				case 4: 
    					if(fields[i] != null && !"".equals(fields[i].trim()) && !"NA".equals(fields[i].trim())) {
    						userLastRequestResult.setPostalCode(fields[i].trim());
    					}
    					break;
    				case 5: userLastRequestResult.setLatLng(fields[i].trim()); break;
    				case 6: userLastRequestResult.setSuburb(fields[i].trim()); break;
    				case 7: 
    					if(fields[i] != null && !"".equals(fields[i].trim()) && !"NA".equals(fields[i].trim())) {
    						userLastRequestResult.setRatingValue(BigDecimal.valueOf(Double.parseDouble(fields[i].trim())));
    					}
    					break;
    				case 8: 
    					if(fields[i] != null && !"".equals(fields[i].trim()) && !"NA".equals(fields[i].trim())) {
    						userLastRequestResult.setNumberOfReviewer(Integer.parseInt(fields[i].trim()));
    					}
    					break;
    				case 9:
    					if(fields[i] != null && !"".equals(fields[i].trim()) && !"NA".equals(fields[i].trim())) {
    						SimpleDateFormat sdfTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    						Date datetime = sdfTime.parse(fields[i].trim());
    						userLastRequestResult.setTaskTime(new Timestamp(datetime.getTime()));
    					}
    					break;
    				default: throw new DataTransitionException("invalid field number: " + i + ", field value: " + fields[i].trim());
    			}
    		} catch (DataTransitionException de) {
    			System.out.println("LOG: DataTransitionException: " + de.getMessage());
    			return null;
    		} catch (Exception e) {
    			System.out.println("LOG: Exception: " + e.getMessage());
    			return null;
    		}
    	}
    	return userLastRequestResult;
    }
    
}
