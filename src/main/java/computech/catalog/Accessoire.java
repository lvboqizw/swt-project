package computech.catalog;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;


@Entity
public class Accessoire extends Hardware{
	
	protected Accessoire(){
		super();
	}

	public Accessoire(String name, String producer, String detail,  String date, String image, MonetaryAmount price,boolean inCatalogshown, HardwareType type){
		super(name, producer, detail,  date, image, price,inCatalogshown, type);
	}

}
