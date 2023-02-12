package computech.warehouse;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface WarehouseRepository extends UniqueInventory<WarehouseItem> {

	Streamable<WarehouseItem> findAll();

}
