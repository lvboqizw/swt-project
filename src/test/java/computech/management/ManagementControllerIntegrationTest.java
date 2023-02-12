package computech.management;

import computech.user.RegistrationForm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import computech.assignment.Assignment;
import computech.assignment.AssignmentManager;
import computech.assignment.AssignmentRepository;
import computech.assignment.OrderAssignment;
import computech.user.User;
import computech.user.UserManagement;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

/**
 * @author Jannek Steinke
 */

 @SpringBootTest
 @AutoConfigureMockMvc
 @WithMockUser(username = "Manager", roles = "Manager")
 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class ManagementControllerIntegrationTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ManagementController managementController;

    @Mock
	  Errors errors;

	  @Mock
	  Model model;

    @Mock
    Assignment assignment;

    @Mock
    UserAccount userAccount;
    
    @Test //#120
    void integrationTest() throws Exception{

        userAccount.setEnabled(true);
        mockMvc.perform(get("/management")).//
                andExpect(status().isOk())//
                .andExpect(view().name("management"))//
                .andExpect(model().attribute("management", is(not(empty()))));
    }

    @Test //#121
    void assertNotNullController(){
        assertNotNull(managementController, "The Controller is Null");
    }

    @Test
    void confirmOrder() throws Exception{

    }

    @Test
    void editOrder(){
        
    }
    @Test
    void removeOrderLine(){

    }
    @Test
    void answerService(){

    }
    @Test
    void confirmRepair(){

    }
    @Test
    void confirmSell(){

    }
    @Test
    void confirmStock(){

    }
    @Test
    void deleteAssignment(){

    }
    @Test
    void deleteWorker(){

    }

    @Test
    void addEmployee() throws Exception {

    }

    @Test
    void addCustomer(){
    }
    @Test
    void changeRole(){
    }
    @Test
    void management(){

    }
    @Test
    void updateItem(){

    }
    @Test
	  void deleteItem() {

	}

    
 }