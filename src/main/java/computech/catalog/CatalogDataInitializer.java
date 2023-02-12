package computech.catalog;

import computech.warehouse.WarehouseRepository;
import org.salespointframework.core.DataInitializer;

import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(20)
class CatalogDataInitializer implements DataInitializer{
    
    private final CompuTechCatalog catalog;

    private final WarehouseRepository warehouseRepository;

	public Quantity quantity;

    CatalogDataInitializer(CompuTechCatalog catalog, WarehouseRepository warehouseRepository){
		this.warehouseRepository = warehouseRepository;

		Assert.notNull(catalog, "catalog must not be null!");

		this.catalog = catalog;
    }

    @Override
    public void initialize() {

		warehouseRepository.findAll().forEach(hardware -> {
			/**
			 * Inserting new Repo
			 */
			if (catalog.findByName(hardware.getProduct().getName()).isEmpty()) {
				if (hardware.isPC()) {
					catalog.save((PC) hardware.getProduct());
				} else {
					catalog.save((Accessoire) hardware.getProduct());
				}
			}

		});
	}
}