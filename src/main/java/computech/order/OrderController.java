package computech.order;


import com.mysema.commons.lang.Assert;
import computech.catalog.Hardware;
import computech.user.User;
import computech.user.UserManagement;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import computech.assignment.*;

import java.util.Optional;
import computech.assignment.AssignmentManager;

/**
 * This is a Spring MVC conmtroller to manage{@link Cart}. {@link Cart} only exists once per authorized Customer
 *
 * @author Lukas
 */


@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {

	private final OrderManagement<Order> orderManagement;
	private final AssignmentManager assignmentManager;
	private final UserManagement userManagement;



	/**
	 * Creates a {@link OrderController} with a given {@link OrderManagement}
	 *
	 * @param orderManagement must not be {@literal null}
	 */
	OrderController(OrderManagement<Order> orderManagement, AssignmentManager assignmentManager, UserManagement userManagement){

		Assert.notNull(orderManagement, "OrderManagement can not be null");
		this.orderManagement = orderManagement;
		this.assignmentManager = assignmentManager;
		this.userManagement = userManagement;

	}

	/**
	 * Creates a new {@link Cart} instance to be stored in the session (see the class-level {@link SessionAttributes}
	 * annotation).
	 *
	 * @return a new {@link Cart} instance.
	 */
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	/**
	 *
	 * @return current cart
	 */
	@GetMapping("/cart")
	String basket() {
		return "cart";
	}


	/**
	 * Adds a {@link Hardware} to the {@link Cart}
	 * The given Product can not be null and supports single Parts as well as complete Systems.
	 * The Item is represented by pid
	 *
	 * @param item the product that is suppose to be added bto the cart
	 * @param number number of items suppose to be added to cart
	 * @param cart must not be {@literal null}
	 * @return the view name
	 */

	@PostMapping("/cart")
	String addItem(@RequestParam("productId") Hardware item, @RequestParam("number") int number, @ModelAttribute Cart cart){

		//Input control
		int amount = number <=0 ? 1 : number;

		//Add Item to Cart
		cart.addOrUpdateItem(item, Quantity.of(amount));

		return "redirect:catalog";

	}



	/**
	 * Checks out the current state of the {@link Cart}. Using a method parameter of type {@code Optional<UserAccount>}
	 * annotated with {@link LoggedIn} you can access the {@link UserAccount} of the currently logged in user.
	 *
	 * @param cart can not be {@literal null}.
	 * @param userAccount can not be {@literal null}.
	 * @return the view name.
	 */
	@PostMapping("/checkout")
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, @RequestParam("businesscustomer") Optional<String> username) {

	return userAccount.map(account -> {
		if(!cart.isEmpty()) {
			Order order = new Order (account, Cash.CASH);
			cart.addItemsTo(order);
			User user = userManagement.findByUsername(account.getUsername());
			orderManagement.payOrder(order);
			if(account.hasRole(Role.of("PrivateCustomer"))){
				assignmentManager.addOrderAssignment((long) -1, order, user);
			}
			if(account.hasRole(Role.of("BusinessCustomer"))){
				assignmentManager.addOrderAssignment((long) 1, order, user);
			}
			if(account.hasRole(Role.of("SalesManager")) || account.hasRole(Role.of("Manager"))){
				User user2 = userManagement.findByUsername(username.get());
				if(user2 != null){
					assignmentManager.addOrderAssignment(user.getId(), order, user2);
				}
				
			}
			
				
			

			}
			cart.clear();


			return "redirect:/cart";

	}).orElse("redirect:/cart");
	}


	/**
	 *
	 * @param cart
	 * @param model
	 * @param userAccount
	 * @return
	 */
	@GetMapping("/confirmCheckout")
	String checkoutDetail(@ModelAttribute Cart cart, Model model, @LoggedIn UserAccount userAccount){
		if(!cart.isEmpty()){
			User user = userManagement.findByUsername(userAccount.getUsername());
			model.addAttribute("confirmCheckout", user.getAddress());
			model.addAttribute("confirmCheckoutb", user.getBillingAddress());
			return "confirmCheckout";
		}
		return "redirect:/cart";
	}

	/**
	 *
	 * @param item
	 * @param number
	 * @param cart
	 * @return
	 */
	@PostMapping("/addQuantity")
	String addQuantity(@RequestParam("productId") Hardware item, @RequestParam("number") int number, @ModelAttribute Cart cart){

		int amount = number <=0 ? 1 : number;
		cart.addOrUpdateItem(item, Quantity.of(amount));

		return "redirect:/cart";
	}

	/**
	 *
	 * @param cart
	 * @param item
	 * @return
	 */
	@PostMapping(path= "/delete")
	String delete(@ModelAttribute Cart cart, @RequestParam(name = "item") String item){

		cart.removeItem(item);

		return "redirect:/cart";
	}

	/**
	 *
	 * @param model
	 * @param userAccount
	 * @return
	 */
	@GetMapping(path= "/order")
	String showOrder(Model model , @LoggedIn UserAccount userAccount){

		model.addAttribute("order", orderManagement.findBy(userAccount));

		return "order";
	}









}
