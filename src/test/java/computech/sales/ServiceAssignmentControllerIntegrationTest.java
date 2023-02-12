package computech.sales;

import computech.assignment.AssignmentRepository;
import computech.assignment.ServiceAssignment;
import computech.user.UserManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;



@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class ServiceAssignmentControllerIntegrationTest {


	@Autowired
	ServiceController serviceController;

	@Autowired
	ServiceRepository serviceRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Autowired
	UserManagement userManagement;

	Model model;

    @BeforeEach
    void setUp() {
    	model = new ExtendedModelMap();
    }


    @Test//#501
    void makeServiceRequest() {
    	String returnedView = serviceController.makeServiceRequest();
    	assertThat(returnedView.endsWith("service"));
    }


	@Test//#502
	void openForum(){
		UserAccount testUserAccount = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = serviceController.openForum(model, testUserAccount);

		assertEquals("forum", returnedView);
	}


    @Test//#503
    void sendQuestion(){
		UserAccount testUserAccount = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = serviceController.sendQuestion(testUserAccount, "testC", "testS");

		assertThat(returnedView.endsWith("service"));
		assertThat(assignmentRepository.findAll()).isNotNull();
	}


    @Test  @WithMockUser(authorities = "PrivateCustomer") //#504
    void confirmAnswer() {
		Long id = userManagement.findByUsername("PrivateCustomer").getId();
		Question q = mock(Question.class);
		ServiceAssignment sa = new ServiceAssignment(q,id);
		assignmentRepository.save(sa);
		String returnedView = serviceController.confirmAnswer(sa.getId(), "test");

		assertThat(returnedView.endsWith("management"));
		assertThat(serviceRepository.findAll()).isNotNull();

	}
}