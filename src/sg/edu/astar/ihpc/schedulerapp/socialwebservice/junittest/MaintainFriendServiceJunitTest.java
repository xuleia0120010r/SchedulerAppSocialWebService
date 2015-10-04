package sg.edu.astar.ihpc.schedulerapp.socialwebservice.junittest;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.FriendRequestBuffer;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.exception.StoreDataException;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.MaintainFriendService;
import sg.edu.astar.ihpc.schedulerapp.socialwebservice.service.ServiceConstant;

public class MaintainFriendServiceJunitTest {

	private MaintainFriendService maintainFriendService = null;
	
	// test based on the default database data as shown in the sqlscript
	@Before
	public void setUp() throws Exception {
		maintainFriendService = new MaintainFriendService();
	}

	@After
	public void tearDown() throws Exception {
		maintainFriendService = null;
	}
	
	@Test
	public void getUsersByEmailTest_legal1() {
		assertTrue(maintainFriendService.getUsersByEmail("a01").size()>0);
	}
	@Test
	public void getUsersByEmailTest_legal2() {
		assertTrue(maintainFriendService.getUsersByEmail("sg").size()>0);
	}
	@Test
	public void getUsersByEmailTest_legal3() {
		assertTrue(maintainFriendService.getUsersByEmail("abnamro").size()==0);
	}
	@Test
	public void getUsersByEmailTest_null() {
		assertTrue(maintainFriendService.getUsersByEmail(null).size()==0);
	}
	
	@Test
	public void getFriendsByEmailTest_legal1() {
		assertTrue(maintainFriendService.getFriendsByEmail("a0120010@nus.edu.sg").size()>0);
	}
	@Test
	public void getFriendsByEmailTest_legal2() {
		assertTrue(maintainFriendService.getFriendsByEmail("a0119949@nus.edu.sg").size()>0);
	}
	@Test
	public void getFriendsByEmailTest_null() {
		assertTrue(maintainFriendService.getFriendsByEmail(null).size()==0);
	}
	@Test
	public void getFriendsByEmailTest_illegal1() {
		assertTrue(maintainFriendService.getFriendsByEmail("xulei@163.com").size()==0);
	}
	@Test
	public void getFriendsByEmailTest_illegal2() {
		assertTrue(maintainFriendService.getFriendsByEmail("whatever").size()==0);
	}
	
	@Test
	public void getReceivedRequestsTest_legal1() {
		assertTrue(maintainFriendService.getReceivedRequests("a0120528@nus.edu.sg").size()>0);
	}
	@Test
	public void getReceivedRequestsTest_legal2() {
		assertTrue(maintainFriendService.getReceivedRequests("a0120010@nus.edu.sg").size()==0);
	}
	@Test
	public void getReceivedRequestsTest_null() {
		assertTrue(maintainFriendService.getReceivedRequests(null).size()==0);
	}
	@Test
	public void getReceivedRequestsTest_illegal() {
		assertTrue(maintainFriendService.getReceivedRequests("whatever").size()==0);
	}
	
	@Test
	public void getSentRequestsTest_legal1() {
		assertTrue(maintainFriendService.getSentRequests("a0120010@nus.edu.sg").size()>0);
	}
	@Test
	public void getSentRequestsTest_legal2() {
		assertTrue(maintainFriendService.getSentRequests("a0120528@nus.edu.sg").size()==0);
	}
	@Test
	public void getSentRequestsTest_null() {
		assertTrue(maintainFriendService.getSentRequests(null).size()==0);
	}
	@Test
	public void getSentRequestsTest_illegal() {
		assertTrue(maintainFriendService.getSentRequests("whatever").size()==0);
	}
	
	@Test
	public void storeFriendRequestDataTest_null() {
		try {
			maintainFriendService.storeFriendRequestData(null);
			fail("storeFriendRequestDataTest_null test fail");
		} catch (StoreDataException e) {
			fail("storeFriendRequestDataTest_null test fail");
		} catch (Exception e) {
			System.out.println("storeFriendRequestDataTest_null test success, info: " + e.getMessage());
		}
	}
	@Test
	public void storeFriendRequestDataTest_illegal1() {
		FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
		friendRequestBuffer.setSendRequestUserEmail("a0120010@nus.edu.sg");
		friendRequestBuffer.setReceiveRequestUserEmail("whatever");
		try {
			maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			fail("storeFriendRequestDataTest_illegal MSG_USER_NOT_EXIST_EXCEPTION test fail");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_USER_NOT_EXIST_EXCEPTION.equals(e.getMessage())){
				System.out.println("storeFriendRequestDataTest_illegal MSG_USER_NOT_EXIST_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("storeFriendRequestDataTest_illega MSG_USER_NOT_EXIST_EXCEPTION test fail, info: " + e.getMessage());
				fail("storeFriendRequestDataTest_illegal MSG_USER_NOT_EXIST_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("storeFriendRequestDataTest_illegal MSG_USER_NOT_EXIST_EXCEPTION test fail");
		}
	}
	@Test
	public void storeFriendRequestDataTest_illegal2() {
		FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
		friendRequestBuffer.setSendRequestUserEmail("a0120010@nus.edu.sg");
		friendRequestBuffer.setReceiveRequestUserEmail("a0120528@nus.edu.sg");
		try {
			maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			fail("storeFriendRequestDataTest_illegal MSG_FRIENDS_ALREADY_EXCEPTION test fail");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_FRIENDS_ALREADY_EXCEPTION.equals(e.getMessage())){
				System.out.println("storeFriendRequestDataTest_illegal MSG_FRIENDS_ALREADY_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("storeFriendRequestDataTest_illegal MSG_FRIENDS_ALREADY_EXCEPTION test fail, info: " + e.getMessage());
				fail("storeFriendRequestDataTest_illegal MSG_FRIENDS_ALREADY_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("storeFriendRequestDataTest_illegal MSG_FRIENDS_ALREADY_EXCEPTION test fail");
		}
	}
	@Test
	public void storeFriendRequestDataTest_illegal3() {
		FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
		friendRequestBuffer.setSendRequestUserEmail("a0120010@nus.edu.sg");
		friendRequestBuffer.setReceiveRequestUserEmail("a0120619@nus.edu.sg");
		try {
			maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			fail("storeFriendRequestDataTest_illegal MSG_BUFFERED_ALREADY_EXCEPTION test fail");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_BUFFERED_ALREADY_EXCEPTION.equals(e.getMessage())){
				System.out.println("storeFriendRequestDataTest_illegal MSG_BUFFERED_ALREADY_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("storeFriendRequestDataTest_illegal MSG_BUFFERED_ALREADY_EXCEPTION test fail, info: " + e.getMessage());
				fail("storeFriendRequestDataTest_illegal MSG_BUFFERED_ALREADY_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("storeFriendRequestDataTest_illegal MSG_BUFFERED_ALREADY_EXCEPTION test fail");
		}
	}
	
	@Test
	public void acknowledgeTest_illegal1() {
		try {
			maintainFriendService.acknowledge("a0120010@nus.edu.sg", "a0120528@nus.edu.sg", "deny");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_NOT_BUFFERED_EXCEPTION.equals(e.getMessage())){
				System.out.println("acknowledgeTest_illegal MSG_NOT_BUFFERED_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("acknowledgeTest_illegal MSG_NOT_BUFFERED_EXCEPTION test fail, info: " + e.getMessage());
				fail("acknowledgeTest_illegal MSG_NOT_BUFFERED_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("acknowledgeTest_illegal MSG_NOT_BUFFERED_EXCEPTION test fail");
		}
	}
	@Test
	public void acknowledgeTest_illegal2() {
		try {
			maintainFriendService.acknowledge("a0120619@nus.edu.sg", "a0120010@nus.edu.sg", "whatever");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_ACKNOWLEDGE_EXCEPTION.equals(e.getMessage())){
				System.out.println("acknowledgeTest_illegal MSG_ACKNOWLEDGE_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("acknowledgeTest_illegal MSG_ACKNOWLEDGE_EXCEPTION test fail, info: " + e.getMessage());
				fail("acknowledgeTest_illegal MSG_ACKNOWLEDGE_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("acknowledgeTest_illegal MSG_ACKNOWLEDGE_EXCEPTION test fail");
		}
	}
	
	@Test
	public void delete_illegal() {
		try {
			maintainFriendService.delete("a0120010@nus.edu.sg", "a0120619@nus.edu.sg");
		} catch (StoreDataException e) {
			if(ServiceConstant.MSG_NOT_FRIENDS_EXCEPTION.equals(e.getMessage())){
				System.out.println("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test success, info: " + e.getMessage());
			} else {
				System.out.println("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test fail, info: " + e.getMessage());
				fail("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test fail");
			}
		} catch (Exception e) {
			fail("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test fail");
		}
	}
	
	@Test
	public void storeFriendRequestData_acknowledge_Test_legal() {
		String sendRequestEmail = "a0120528@nus.edu.sg";
		String receiveRequestEmail = "a0120049@nus.edu.sg";
		FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
		friendRequestBuffer.setSendRequestUserEmail(sendRequestEmail);
		friendRequestBuffer.setReceiveRequestUserEmail(receiveRequestEmail);
		// 1. store data
		try {
			maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			System.out.println("storeFriendRequestData success");
		} catch (StoreDataException e) {
			System.out.println("storeFriendRequestData_acknowledge_Test_legal deny test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_Test_legal deny test fail");
		} catch (Exception e) {
			System.out.println("storeFriendRequestData_acknowledge_Test_legal deny test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_Test_legal deny test fail");
		}
		assertTrue(maintainFriendService.getReceivedRequests(receiveRequestEmail).size()>0);
		// 2. acknowledge deny
		try {
			maintainFriendService.acknowledge(receiveRequestEmail, sendRequestEmail, "deny");
			System.out.println("acknowledge deny success");
		} catch (StoreDataException e) {
			System.out.println("storeFriendRequestData_acknowledge_Test_legal deny test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_Test_legal deny test fail");
		} catch (Exception e) {
			System.out.println("storeFriendRequestData_acknowledge_Test_legal deny test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_Test_legal deny test fail");
		}
		assertTrue(maintainFriendService.getReceivedRequests(receiveRequestEmail).size()==0);
	}
	
	@Test
	public void storeFriendRequestData_acknowledge_delete_Test_legal() {
		String sendRequestEmail = "a0120528@nus.edu.sg";
		String receiveRequestEmail = "a0120049@nus.edu.sg";
		FriendRequestBuffer friendRequestBuffer = new FriendRequestBuffer();
		friendRequestBuffer.setSendRequestUserEmail(sendRequestEmail);
		friendRequestBuffer.setReceiveRequestUserEmail(receiveRequestEmail);
		// 1. store data
		try {
			maintainFriendService.storeFriendRequestData(friendRequestBuffer);
			System.out.println("storeFriendRequestData success");
		} catch (StoreDataException e) {
			System.out.println("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail");
		} catch (Exception e) {
			System.out.println("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail");
		}
		assertTrue(maintainFriendService.getReceivedRequests(receiveRequestEmail).size()>0);
		int friendNoPre = maintainFriendService.getFriendsByEmail(receiveRequestEmail).size();
		System.out.println("friendNoPre: " + friendNoPre);
		// 2. acknowledge accept
		try {
			maintainFriendService.acknowledge(receiveRequestEmail, sendRequestEmail, "accept");
			System.out.println("acknowledge accept success");
		} catch (StoreDataException e) {
			System.out.println("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail");
		} catch (Exception e) {
			System.out.println("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail, info: " + e.getMessage());
			fail("storeFriendRequestData_acknowledge_delete_Test_legal accept test fail");
		}
		assertTrue(maintainFriendService.getReceivedRequests(receiveRequestEmail).size()==0);
		int friendNoPost = maintainFriendService.getFriendsByEmail(receiveRequestEmail).size();
		System.out.println("friendNoPost: " + friendNoPost);
		assertTrue(friendNoPost>friendNoPre);
		// 3. delete friend
		try {
			maintainFriendService.delete(receiveRequestEmail, sendRequestEmail);
		} catch (StoreDataException e) {
			fail("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test fail");
		} catch (Exception e) {
			fail("delete_illegal MSG_NOT_FRIENDS_EXCEPTION test fail");
		}
		assertTrue(maintainFriendService.getFriendsByEmail(receiveRequestEmail).size()==friendNoPre);
	}
	
}
