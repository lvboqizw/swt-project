package computech.catalog;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * This Class is handling Accessoires that were added to a PC
 */
@Entity
public class ShadowAccessoire extends Accessoire{

	public ShadowAccessoire(){super();}

	public ShadowAccessoire(String name, String producer, String detail, String date, String image, MonetaryAmount price,boolean inCatalogshown, HardwareType type){
		super(name, producer, detail,  date, image, price,inCatalogshown, type);
	}
}
