package computech.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lukas
 */
@SpringBootTest
class CatalogControllerTest {

	@Autowired
	CatalogController catalogController;



	Model model;

    @BeforeEach
    void setUp() {
    	model = new ExtendedModelMap();
    }

	/**
	 * Test if catalog is empty
	 */
	@Test//#301
    void completecatalog() {
    	String returnedView = catalogController.completecatalog(model);
    	assertEquals("catalog", returnedView);

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");
		assertThat(object).isNotEmpty();
    }

}