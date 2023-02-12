package computech.accountancy;


import computech.user.User;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
public class AccountancyEntryManagement {

	private final AccountancyRepository accountancyEntryRepositor;
	private final UserAccountManagement userManagement;


	public AccountancyEntryManagement(AccountancyRepository accountancyEntryRepositor,
									   UserAccountManagement userManagement) {
		this.accountancyEntryRepositor = accountancyEntryRepositor;
		this.userManagement = userManagement;
	}

	public Streamable<ProductPaymentEntry> findAll() {
		return accountancyEntryRepositor.findAll();
	}


	/**
	 * Use the data provided by {@link SearchForm} search the {@link ProductPaymentEntry} with the given time interval.
	 * @param form must not bew {@literal null}.
	 * @return the productPaymentEntry in the time interval.
	 */
	public Streamable<ProductPaymentEntry> findDateBetween(SearchForm form){

		Streamable<ProductPaymentEntry> list;

		if(form.getEnd().isEmpty()) {
			list = accountancyEntryRepositor.findByDateBetween(LocalDateTime.of(
					LocalDate.parse(form.getStart()), LocalTime.MIN),
					LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
		} else {
			list = accountancyEntryRepositor.findByDateBetween(LocalDateTime.of(
					LocalDate.parse(form.getStart()), LocalTime.MIN),
					LocalDateTime.of(LocalDate.parse(form.getEnd()), LocalTime.MAX));
		}

		Streamable<ProductPaymentEntry> back;
		switch (form.getSearchRange()){
			case "sales":
				back = list.filter(x -> x.getValue().isPositiveOrZero());
				return back;
			case "spend":
				back = list.filter(x -> x.getValue().isNegative());
				return back;
			default:
				break;
		}
		return list;
	}

	/**
	 * Use the data provided by {@link SearchForm} search the {@link ProductPaymentEntry} with the given Username.
	 * @param form must not bew {@literal null}.
	 * @return the productPaymentEntry with the same Username
	 */
	public Streamable<ProductPaymentEntry> findByUserAccount(SearchForm form) {

		UserAccount userAccount = userManagement.findByUsername(form.getUsername()).orElse(null);

		Streamable<ProductPaymentEntry> list = accountancyEntryRepositor.findByUserAccount(userAccount);

		Streamable<ProductPaymentEntry> back;
		switch (form.getSearchRange()){
			case "sales":
				back = list.filter(x -> x.getValue().isPositiveOrZero());
				return back;
			case "spend":
				back = list.filter(x -> x.getValue().isNegative());
				return back;
			default:
				break;
		}
		return list;
	}

	/**
	 * Search with date and the username.
	 * @param form must not bew {@literal null}.
	 * @return the productPaymentEntry, which has the same username and created in the given interval.
	 */
	public Streamable<ProductPaymentEntry> findByDateAndAccount(SearchForm form) {

		Streamable<ProductPaymentEntry> stream;

		if(form.getEnd().isEmpty()) {
			stream = accountancyEntryRepositor.findByDateBetween(
					LocalDateTime.of(LocalDate.parse(form.getStart()), LocalTime.MIN),
					LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
		} else {
			stream = accountancyEntryRepositor.findByDateBetween(
					LocalDateTime.of(LocalDate.parse(form.getStart()), LocalTime.MIN),
					LocalDateTime.of(LocalDate.parse(form.getEnd()), LocalTime.MAX));
		}

		Streamable<ProductPaymentEntry> list = stream.filter(
				x -> x.getUserAccount().equals(
						userManagement.findByUsername(form.getUsername()).orElse(null)));

		Streamable<ProductPaymentEntry> back;
		switch (form.getSearchRange()){
			case "sales":
				back = list.filter(x -> x.getValue().isPositiveOrZero());
				return back;
			case "spend":
				back = list.filter(x -> x.getValue().isNegative());
				return back;
			default:
				break;
		}

		return list;
	}


	/**
	 *  Delete the Accountancy Record of the given {@link User} user form the Repository
	 * @param user the owner of the deleted Accountancy record
	 */
	public void deleteAccountancy(User user){
		SearchForm form = new SearchForm(
				"all", null, null, user.getUserAccount().getUsername());
		Streamable<ProductPaymentEntry> list = findByUserAccount(form);
		list.forEach(accountancyEntryRepositor::delete);
	}

}
