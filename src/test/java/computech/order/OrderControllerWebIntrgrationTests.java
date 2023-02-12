package computech.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;


import com.google.common.net.HttpHeaders;
import com.sun.istack.NotNull;
import computech.catalog.Accessoire;
import computech.catalog.CatalogController;
import computech.catalog.Hardware;
import computech.catalog.ProductRepositoryHardware;
import computech.user.RegistrationForm;
import computech.user.User;
import computech.user.UserManagement;
import computech.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.xml.catalog.Catalog;
import java.util.Collection;

/**
 * @author Lukas
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class OrderControllerWebIntegrationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	OrderController orderController;

	@Autowired
	UserManagement userManagement;

	@Mock
	UserAccount userAccount;

	@Autowired
	ProductRepositoryHardware productRepositoryHardware;

	Cart cart;
	/**
	 * check returned view if customer wants to see his orders
	 * @throws Exception
	 */
	@Test//#311
	void integrationTest() throws Exception{

		userAccount.setEnabled(true);
		mockMvc.perform(get("/order")).//
				andExpect(status().isOk())//
				.andExpect(view().name("order")) //
				.andExpect(model().attribute("order", is(not(empty()))));
	}

	/**
	 * Assess OrderController
	 */
	@Test//#312
	void assrtNotNullController(){
		assertNotNull("The Controller is Null",orderController);
	}

	/**
	 * test if user can view cart
	 * @throws Exception
	 */
	@Test @WithMockUser(authorities = "PrivateCustomer")//#313
	void basket() throws Exception{
		//orderController.initializeCart();
		mockMvc.perform(get("/cart")).//
				andExpect(status().isOk())//
				.andExpect(view().name("cart"));
	}





	/**
	 * Test adding a produkt to the cart
	 * @throws Exception
	 */

	@Test//#314
	void addItem() throws Exception{
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();
		cart = new Cart();
		String returnedView = orderController.addItem(prod,1,cart);
		assertThat(returnedView.endsWith("catalog"));
	}




	/**
	 * Test redirect after checkout
	 * @throws Exception
	 */
	@Test//#315
	void buy() throws Exception{
		mockMvc.perform(post( "/checkout"))//
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/cart"))); // being redirected to cart
	}


	@Test//#316
	void checkoutConfirm() throws Exception {
		cart = new Cart();
		Model model = new ExtendedModelMap();
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();
		cart.addOrUpdateItem(prod, 1);
		userManagement.addUser(new RegistrationForm("Test0","Test0","Test0","Test0","Test0","Test0","Test0","PrivateCustomer"));
		User testUser = userManagement.findByUsername("Test0");
		String returnedView = orderController.checkoutDetail(cart,model,testUser.getUserAccount());
		assertThat(returnedView.endsWith("Checkout"));
		assertThat(testUser.getAddress().equals(model.getAttribute("confirmCheckout")));

		cart.clear();
		returnedView = orderController.checkoutDetail(cart,model,testUser.getUserAccount());
		assertThat(returnedView.endsWith("cart"));
		

	}

	@Test//#317
	void addQuantity() {
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();
		cart = new Cart();

		String returnedView = orderController.addQuantity(prod,1,cart);
		assertThat(returnedView.contains("/cart"));

	}

	@Test//#318
	void delete(){
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();
		cart = new Cart();

		String returnedView = orderController.delete(cart,prod.getName());
		assertThat(returnedView.contains("/cart"));
	}





}
