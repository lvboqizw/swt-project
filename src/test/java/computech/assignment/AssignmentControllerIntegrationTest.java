package computech.assignment;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@SpringBootTest
class AssignmentControllerIntegrationTest {

	@Autowired 
	AssignmentController assignmentController;

	Model model;

	@BeforeEach
	void setUp(){
		model = new ExtendedModelMap();
	}
	@Test
	void confirmAssignment() {
	}
	
	@Test //#101
	void assignments() {
		String returnedView = assignmentController.assignments(model);
    	assertEquals("assignments", returnedView);

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("assignments");
		assertThat(object).isEmpty();
	}
}