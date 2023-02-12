package computech.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.LinkedList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class UserManagementTest {


	@Autowired
	UserManagement userManagement;

	@Autowired
	UserAccountManagement userAccountManagement;

	@Autowired
	UserRepository userRepository;

	Model model;

	@BeforeEach
	void setUp() {
		model = new ExtendedModelMap();
	}

	@Test
	void createUser() {
	}

	@Test//225
	void addUser() {

		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(userAccount);

		UserManagement userManagement = new UserManagement(userRepository, userAccountManagement);

		RegistrationForm form = new RegistrationForm("Nutzername", "name","forename", "1234", "1234", "email@email.de", "address", "PrivateCustomer");
		User user = userManagement.addUser(form);

		assertEquals(user.getUserAccount().getUsername(), form.getUserName());
		assertThat(user.getUserAccount()).isNotNull();
	}

	@Test//226
	void findAll() {
		Streamable<User> returnedView = userManagement.findAll();
		assertThat(returnedView).isNotNull();
	}

	@Test//227
	void findByUsername() {
		String returnedView = userManagement.findByUsername("PrivateCustomer").getUserAccount().getUsername();
		assertEquals(returnedView,"PrivateCustomer");
	}

	@Test//228
	void findByRole() throws Exception{
		LinkedList<User> returnedView = userManagement.findByRole("Manager");
		for(User user : returnedView){
			assertEquals(user.getRole(), "Manager");
		}

	}

	@Test
	void delete() {
	}

	@Test//229
	void findByID() {
		long id = Long.parseLong("1");
		long returnedView = userManagement.findByID(id).getId();
		assertEquals(id , returnedView);
	}

	@Test
	void deleteUserById() {
	}

	@Test
	void changepassword() {
	}

	@Test//230
	void changeLastname() {
		userManagement.changeLastname("PrivateCustomer", "newLastname");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getName();
		assertEquals(returnedView,"newLastname");
	}

	@Test//231
	void changeFirstname() {
		userManagement.changeFirstname("PrivateCustomer", "newFirstname");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getForename();
		assertEquals(returnedView,"newFirstname");
	}

	@Test//232
	void changeAddress() {
		userManagement.changeAddress("PrivateCustomer", "newAddress");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getAddress();
		assertEquals(returnedView,"newAddress");
	}

	@Test//233
	void changeBillingAddress() {
		userManagement.changeBillingAddress("PrivateCustomer", "newBillingAddress");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getBillingAddress();
		assertEquals(returnedView,"newBillingAddress");
	}

	@Test//234
	void changeEmail() {
		userManagement.changeEmail("PrivateCustomer", "newEmail");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getUserAccount().getEmail();
		assertEquals(returnedView,"newEmail");
	}

	@Test//235
	void changeRole() {
		Long id =userManagement.findByUsername("PrivateCustomer").getId();
		userManagement.changeRole(id, "Manager");
		String returnedView = userManagement.findByUsername("PrivateCustomer").getRole();
		assertEquals(returnedView,"Manager");
	}

	@Test
	void deaktivateUser() {
	}

	@Test
	void aktivateUser() {
	}

	@Test//236
	void saveUser() {
		User nutzer = userManagement.findByUsername("PrivateCustomer");
		nutzer.changeAddress("address");
		userManagement.saveUser(nutzer);
		User user = userManagement.findByUsername("PrivateCustomer");
		assertEquals(user.getAddress(),"address");
	}

	@Test//237
	void getAssosiatedID() {
		User nutzer = userManagement.findByUsername("BusinessCustomer");
		userManagement.getAssosiatedID(nutzer.getUserAccount());
		User user = userManagement.findByUsername("BusinessCustomer");
		assertThat(user.getassosiated()).isNotNull();
	}
}