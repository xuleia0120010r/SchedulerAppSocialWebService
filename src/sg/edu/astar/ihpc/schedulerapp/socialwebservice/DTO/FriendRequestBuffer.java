package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO;

public class FriendRequestBuffer {

	private Integer id;
	private String sendRequestUserEmail;
	private String receiveRequestUserEmail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSendRequestUserEmail() {
		return sendRequestUserEmail;
	}

	public void setSendRequestUserEmail(String sendRequestUserEmail) {
		this.sendRequestUserEmail = sendRequestUserEmail;
	}

	public String getReceiveRequestUserEmail() {
		return receiveRequestUserEmail;
	}

	public void setReceiveRequestUserEmail(String receiveRequestUserEmail) {
		this.receiveRequestUserEmail = receiveRequestUserEmail;
	}

}
