package computech.warehouse;

import computech.catalog.*;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.order.*;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;
import java.util.List;
import static org.salespointframework.core.Currencies.EURO;

@Component
public class WarehouseManagement {


	private OrderManagement orderManagement;

	private WarehouseRepository warehouseRepository;

	private CompuTechCatalog compuTechCatalog;

	public WarehouseManagement(OrderManagement orderManagement, WarehouseRepository warehouseRepository, CompuTechCatalog compuTechCatalog) {
		this.orderManagement = orderManagement;
		this.warehouseRepository = warehouseRepository;
		this.compuTechCatalog = compuTechCatalog;
	}

	/**
	 * change if Item would be shown in Catalog
	 *
	 * @param tempInventoryItem Inventory Item Identifier in String format
	 * @param i       			Boolean if Item will be shown in Catalog
	 * @return 					void
	 */

	public void updateShowabilityOfItem(String tempInventoryItem, boolean i){
		WarehouseItem warehouseItem=this.findByStringInventoryItem(tempInventoryItem);
		if(warehouseItem.isPC()){
			PC pc= (PC) warehouseItem.getProduct();
			pc.setInCatalogshown(i);
		}else {
		Accessoire accessoire= (Accessoire) warehouseItem.getProduct();
		accessoire.setInCatalogshown(i);}
	}

	/**
	 * Returns Warehouse Item by String identifier
	 *
	 * @param stringInventoryItem
	 * @return WarehouseItem
	 */

	public WarehouseItem findByStringInventoryItem(String stringInventoryItem){
		List<WarehouseItem> warehouseItemList=warehouseRepository.findAll().toList();
		for(WarehouseItem s:warehouseItemList){
			if(s.getId().getIdentifier().equals(stringInventoryItem)){
				WarehouseItem warehouseItem=warehouseRepository.findById(s.getId()).get();
				return warehouseItem;
			}
		}
		return null;

	}
	/**
	 * Takes an object after reselling and saves it into warehouse
	 *
	 * @param product Product which is given
	 * @return void
	 */

	public void reselling(Product product,Integer i){
		WarehouseItem warehouseItem=warehouseRepository.findByProduct(product).get();
		warehouseItem.increaseQuantity(Quantity.of((long)i));
		warehouseRepository.save(warehouseItem);
	}

	/**
	 * Returns product by Inventory identifier
	 *
	 * @param inventoryItemIdentifier of a Product which is given
	 * @return product
	 */

	public Product findProductByInventoryItemIdentifier(InventoryItemIdentifier inventoryItemIdentifier){
		WarehouseItem warehouseItem=warehouseRepository.findById(inventoryItemIdentifier).get();
		Product product=warehouseItem.getProduct();
		return product;
	}

	/**
	 * delete Item from warehouse by product
	 *
	 * @param product of a Product which is given
	 * @return product
	 */
	public void deleteItem(Product product) {
		WarehouseItem warehouseItemDeleted = warehouseRepository.findByProductIdentifier(product.getId()).get();
		warehouseRepository.deleteById(warehouseItemDeleted.getId());
	}

	/**
	 * is called by assigment and approves it for reoder
	 *
	 * @param i number of items
	 * @param warehouseItem item to be reodered
	 * @return void
	 */

	public void confirmReoderAssigment(int i,WarehouseItem warehouseItem) {
		if(warehouseItem.getQuantity().getAmount().intValue()>=warehouseItem.getMinQuantity()){
			System.out.println("Warehouse has sufficient Quantity!");
		}else {

			WarehouseItem warehouseItemTemp_= (WarehouseItem) warehouseItem.increaseQuantity(Quantity.of((long)i));
			warehouseRepository.save(warehouseItemTemp_);
		}
	}

	/**
	 * Update warehouse Item
	 *
	 * @param ProductName name of the Product from catalog
 	 * @param monetary price of the product
	 * @param quantity number of Items in warehouse repository
	 * @param minquantity minimal
	 * @param inventoryItemIdentifier
	 * @return void
	 */

	public void updateItemInCatalogWarehouse(String ProductName,String monetary,
											 String quantity, String minquantity,
											 String inventoryItemIdentifier){
		System.out.println("productname: "+ProductName);
		System.out.println("monetary: "+monetary);
		System.out.println("quantity: "+quantity);
		System.out.println("minquantity: "+minquantity);
		System.out.println("inventoryItemIdentifier: "+inventoryItemIdentifier);

		WarehouseItem warehouseItem=this.findByStringInventoryItem(inventoryItemIdentifier);

		warehouseItem.getProduct().setName(ProductName);
		warehouseRepository.save(warehouseItem);

		monetary=monetary.replace("EUR ","");
		int monetaryamount_int=Integer.parseInt(monetary);
		warehouseItem.getProduct().setPrice(Money.of(monetaryamount_int,EURO));
		warehouseRepository.save(warehouseItem);

		int quantity_i=Integer.parseInt(quantity);

			if(quantity_i-warehouseItem.getQuantity().getAmount().intValue()>0){
				warehouseItem.increaseQuantity(Quantity.of((long)quantity_i-warehouseItem.getQuantity().getAmount().intValue()));
				warehouseRepository.save(warehouseItem);
			}else {
				warehouseItem.decreaseQuantity(Quantity.of((long)warehouseItem.getQuantity().getAmount().intValue()-quantity_i));
				warehouseRepository.save(warehouseItem);
			}

		int minquqantity_i=Integer.parseInt(minquantity);
			warehouseItem.setMinQuantity(minquqantity_i);
			warehouseRepository.save(warehouseItem);

	}
	/**
	 * closes order after assigment call
	 *
	 * @param order to be closed
	 * @return void
	 */

	public void confirmoder(Order order) {

		orderManagement.completeOrder(order);

	}

}