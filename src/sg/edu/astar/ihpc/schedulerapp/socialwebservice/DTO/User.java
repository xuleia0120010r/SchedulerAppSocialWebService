package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.adapter.*;

@XmlRootElement
public class User {

	private Integer id;
	private String orgId;
	private String name;
	private String password;
	private String token;
	private Timestamp loginTime;
	private String gender;
	private String familyName;
	private String givenName;
	private String imageUrl;
	private Date birthday;
	private String email;
	private String locale;
	private String link;
	private String source;
	private Timestamp createTime;
	private Timestamp updateTime;

	private Set<User> friends = new HashSet<User>();
	private Set<UserLastRequestResult> userLastRequests = new HashSet<UserLastRequestResult>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@XmlJavaTypeAdapter(SQLDateAdapter.class)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	public Set<UserLastRequestResult> getUserLastRequests() {
		return userLastRequests;
	}

	public void setUserLastRequests(Set<UserLastRequestResult> userLastRequests) {
		this.userLastRequests = userLastRequests;
	}

}
