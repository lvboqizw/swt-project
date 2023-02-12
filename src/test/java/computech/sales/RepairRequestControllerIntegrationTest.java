package computech.sales;

import computech.assignment.RepairAssignment;
import computech.catalog.HardwareType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import computech.assignment.AssignmentRepository;
import computech.user.UserManagement;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class RepairRequestControllerIntegrationTest {

	@Autowired
	RepairController repairController;

	@Autowired
	RepairRepository repairRepository;

	@Autowired
	UserManagement userManagement;

	@Autowired
	AssignmentRepository assignmentRepository;

	Model model;

	@BeforeEach
	void setUp() {
		model = new ExtendedModelMap();
	}


	@AfterEach
	void tearDown() {
	}


	@Test//#505
	void makeRepairRequest(){
		UserAccount testUserAccount = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = repairController.makeRepairRequest(testUserAccount, model);

		assertEquals("repairRequest", returnedView);
	}


	@Test//#506
	void sendRepairRequest() {
		UserAccount testUserAccount = userManagement.findByUsername("PrivateCustomer").getUserAccount();
		String returnedView = repairController.sendRepairRequest(testUserAccount, "true", 1, 1, "true", HardwareType.PC, "test", "flaw");

		assertThat(returnedView.endsWith("confirmedRepairPage"));
		assertThat(repairRepository.findAll()).isNotNull();

	}


	@Test//#507
	void confirmedRepairPage() {
		String returnedView = repairController.confirmedRepairPage();
		assertThat(returnedView.endsWith("service"));
	}


	@Test//#508
	void acceptRepairRequest() {
		Long id = userManagement.findByUsername("PrivateCustomer").getId();
		RepairItem r = mock(RepairItem.class);
		RepairAssignment ra = new RepairAssignment(r, id);
		assignmentRepository.save(ra);
		String returnedView = repairController.acceptRepairRequest(ra.getId());

		assertThat(returnedView.endsWith("management"));
	}
}