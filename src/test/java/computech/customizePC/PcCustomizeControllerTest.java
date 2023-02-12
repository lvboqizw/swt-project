package computech.customizePC;

import com.google.common.net.HttpHeaders;
import computech.catalog.Hardware;
import computech.catalog.HardwareType;
import computech.catalog.ProductRepositoryHardware;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.order.Cart;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Lukas
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "PrivateCustomer", roles = "PrivateCustomer")
class PcCustomizeControllerWebIntegrationTest {

	@Autowired
	PcCustomizeController pcCustomizeController;

	@Autowired
	ProductRepositoryHardware productRepositoryHardware;

	@Autowired
	MockMvc mockMvc;

	Model model;

    @BeforeEach
    void setUp() {
    	model = new ExtendedModelMap();
    }

    @Test//#305
    void initialize() {
    	String returnedView = pcCustomizeController.initialize(model);
    	assertEquals("PcCreate", returnedView);

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("parts");
    	assertThat(object).isNotEmpty();
    }

	/**
	 * Test that only the selected Type is displayed
	 */
	@Test//#306
    void choosePart() {
    	String returnedView = pcCustomizeController.choosePart(model, HardwareType.PROCESSOR);
    	assertEquals("PcCreate", returnedView);

		Iterable<Hardware> object = (Iterable<Hardware>) model.asMap().get("parts");
		assertThat(object).isNotEmpty();
		for(Hardware e : object){
			assertThat(e.getType().equals(HardwareType.PROCESSOR));
		}
	}

	/**
	 * Test if selected Part is added to partList
	 */
    @Test//#307
    void addItem() {
		int lengthBefore = pcCustomizeController.getListOfPartsLength();
		int amount = 1;
		Hardware prod = productRepositoryHardware.findByName("RGB RAM").iterator().next();

		String returnedView = pcCustomizeController.addItem(amount,prod);
		assertThat(returnedView.endsWith("partPicker"));

		assertEquals(amount+lengthBefore, pcCustomizeController.getListOfPartsLength());
    }

	/**
	 * Test view of Only Customizables
	 * @throws Exception
	 */
	@Test//#308
    void customizePc() throws Exception{
		String returnedView = pcCustomizeController.customizePc(model);
		assertEquals("customizePc", returnedView);

		Iterable<Hardware> object = (Iterable<Hardware>) model.asMap().get("parts");
		assertThat(object.iterator().hasNext());
    }

	/**
	 * Test the list being added to the Cart
	 * @throws Exception
	 */
    @Test//#309
    void customize() throws Exception{
		mockMvc.perform(post("/addListToCart"))//
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/cart")));//is being redirected


    }

	/**
	 * Test if all types and parts are added to the model
	 */
	@Test//#310
    void partPicker() {
    	String returnedView = pcCustomizeController.partPicker(model);
		int amount = model.asMap().size();
		assertThat(amount == 14);
		assertEquals("partPicker", returnedView);

    }
}