package computech.warehouse;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import computech.assignment.AssignmentManager;
import computech.assignment.AssignmentRepository;
import computech.catalog.Accessoire;
import computech.catalog.CompuTechCatalog;
import computech.catalog.HardwareType;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration test for the {@link WarehouseController} on the web layer, i.e. simulating HTTP requests.
 *
 * @author Fedor Leonov
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WarehouseWebIntegrationTest {



	@Autowired MockMvc mvc;

	@Mock
	WarehouseItem warehouseItem;

	@Autowired
	WarehouseRepository warehouseRepository;

	@InjectMocks
	WarehouseManagement warehouseManagement;

	@Mock
	CompuTechCatalog compuTechCatalog;

	@BeforeEach
	void setUp() {
		OrderManagement<Order> orderManagement = mock(OrderManagement.class);
		warehouseRepository = mock(WarehouseRepository.class);
		compuTechCatalog=mock(CompuTechCatalog.class);
		warehouseItem=mock(WarehouseItem.class);
		warehouseRepository.save(warehouseItem);
		warehouseManagement=new WarehouseManagement(orderManagement,warehouseRepository,compuTechCatalog);
		MockitoAnnotations.initMocks(this);

	}

	/**
	 * Sample integration test using fake HTTP requests to the system and using the expectations API to define
	 * constraints.
	 */
	@Test
	@WithMockUser(username = "SalesManager", password = "1234", roles = "SalesManager")
	void sampleMvcIntegrationTest() throws Exception {

		mvc.perform(get("/management")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("products", is(not(emptyIterable()))));
	}
	@Test
	@WithMockUser(username = "SalesManager", password = "1234", roles = "SalesManager")
	void deleteItem() throws Exception {
//		mvc.perform(post("/deleteWarehouseItem/{id_del}",warehouseItem.getId().getIdentifier())).andExpect(status().isOk());
		System.out.println("delete item test");
	}

}