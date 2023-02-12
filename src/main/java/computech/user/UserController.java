package computech.user;


import computech.accountancy.AccountancyEntryManagement;
import computech.order.OrderController;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.Random;


@Controller
public class UserController {

	private final UserManagement userManagement;
	private final AccountancyEntryManagement accountancyEntryManagement;


	/**
	 * Create a {@link UserController}
	 *
	 * @param userManagement
	 * @param accountancyEntryManagement
	 */
	UserController(UserManagement userManagement, AccountancyEntryManagement accountancyEntryManagement) {
		this.userManagement = userManagement;
		this.accountancyEntryManagement = accountancyEntryManagement;
	}


	/**
	 * register a new User
	 *
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("/register")
	String registerNew(@ModelAttribute("form") RegistrationForm form, @NotNull Errors result, Model model) {
		if (result.hasErrors() || !form.getPassword().equals(form.getMatchingpassword())) {
			return "register";
		}

		model.addAttribute("form", form);

		userManagement.createUser(form);
		userManagement.deaktivateUser(form.getUserName());

		return "redirect:/login";
	}

	/**
	 * adds a new User
	 *
	 * @param form
	 * @param result
	 * @return
	 */
	@PostMapping("/addUser")
	String addUser(@ModelAttribute("form") RegistrationForm form, @NotNull Errors result){
		if (result.hasErrors()) {
			return "addUser";
		}
		userManagement.addUser(form);
		return "redirect:/management";
	}


	/**
	 * Returns all Users with the Role PrivateCustomer or BusinessCustomer
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/customers")
	@PreAuthorize("hasAnyRole('Manager', 'SalesManager', 'Worker')")
	String customers(Model model) {
		LinkedList ausgabe = userManagement.findByRole("PrivateCustomer");
		ausgabe.addAll(userManagement.findByRole("BusinessCustomer"));
		model.addAttribute("customerList", ausgabe);
		return "customers";
	}


	/**
	 * Returns all Users with the Role BusinessCustomer
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/businessCustomer")
	@PreAuthorize("hasRole('Manager')")
	String businessCustomer(Model model) {
		model.addAttribute("customerList", userManagement.findByRole("BusinessCustomer"));
		return "businessCustomer";
	}


	/**
	 * Returns all Users with the Role PrivateCustomer
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/privateCustomer")
	@PreAuthorize("hasRole('Manager')")
	String privateCustomer(Model model) {
		model.addAttribute("customerList", userManagement.findByRole("PrivateCustomer"));

		return "privateCustomer";
	}


	/**
	 * Returns all Users with the Role Worker, SalesManager or Manger
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/employee")
	@PreAuthorize("hasRole('Manager')")
	String employee(Model model) {

		LinkedList ausgabe = userManagement.findByRole("Worker");
		ausgabe.addAll(userManagement.findByRole("SalesManager"));
		ausgabe.addAll(userManagement.findByRole("Manager"));
		model.addAttribute("employeeList", ausgabe);

		return "employee";
	}


	/**
	 * Returns all Users with the Role Manager
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/manager")
	@PreAuthorize("hasRole('Manager')")
	String manager(Model model) {
		model.addAttribute("managerList", userManagement.findByRole("Manager"));
		return "manager";
	}


	/**
	 * Returns all Users with the Role Worker
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/worker")
	@PreAuthorize("hasRole('Manager')")
	String worker(Model model) {
		model.addAttribute("workerList", userManagement.findByRole("Worker"));
		return "worker";
	}


	/**
	 * Returns all Users with the Role SalesManager
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/salesManager")
	@PreAuthorize("hasRole('Manager')")
	String salesManager(Model model) {
		model.addAttribute("salesManagerList", userManagement.findByRole("SalesManager"));
		return "salesManager";
	}


	/**
	 * Returns all Details of the User which is logged in
	 *
	 * @param model
	 * @param userAccount
	 * @return
	 */
	@GetMapping("/accountdetail")
	String account( Model model, @LoggedIn UserAccount userAccount) {
		User user = userManagement.findByUsername(userAccount.getUsername());
			model.addAttribute("accountdetail", user);
			model.addAttribute("accountdetail.userAccount", user.getUserAccount());
			if (user.getRole() == "SalesManager" || user.getRole() == "BusinessCustomer"){
				model.addAttribute("assosiated", user.getassosiated());
			}
			return "accountdetail";
	}


	/**
	 * Deactivates the Account of the logged in User permanently
	 *
	 * @param userAccount
	 * @return
	 */
	@GetMapping("/delete")
	String delete (@LoggedIn UserAccount userAccount){
		//accountancyEntryManagement.deleteAccountancy(userManagement.findByUsername(userAccount.getUsername()));
		User nutzer= userManagement.findByUsername(userAccount.getUsername());
		userManagement.deleteUserById(nutzer.getId());
		return "redirect:/logout";
	}


	/**
	 * Deactivates the Account for a given id permanently
	 *
	 * @param id
	 */
	@GetMapping("/zulöschen")
	void deletebyid (@ModelAttribute("id") Long id){
		User nutzer= userManagement.findByID(id);
		userManagement.delete(nutzer);
	}


	/**
	 *Validates the the new password was entert correctly twice and then changes the password
	 * of the logged in User to the one he entert
	 *
	 * @param password
	 * @param matchingpassword
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changepassword")
	String changepassword ( @RequestParam ("changepassword") String password,
							@RequestParam("matchingpassword") String matchingpassword, @LoggedIn UserAccount userAccount){
		if (!password.equals(matchingpassword)){
			return "accountdetail";
		}
		userManagement.changepassword(userAccount.getUsername() , password);
		return "redirect:/accountdetail";
	}


	/**
	 * Changes the lastname of the logged in User
	 *
	 * @param lastname
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeLastname")
	String changeLastname ( @RequestParam ("changeLastname") String lastname, @LoggedIn UserAccount userAccount){
		userManagement.changeLastname(userAccount.getUsername() , lastname);
		return "redirect:/accountdetail";
	}


	/**
	 * Changes the firstname of the logged in User
	 *
	 * @param firstname
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeFirstname")
	String changeFirstname ( @RequestParam ("changeFirstname") String firstname, @LoggedIn UserAccount userAccount){
		userManagement.changeFirstname(userAccount.getUsername() , firstname);
		return "redirect:/accountdetail";
	}


	/**
	 * Changes the address of the logged in User
	 *
	 * @param address
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeAddress")
	String changeAddress ( @RequestParam ("changeAddress") String address, @LoggedIn UserAccount userAccount){
		userManagement.changeAddress(userAccount.getUsername() , address);
		return "redirect:/accountdetail";
	}


	/**
	 * Changes the Check out Shipping address of the logged in User
	 *
	 * @param model
	 * @param address
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeCheckoutAddress")
	String changeCheckoutShippingAddress (Model model, @RequestParam ("changeCheckoutAddress") String address,
										  @LoggedIn UserAccount userAccount){
		userManagement.changeAddress(userAccount.getUsername() , address);
		return "redirect:/confirmCheckout";
	}


	/**
	 * Changes the Check out Billing address of the logged in User
	 *
	 * @param model
	 * @param address
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeCheckoutBillingAddress")
	String changeCheckoutBillingAddress(Model model, @RequestParam ("changeCheckoutBillingAddress") String address,
										@LoggedIn UserAccount userAccount){
		String ausgabe = userAccount.getUsername();
		User user = userManagement.findByUsername(ausgabe);
		userManagement.changeBillingAddress(ausgabe, address);
		model.addAttribute("confirmCheckoutb", user.getBillingAddress());
		return "redirect:/confirmCheckout";
	}


	/**
	 * Changes the EMail-Address of the Logged in User
	 *
	 * @param email
	 * @param userAccount
	 * @return
	 */
	@PostMapping("/changeEmail")
	String changeEmail ( @RequestParam ("changeEmail") String email, @LoggedIn UserAccount userAccount){
		userManagement.changeEmail(userAccount.getUsername() , email);
		return "redirect:/accountdetail";
	}


	/**
	 * Changes the Assosiated Sales manager of a BusinessCustomer
	 *
	 * @param userAccount
	 * @return
	 */
	@GetMapping("/changeAssosiated")
	@PreAuthorize("hasAnyRole('BusinessCustomer')")
	String changeAssosiated(@LoggedIn UserAccount userAccount){
		String username;
		User nutzer = userManagement.findByUsername(userAccount.getUsername());
		LinkedList<User> SalesManager = userManagement.findByRole("SalesManager");
		if(SalesManager.size()>1) {
			do {
				Random zahl = new Random();

				username = SalesManager.get(zahl.nextInt(SalesManager.size())).getUserAccount().getUsername();
			} while (username.equals(nutzer.getassosiated().getFirst()));
			userManagement.changeAssosiated(userAccount.getUsername(), username);
			return "redirect:/accountdetail";
		} else {
			return "error";
		}
	}


	/**
	 * Activates the UserAccount for the given Username if the code is correct
	 *
	 * @param username
	 * @param code
	 * @return
	 */
	@GetMapping("/activation/{username}/{code}")
	String activation(@PathVariable("username") String username, @PathVariable("code") String code){
		userManagement.aktivateUser(username, code);
		return "redirect:/login";
	}


}

