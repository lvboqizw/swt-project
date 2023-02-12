package computech.warehouse;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import javax.persistence.Entity;


/**
 * The Class WarehouseItem.
 */
@Entity
public class WarehouseItem extends UniqueInventoryItem {

	private final static Quantity NONE = Quantity.of(0);
	/** The minimal quantity. */
	public int minQuantity;
	/** Checks if WarehouseItem is a PC */
	private boolean isPC;

	/**
	 * Instantiates a new WarehouseItem.
	 *
	 * @param product the product
	 * @param quantity the quantity
	 * @param minQuantity the minimal quantity
	 * @param isPC if Item is a PC
	 */
	public WarehouseItem(Product product, Quantity quantity, int minQuantity, boolean isPC) {
		super(product, quantity);
		this.minQuantity = minQuantity;
		this.isPC = isPC;
	}

	public WarehouseItem() {

	}

	public int getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(int minQuantity) {
		this.minQuantity = minQuantity;
	}

	public boolean isPC() {
		return isPC;
	}
}
