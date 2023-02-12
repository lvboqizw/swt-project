package computech.assignment;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.Order;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import computech.catalog.Hardware;
import computech.sales.Question;
import computech.sales.RepairItem;
import computech.user.User;
import computech.warehouse.WarehouseItem;
import computech.warehouse.WarehouseManagement;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
class AssignmentManagerTest {
	AssignmentManager assignmentManager;
	AssignmentRepository assignments;

	@BeforeEach
	void setUp() {
		assignments = mock(AssignmentRepository.class);
		OrderManagement<Order> orderManagement = mock(OrderManagement.class);
		WarehouseManagement warehouseManagement = mock(WarehouseManagement.class);
		assignmentManager = new AssignmentManager(assignments, orderManagement, warehouseManagement);
	}
	@Test //#110
	void addOrderAssignment() {
		
		Long id = 1L;
		Order order = mock(Order.class);
		User user = mock(User.class);
		assignmentManager.addOrderAssignment(id, order, user);
		assertThat(assignments.findAll()).isNotNull();

	}

	@Test
	void saveAssignment() {	
	}

	@Test //#111
	void addServiceAssignment() {
		Long id = 1L;
		Question question = mock(Question.class);
		assignmentManager.addServiceAssignment(id, question);
		assertThat(assignments.findAll()).isNotNull();
	}

	@Test //#112
	void addSellAssignment() {
		Order order = mock(Order.class);
		Hardware hardware = mock(Hardware.class);
		assignmentManager.addSellAssignment(order, hardware);
		assertThat(assignments.findAll()).isNotNull();
	}

	@Test //#113
	void addStockAssignment() {
		int amountToBeReodered = 1;
		WarehouseItem warehouseItem = mock(WarehouseItem.class);
		assignmentManager.addStockAssignment(amountToBeReodered, warehouseItem);
		assertThat(assignments.findAll()).isNotNull();
	}

	@Test //#114
	void addRepairAssignment() {
		Long id = 1L;
		RepairItem repairItem = mock(RepairItem.class);
		assignmentManager.addRepairAssignment(id, repairItem);
		assertThat(assignments.findAll()).isNotNull();
	}

	@Test //#115
	void confirmAssignment() {
		Long id = 1L;
		Order order = mock(Order.class);
		User user = mock(User.class);
		Assignment orderAssignment = new OrderAssignment(id, order, user);
		assignmentManager.confirmAssignment(orderAssignment);
		assertThat(orderAssignment.getState()).isEqualTo(AssignmentState.CONFIRMED);

		Question question = mock(Question.class);
		Assignment serviceAssignment = new ServiceAssignment(question, id);
		assignmentManager.confirmAssignment(serviceAssignment);
		assertThat(serviceAssignment.getState()).isEqualTo(AssignmentState.CONFIRMED);

		Hardware hardware = mock(Hardware.class);
		Assignment sellAssignment = new SellAssignment("name", order, hardware);
		assignmentManager.confirmAssignment(sellAssignment);
		assertThat(serviceAssignment.getState()).isEqualTo(AssignmentState.CONFIRMED);

		int amountToBeReodered = 1;
		WarehouseItem warehouseItem = mock(WarehouseItem.class);
		Assignment stockAssignment = new StockAssignment("name", amountToBeReodered, warehouseItem);
		assignmentManager.confirmAssignment(stockAssignment);
		assertThat(stockAssignment.getState()).isEqualTo(AssignmentState.CONFIRMED);

		RepairItem repairItem = mock(RepairItem.class);
		Assignment repairAssignment = new RepairAssignment(repairItem, id);
		assignmentManager.confirmAssignment(repairAssignment);
		assertThat(repairAssignment.getState()).isEqualTo(AssignmentState.CONFIRMED);

	}

	@Test 
	void deleteAssignment() {
	}

	@Test
	void getAssignmentById() {
	}

	@Test //#116
	void getAllAssignments() {
		Assignment assignment = mock(Assignment.class);
		assignments.save(assignment);
		assertThat(assignments.findAll()).isEqualTo(assignmentManager.getAllAssignments());
	}
}