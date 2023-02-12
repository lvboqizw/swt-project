package computech.accountancy;


import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import java.time.LocalDate;



@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountancyControllerTest {

	@Autowired
	AccountancyController controller;
	@Autowired
	MockMvc mvc;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	/**
	 * test if the public user accountancy page enter
	 */
	@Test //#401
	void preventsPublicAccessForVisitAccountancyView() throws Exception {

		mvc.perform(get("/accountancy")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	/**
	 * test if the user with manager role enter
	 */
	@Test //#402
	@WithMockUser(roles = "Manager")
	void allowUserWithROlesAccess() throws Exception {

		mvc.perform(get("/accountancy")) //
				.andExpect(status().isOk()) //
				.andExpect(view().name("accountancy"));
	}

	/**
	 * test if the page can good enter
	 */

	@Test //#403
	@WithMockUser(roles = "Manager")
	void accountancy() {
		ExtendedModelMap model = new ExtendedModelMap();

		controller.accountancy(model, new SearchForm("all", null, null, null));

		assertThat(model.get("form")).isNotNull();
	}

	/**
	 * test if listAccountancy can god things return
	 */
	@Test //#404
	@WithMockUser(roles = "Manager")
	void listAccountancy() {
		ExtendedModelMap model = new ExtendedModelMap();

		controller.listAccountancy(model, new SearchForm("all", LocalDate.now().toString(), LocalDate.now().toString(), ""));

		assertThat(model.get("PPEntryList")).isNotNull();
	}
}