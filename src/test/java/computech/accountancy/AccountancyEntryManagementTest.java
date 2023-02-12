package computech.accountancy;


import static org.assertj.core.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

import computech.catalog.Accessoire;
import computech.catalog.HardwareType;
import computech.user.RegistrationForm;
import computech.user.UserManagement;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.*;


import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountancyEntryManagementTest {
	@Autowired
	UserManagement userManagement;
	@Autowired
	OrderManagement<Order> orderManagement;
	@Autowired
	AccountancyEntryManagement accountancyManagement;


	@BeforeAll
	void setUp() {

	}

	@AfterEach
	void tearDown() {
	}

	/**
	 * test if findAll method can things return
	 */
	@Test //#405
	void findAll() {
		assertThat(accountancyManagement.findAll()).isNotNull();
	}

	/**
	 * test if only with date information record search
	 */
	@Test //#406
	void findDateBetween() {
		SearchForm searchForm = new SearchForm("all",LocalDate.now().toString(),LocalDate.now().toString(),"");
		assertThat(accountancyManagement.findDateBetween(searchForm)).isNotNull();
	}

	/**
	 * test if only with start date search
	 */
	@Test //#407
	void findDateBetweenWithoutEndDate() {
		SearchForm searchForm = new SearchForm("all",LocalDate.now().toString(),"","");
		assertThat(accountancyManagement.findDateBetween(searchForm)).isNotNull();
	}

	/**
	 * test search with different search rang and date
	 */
	@Test //#408
	void findDateBetweenWithSearchRange() {
		SearchForm searchForm = new SearchForm("sales",LocalDate.now().toString(),"","");
		assertThat(accountancyManagement.findDateBetween(searchForm)).isNotNull();
		SearchForm searchForm2 = new SearchForm("spend",LocalDate.now().toString(),"","");
		assertThat(accountancyManagement.findDateBetween(searchForm2)).isEmpty();
	}

	/**
	 * test search only with username
	 */
	@Test//#409
	void findByUserAccount() {
		SearchForm searchForm = new SearchForm("all","","","PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm)).isNotNull();
	}

	/**
	 * test search with username and different search range
	 */
	@Test //#410
	void findByUserAccountWithSearchRange() {
		SearchForm searchForm = new SearchForm("sales",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm)).isNotNull();
		SearchForm searchForm2 = new SearchForm("spend",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm2)).isEmpty();
	}

	/**
	 * test search with date and username
	 */
	@Test//#411
	void findByDateAndAccount() {
		SearchForm searchForm = new SearchForm("all",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm)).isNotNull();
		SearchForm searchForm2 = new SearchForm("all",LocalDate.now().toString(),"","PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm2)).isNotNull();
	}

	/**
	 * test search with date, username and search range
	 */
	@Test //#412
	void finByDateAndAccountWithSearchRange() {
		SearchForm searchForm = new SearchForm("sales",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm)).isNotNull();
		SearchForm searchForm2 = new SearchForm("spend",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		assertThat(accountancyManagement.findByUserAccount(searchForm2)).isEmpty();
	}

	/**
	 * test if delete can record empty
	 */
	@Test //#413
	void deleteAccountancy() {
		accountancyManagement.deleteAccountancy(userManagement.findByUsername("PrivateCustomer"));
		SearchForm searchForm = new SearchForm("all",LocalDate.now().toString(),LocalDate.now().toString(),"PrivateCustomer");
		Streamable<ProductPaymentEntry> list = accountancyManagement.findByUserAccount(searchForm);
		assertThat(accountancyManagement.findByUserAccount(searchForm).isEmpty()).isEqualTo(true);
	}
}