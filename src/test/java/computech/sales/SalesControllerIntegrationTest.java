package computech.sales;

import com.google.common.net.HttpHeaders;
import computech.assignment.Assignment;
import computech.assignment.AssignmentManager;
import computech.assignment.SellAssignment;
import computech.catalog.Hardware;
import computech.catalog.ProductRepositoryHardware;
import computech.user.User;
import computech.user.UserManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Lukas
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class SalesControllerIntegrationTest {

	Model model;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	SalesController salesController;

	@Autowired
	UserManagement userManagement;

	@Autowired
	ProductRepositoryHardware productRepositoryHardware;



    @BeforeEach
    void setUp(){
		model = new ExtendedModelMap();
    }
/*
    @Test
    void initialize() throws Exception{
		mockMvc.perform(get("/sales"))//
				.andExpect(status().isOk());//
    }
    
 */

    @Test
    void sale() {
    	UserAccount testUserAccount = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		var order = new Order (testUserAccount, Cash.CASH);
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();
		order.addOrderLine(prod, Quantity.of(2));
		String returnedView = salesController.sale(1,testUserAccount,order,order.getOrderLines().iterator().next());
		assertThat(returnedView.endsWith("sales"));
    }

}