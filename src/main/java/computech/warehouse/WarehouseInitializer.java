package computech.warehouse;

import computech.catalog.*;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Initializes the Standard warehouseItems into the warehouse
 */
@Component
public class WarehouseInitializer implements DataInitializer {

	private final UniqueInventory<WarehouseItem> warehouse;
	private final ProductRepositoryHardware productRepositoryHardware;

	public WarehouseInitializer(@Qualifier("warehouseRepository") UniqueInventory<WarehouseItem> warehouse, ProductRepositoryHardware productRepositoryHardware) {
		this.warehouse = warehouse;

		this.productRepositoryHardware = productRepositoryHardware;
	}
	/**
	 * Initialize.
	 */
	@Override
	public void initialize() {
		productRepositoryHardware.findByType(HardwareType.PC).forEach(hardware -> {
			if (warehouse.findByProduct(hardware).isEmpty()) {
				warehouse.save(new WarehouseItem(hardware, Quantity.of(5),10, true));
			}
		});
		productRepositoryHardware.findAll().forEach(hardware -> {
			if (warehouse.findByProduct(hardware).isEmpty()) {
				warehouse.save(new WarehouseItem(hardware, Quantity.of(5),10, false));
			}
		});

	}
}
