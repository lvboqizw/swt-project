package computech.user;

import com.google.common.net.HttpHeaders;
import computech.order.OrderController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class UserControllerWebIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserManagement userManagement;

	@Mock
	UserAccount userAccount;

	@Mock
	Model model;

	@Autowired
	UserController userController;


	@Test
	void registerNew() {
	}

	@Test
	void addUser() {
		
	}

	@Test @WithMockUser(roles = "Manager")//209
	void customers() {
		String returnedView = userController.customers(model);
		assertThat(returnedView.endsWith("customers"));
	}

	@Test @WithMockUser(roles = "Manager")//210
	void businessCustomer() {
		String returnedView = userController.businessCustomer(model);
		assertThat(returnedView.endsWith("businessCustomer"));
	}

	@Test @WithMockUser(roles = "Manager")//211
	void privateCustomer() {
		String returnedView = userController.privateCustomer(model);
		assertThat(returnedView.endsWith("privateCustomer"));
	}

	@Test @WithMockUser(roles = "Manager")//212
	void employee() {
		String returnedView = userController.employee(model);
		assertThat(returnedView.endsWith("employee"));
	}

	@Test @WithMockUser(roles = "Manager")//213
	void manager() {
		String returnedView = userController.manager(model);
		assertThat(returnedView.endsWith("manager"));
	}

	@Test @WithMockUser(roles = "Manager")//214
	void worker() {
		String returnedView = userController.worker(model);
		assertThat(returnedView.endsWith("worker"));
	}

	@Test @WithMockUser(roles = "Manager")//215
	void salesManager() throws Exception {
		String returnedView = userController.salesManager(model);
		assertThat(returnedView.endsWith("salesManager"));
	}

	@Test//216
	void account() throws Exception {
		long id = 1;
		User nuter = userManagement.findByID(id);
		String returnedView = userController.account(model,nuter.getUserAccount());
		assertThat(returnedView.contains("accountdetail"));
	}

	@Test//217
	void delete() throws Exception{
		mockMvc.perform(get("/delete"))//
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/logout"))); //
	}

	@Test
	void deletebyid() {
	}

	@Test//218
	void changepassword() throws Exception {
		String password = "Password";
		userManagement.addUser(new RegistrationForm("Test","Test","Test","Test","Test","Test","Test","PrivateCustomer"));
		User nutzer = userManagement.findByUsername("Test");
		String returnedView = userController.changepassword(password,password, nutzer.getUserAccount());
		assertThat(returnedView.contains("/accountdetail"));
		//userManagement.delete(nutzer);
	}

	@Test//219
	void changeLastname() {
		String lastname = "Lastname";
		UserAccount nutzer = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = userController.changeLastname(lastname, nutzer);
		assertThat(returnedView.contains("/accountdetail"));
		assertEquals(userManagement.findByUsername("PrivateCustomer").getName(),lastname);
	}

	@Test//220
	void changeFirstname() {
		String firstname = "Firstname";
		userManagement.addUser(new RegistrationForm("Test1","Test1","Test1","Test1","Test1","Test1","Test1","PrivateCustomer"));
		User nutzer = userManagement.findByUsername("Test1");
		String returnedView = userController.changeFirstname(firstname, nutzer.getUserAccount());
		assertThat(returnedView.contains("/accountdetail"));
		assertEquals(userManagement.findByUsername(nutzer.getUserAccount().getUsername()).getForename(),firstname);
	}

	@Test//221
	void changeAddress() {
		String address = "Address";
		UserAccount nutzer = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = userController.changeAddress(address, nutzer);
		assertThat(returnedView.contains("/accountdetail"));
		assertEquals(userManagement.findByUsername("PrivateCustomer").getAddress(),address);
	}

	@Test//222
	void changeCheckoutShippingAddress() {
		String checkoutShippingAddress = "CheckoutShippingAddress";
		userManagement.addUser(new RegistrationForm("Test2","Test2","Test2","Test2","Test2","Test2","Test2","PrivateCustomer"));
		User nutzer = userManagement.findByUsername("Test2");
		String returnedView = userController.changeCheckoutShippingAddress(model, checkoutShippingAddress, nutzer.getUserAccount());
		assertThat(returnedView.contains("/confirmCheckout"));
		//userManagement.delete(nutzer);
	}

	@Test//223
	void changeCheckoutBillingAddress() {
		String checkoutBillingAddress = "CheckoutBillingAddress";
		userManagement.addUser(new RegistrationForm("Test3","Test3","Test3","Test3","Test3","Test3","Test3","PrivateCustomer"));
		User nutzer = userManagement.findByUsername("Test3");
		String returnedView = userController.changeCheckoutBillingAddress(model,checkoutBillingAddress, nutzer.getUserAccount());
		assertThat(returnedView.contains("/confirmCheckout"));
		assertEquals(userManagement.findByUsername(nutzer.getUserAccount().getUsername()).getBillingAddress(),checkoutBillingAddress);
	}

	@Test//224
	void changeEmail() {
		String email = "Email";
		UserAccount nutzer = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = userController.changeEmail(email, nutzer);
		assertThat(returnedView.contains("/accountdetail"));
		assertEquals(userManagement.findByUsername("PrivateCustomer").getUserAccount().getEmail(),email);
	}

	@Test
	void changeAssosiated() {
	}

	@Test
	void activation() {
	}
}