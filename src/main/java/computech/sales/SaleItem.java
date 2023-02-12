package computech.sales;

import computech.catalog.Hardware;
import computech.catalog.HardwareType;
import lombok.NonNull;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * Class to track SaleItems
 */

public class SaleItem extends Product {

	public SaleItem(){}

	public SaleItem(String name, MonetaryAmount price){
		super(name,price);
	}

	@Override
	public Product setPrice(@NonNull MonetaryAmount price) {
		return super.setPrice(price);
	}
}
