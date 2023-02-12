package computech.user;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

	@Autowired
	UserAccountManagement userAccountManagement;

	User testUser;
	UserAccount testAccount;


	@BeforeAll
	void setUp() {
		testAccount = userAccountManagement.create("test", Password.UnencryptedPassword.of("1234"), "testEmail@test.com", Role.of("PrivateCustomer"));
		testUser = new User(testAccount, "testAddress","testAssociated","testCode");
	}

	@Test//238
	void setGetChangeAddress() {
		assertEquals("testAddress", testUser.getAddress(), "Wrong with getAddress()");
		testUser.setAddress("setAddress");
		assertEquals("setAddress", testUser.getAddress(), "Wrong with setAddress()");
		testUser.changeAddress("changedAddress");
		assertEquals("changedAddress", testUser.getAddress(), "Wrong with changeAddress()");
	}

	@Test//239
	void setAndGetBillingAddress() {
		assertNull(testUser.getBillingAddress(), "Wrong with getBillingAddress()");
		testUser.setBillingAddress("testBillingAddress");
		assertEquals("testBillingAddress", testUser.getBillingAddress(),"Wrong with getBillingAddress");

	}

	@Test//240
	void getUserAccount() {
		assertEquals(testAccount, testUser.getUserAccount(), "Wrong with getUserAccount");
	}

	@Test
	void getId() {

	}

	@Test//241
	void getAndChangeForename() {
		assertNull(testUser.getForename(),"Wrong with getForename");
		testUser.changeFirstname("testForeName");
		assertEquals("testForeName", testUser.getForename(), "Wrong with changeForename");
	}

	@Test//242
	void getNameAndChangeLastname() {
		assertNull(testUser.getName(),"Wrong with getName");
		testUser.changeLastname("testLastname");
		assertEquals("testLastname", testUser.getName(), "Wrong with changeLastname");
	}

	@Test//243
	void getAndChangeRole() {
		assertEquals("PrivateCustomer",testUser.getRole(), "Wrong with getRole()");
		testUser.changeRole("BusinessCustomer");
		assertEquals("BusinessCustomer",testUser.getRole(), "Wrong with changeRole()");
	}


	@Test//244
	void changeEmail() {
		assertEquals("testEmail@test.com",testAccount.getEmail());
		testUser.changeEmail("changedEmail@test.com");
		assertEquals("changedEmail@test.com",testAccount.getEmail(), "Wrong with changeEmail()");
	}

	@Test//245
	void getassosiated() {
		LinkedList<String> associated = new LinkedList<>();
		associated.add("testAssociated");
		assertEquals(associated, testUser.getassosiated(), "Wrong with getAssosiated()");
		testUser.changeAssosiated("changedAssociated");
		associated.clear();
		associated.add("changedAssociated");
		assertEquals(associated, testUser.getassosiated(), "Wrong with changeAssosiated()");
	}

	@Test//246
	void getCode() {
		assertEquals("testCode", testUser.getCode(),"Wrong with getCode()");
		assertNull(testUser.getCode(), "Wrong with getCode()");
	}
}