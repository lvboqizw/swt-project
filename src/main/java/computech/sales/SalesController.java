package computech.sales;


import computech.assignment.AssignmentManager;
import computech.assignment.SellAssignment;
import computech.catalog.Hardware;
import computech.catalog.ProductRepositoryHardware;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.money.MonetaryAmount;
import java.util.LinkedList;

/**
 * Spring Controller for processing the cycle of a Sell by the Customer
 * @author Lukas
 */
@Controller
@PreAuthorize("isAuthenticated()")
public class SalesController {

	private final OrderManagement<Order> orderManagement;
	private final ProductRepositoryHardware productRepositoryHardware;
	private final AssignmentManager assignmentManager;

	public SalesController(OrderManagement<Order> orderManagement, ProductRepositoryHardware productRepositoryHardware, AssignmentManager assignmentManager) {
		this.orderManagement = orderManagement;
		this.productRepositoryHardware = productRepositoryHardware;
		this.assignmentManager = assignmentManager;
	}

	/**
	 * add all bought items to the model
	 * @param model Model view
	 * @param userAccount Session Customer
	 * @return sales
	 */
	@GetMapping("/sales")
	String initialize(Model model, @LoggedIn UserAccount userAccount){
		LinkedList<Order> orderList = new LinkedList<>();
		orderList.addAll(orderManagement.findBy(userAccount).toList());
		model.addAttribute("orderList", orderList);
		return "sales";
	}

	/**
	 * Handling of Parameters after Customer sells an item
	 * @param number amount of parts
	 * @param userAccount Session customer
	 * @param order Order that is being handled
	 * @param orderLine OrderLine for accessing Product
	 * @return "redirect:/sales"
	 */
	@PostMapping("/submitSale")
	String sale(@RequestParam("number")int number, @LoggedIn UserAccount userAccount, @RequestParam("order") Order order, @RequestParam("orderLine") OrderLine orderLine){
		Hardware item = null;
		for(Hardware h : productRepositoryHardware.findAll()){
			if(orderLine.refersTo(h)){
				item = h;
			}
		}
		var saleOrder = new Order(userAccount, Cash.CASH);
		MonetaryAmount sellPrice = Currencies.ZERO_EURO;
		sellPrice = (sellPrice.subtract(item.getPrice())).multiply(assignmentManager.getDiscount());
		SaleItem saleItem = new SaleItem(item.getName(), sellPrice);
		saleOrder.addOrderLine(saleItem, Quantity.of(number));
		orderManagement.payOrder(saleOrder);
		assignmentManager.addSellAssignment(saleOrder,item);

		return "redirect:/sales";
	}


	/**
	 * View of submitted Assignments and changing selling price
	 * @param actualPrice new set Price
	 * @param order processed Order
	 * @param assignmentId Responsible Assignment
	 * @return "redirect:/management"
	 */
	/*
	@PostMapping("/confirmSell")
	@PreAuthorize("hasAnyRole('SalesManager')")
	String confirmSell(@RequestParam("text") String actualPrice, @RequestParam(name="order") Order order, @RequestParam("assignment") Long assignmentId){
		SellAssignment assignment = (SellAssignment) assignmentManager.getAssignmentById(assignmentId);
		float price = Integer.valueOf(actualPrice);
		String prodName=null;
		Quantity amount=null;
		if(order.getOrderLines().toList().size() == 1){
			for(OrderLine e : order.getOrderLines().toList()){
				prodName = e.getProductName();
				amount = e.getQuantity();
			}
		}
		assignmentManager.confirmAssignment(assignment);
		return "redirect:/management";
	}

	 */


}
