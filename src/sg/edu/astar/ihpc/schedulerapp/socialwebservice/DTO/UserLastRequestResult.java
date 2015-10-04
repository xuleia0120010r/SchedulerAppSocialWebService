package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.adapter.TimestampAdapter;

@XmlRootElement
public class UserLastRequestResult {

	private Long id;
	//private Integer userId;
	private String task;
	private String companyName;
	private String category;
	private String address;
	private String postalCode;
	private String latLng;
	private String suburb;
	private String sourceUrl;
	private BigDecimal ratingValue;
	private Integer numberOfReviewer;
	private Timestamp taskTime;
	private Timestamp lastUpdateTime;
	
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Integer getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Integer userId) {
//		this.userId = userId;
//	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLatLng() {
		return latLng;
	}

	public void setLatLng(String latLng) {
		this.latLng = latLng;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public BigDecimal getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(BigDecimal ratingValue) {
		this.ratingValue = ratingValue;
	}

	public Integer getNumberOfReviewer() {
		return numberOfReviewer;
	}

	public void setNumberOfReviewer(Integer numberOfReviewer) {
		this.numberOfReviewer = numberOfReviewer;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Timestamp taskTime) {
		this.taskTime = taskTime;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
