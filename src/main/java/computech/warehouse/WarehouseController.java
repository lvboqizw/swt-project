package computech.warehouse;


import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The Class WarehouseController.Mainly used for checking warehouse items.
 *
 */
@Controller
@PreAuthorize("isAuthenticated()")
public class WarehouseController {

	public WarehouseRepository warehouse;
	public WarehouseItem warehouseItem;
	private static Quantity NONE=Quantity.of(5);
	public Quantity quantity;

	public WarehouseController(WarehouseRepository warehouse) {
		this.warehouse = warehouse;

	}
	/**
	 * Warehouse.
	 *
	 * @param model will never be {@literal null}.
	 * @return the view name.
	 */
	@GetMapping("/warehouse")
	@PreAuthorize("hasRole('Manager')")
	String warehouseAll(Model model) {
		model.addAttribute("warehouse", warehouse.findAll());
		return "warehouse";
	}


}
