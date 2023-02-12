package computech.accountancy;

import computech.user.UserManagement;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.core.Currencies;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

@Controller
public class AccountancyController {

	private final AccountancyEntryManagement accountancyEntryManagement;
	private final UserManagement userManagement;

	public AccountancyController(AccountancyEntryManagement accountancyEntryManagement, UserManagement userManagement){
		this.accountancyEntryManagement = accountancyEntryManagement;
		this.userManagement = userManagement;
	}

	/**
	 * entry the page of accountancy
	 * @param model will never be {@literal null}.
	 * @param form must not be {@literal null}.
	 * @return the view of accountancy.html
	 */
	@GetMapping("/accountancy")
	@PreAuthorize("hasRole('Manager') or hasAnyRole('SalesManager')")
	String accountancy(@NotNull(message = "Model must not be null!")Model model,
					   @NotNull(message = "SearchForm must not be null!") SearchForm form) {
		model.addAttribute("form", form);
		return "accountancy";
	}


	/**
	 * according to the data provided by {@link SearchForm} search the ProductPaymentEntry.
	 * @param model will never be {@literal null}.
	 * @param form must not be {@literal null}.
	 * @return the view name.
	 */
	@GetMapping("/accountancy/search")
	@PreAuthorize("hasRole('Manager') or hasAnyRole('SalesManager')")
	String listAccountancy(Model model, @ModelAttribute("form") SearchForm form) {
		Streamable<ProductPaymentEntry> list;
		MonetaryAmount sum1 = Currencies.ZERO_EURO;

		if(userManagement.findByUsername(form.getUsername())==null) {
			model.addAttribute("NotExist",true);
		} else {
			model.addAttribute("NotExist", false);
		}

		if(form.getStart().isEmpty() && form.getUsername().isEmpty()) {
			list = accountancyEntryManagement.findAll();
			model.addAttribute("PPEntryList", list);
			model.addAttribute("NotExist", false);
		} else if (form.getStart().isEmpty() && !form.getUsername().isEmpty()){
			list = accountancyEntryManagement.findByUserAccount(form);
			model.addAttribute("PPEntryList", list);

		} else if(!form.getStart().isEmpty() && form.getUsername().isEmpty()) {
			list = accountancyEntryManagement.findDateBetween(form);
			model.addAttribute("NotExist", false);
			model.addAttribute("PPEntryList", accountancyEntryManagement.findDateBetween(form));
		} else {
			list = accountancyEntryManagement.findByDateAndAccount(form);
			model.addAttribute("PPEntryList", accountancyEntryManagement.findByDateAndAccount(form));
		}


		for(var t : list) {
			sum1 = sum1.add(t.getValue());
		}

		model.addAttribute("Sum", sum1);
		return "accountancy";
	}

}
