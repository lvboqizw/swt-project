package computech.accountancy;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.Order;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

@Component
public class AccountancyInitializer implements DataInitializer {

	private final UserAccountManagement accountManagement;
	private final AccountancyRepository accountancyRepository;
	private final Accountancy accountancy;
	private final AccountancyEntryManagement accountancyEntryManagement;

	public AccountancyInitializer(UserAccountManagement accountManagement, AccountancyRepository accountancyRepository,
								  Accountancy accountancy, AccountancyEntryManagement accountancyEntryManagement) {
		this.accountManagement = accountManagement;
		this.accountancyRepository = accountancyRepository;
		this.accountancy = accountancy;
		this.accountancyEntryManagement = accountancyEntryManagement;
	}

	/**
	 * initialize a record for privateCustomer and two deleted record for businessCustomer
	 */

	@Override
	public void initialize() {


		if(accountManagement.findByUsername("PrivateCustomer").isPresent()) {
			UserAccount account = accountManagement.findByUsername("PrivateCustomer").get();

			PaymentMethod paymentMethod = Cash.CASH;

			Order order = new Order(account, paymentMethod);


			accountancy.add(ProductPaymentEntry.of(order, String.format("Rechnung Nr. %s", order.getId())));

			UserAccount bussAccount = accountManagement.findByUsername("BusinessCustomer").get();

			Order order2 = new Order(bussAccount, paymentMethod);

			accountancy.add(ProductPaymentEntry.of(order2, String.format("Rechnung Nr. %s", order2.getId())));

			Order order3 = new Order(bussAccount, paymentMethod);

			accountancy.add(ProductPaymentEntry.of(order2, String.format("Rechnung Nr. %s", order3.getId())));
		}

		SearchForm form = new SearchForm("all", null, null, "BusinessCustomer");

		Streamable<ProductPaymentEntry> list = accountancyEntryManagement.findByUserAccount(form);
		list.forEach(accountancyRepository::delete);
	}



}
